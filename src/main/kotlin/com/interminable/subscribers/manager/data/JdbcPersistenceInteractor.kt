package com.interminable.subscribers.manager.data

import com.interminable.subscribers.manager.config.JdbcPersistenceInteractorConfig
import com.interminable.subscribers.manager.model.CellIdEntry
import com.interminable.subscribers.manager.model.SubInfoEntry
import com.interminable.subscribers.manager.util.SqlPatterns.CELL_ID_TABLE_CREATE_QUERY
import com.interminable.subscribers.manager.util.SqlPatterns.CELL_ID_TABLE_DROP_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.CELL_ID_TABLE_INSERT_QUERY_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.CELL_ID_TABLE_SELECT_QUERY
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_BY_CELL_ID_SELECT_QUERY_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_TABLE_CREATE_QUERY
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_TABLE_DROP_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_TABLE_INSERT_QUERY_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_TABLE_SELECT_QUERY
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.sql.*

/**
 * Класс, взаимодействующий с базой данных Apache Ignite.
 *
 * Является DAO-слоем в данном приложении
 */
@Component
@EnableConfigurationProperties(JdbcPersistenceInteractorConfig::class)
class JdbcPersistenceInteractor(
        private val config: JdbcPersistenceInteractorConfig = JdbcPersistenceInteractorConfig()
) : PersistenceInteractor {

    init {
        Class.forName(config.className)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JdbcPersistenceInteractor::class.java)
    }

    /* CREATE TABLE */

    /**
     * Создает таблицу, хранящую связи номеров базовой станции (Cell Id) и номеров абонента (CTN) (один ко многим)
     */
    override fun createCellIdTable() = executeQuery(CELL_ID_TABLE_CREATE_QUERY)

    /**
     * Создает таблицу, хранящую профили абонентов (SubInfo)
     */
    override fun createSubInfoTable() = executeQuery(SUB_INFO_TABLE_CREATE_QUERY)

    /* DROP TABLE */

    /**
     * Удаляет таблицу, хранящуюю связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    override fun dropCellIdTable() = executeQuery(CELL_ID_TABLE_DROP_PATTERN)

    /**
     * Удаляет таблицу, хранящуюю профили абонентов
     */
    override fun dropSubInfo() = executeQuery(SUB_INFO_TABLE_DROP_PATTERN)

    /* INSERT */

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    override fun addCellIdToCtnBind(
            ctn: Long,
            cellId: Long
    ) = executeQuery(CELL_ID_TABLE_INSERT_QUERY_PATTERN) {
        setLong(1, ctn)
        setLong(2, cellId)
    }

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    override fun addSubInfo(
            ctn: Long,
            name: String,
            email: String,
            activateDate: Timestamp
    ) = executeQuery(SUB_INFO_TABLE_INSERT_QUERY_PATTERN) {
        setLong(1, ctn)
        setString(2, name)
        setString(3, email)
        setTimestamp(4, activateDate)
    }

    /* SELECT */

    /**
     * Возвращает все записи из таблицы, хранящей связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    override fun getAllCellIdEntries(): MutableList<CellIdEntry>? = executeSelectQuery(CELL_ID_TABLE_SELECT_QUERY) {
        CellIdEntry(
                getLong(1),
                getLong(2)
        )
    }

    /**
     * Возвращает все записи из таблицы, хранящей профили абонентов (SubInfo)
     */
    override fun getAllSubInfo(): MutableList<SubInfoEntry>? = executeSelectQuery(SUB_INFO_TABLE_SELECT_QUERY) {
        SubInfoEntry(
                getLong(1),
                getString(2),
                getString(3),
                getTimestamp(4).toString()
        )
    }

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    override fun getSubInfoByCellId(
            cellId: Long
    ): MutableList<SubInfoEntry>? = executeSelectQuery(
            "$SUB_INFO_BY_CELL_ID_SELECT_QUERY_PATTERN$cellId"
    ) {
        SubInfoEntry(
                getLong(1),
                getString(2),
                getString(3),
                getTimestamp(4).toString()
        )
    }

    /**
     * Метод, обрабатывающий CREATE, INSERT и DROP запросы
     *
     * @param query SQL запрос на выполнение
     * @param handle метод для обработки параметризированных запросов
     */
    private fun executeQuery(
            query: String,
            handle: (PreparedStatement.() -> Unit)? = null
    ): Boolean {
        var connection: Connection? = null
        var statement: Statement? = null
        try {
            connection = connect()
            if (handle == null) {
                statement = connection.statement()
                statement.executeUpdate(query)
            } else {
                statement = connection.statement(query) as PreparedStatement
                statement.handle()
                statement.execute()
            }
            LOGGER.debug("Executed a query: $query")
            return true
        } catch (e: SQLException) {
            LOGGER.error("Can't execute a SQL query: $query")
        } catch (e: SQLTimeoutException) {
            LOGGER.error("Timeout of a SQL query execution")
        } catch (e: Exception) {
            LOGGER.error("Can't connect to DB server with url: ${config.url}")
        } finally {
            statement?.close()
            connection?.close()
        }
        return false
    }

    /**
     * Метод, обрабатывающий SELECT запросы
     *
     * @param query SELECT запрос на выполнение
     * @param getEntity метод, описывающий формирование объекта после выполнения SELECT запроса
     */
    private fun <EntityType> executeSelectQuery(
            query: String,
            getEntity: ResultSet.() -> EntityType
    ): MutableList<EntityType>? {
        var connection: Connection? = null
        var statement: Statement? = null
        try {
            connection = connect()
            statement = connection.statement()

            val queryResult = statement.executeQuery(query)
            LOGGER.debug("Executed a query: $query")

            val result = mutableListOf<EntityType>()
            while (queryResult.next()) {
                result.add(queryResult.getEntity())
            }
            return result
        } catch (e: SQLException) {
            LOGGER.error("Can't execute a SELECT query: $query")
        } catch (e: SQLTimeoutException) {
            LOGGER.error("Timeout of a SELECT query execution")
        } catch (e: Exception) {
            LOGGER.error("Can't connect to DB server with url: ${config.url}")
        } finally {
            statement?.close()
            connection?.close()
        }
        return null
    }

    /**
     * Подключается к БД
     *
     * @return объект для взаимодействия с БД
     */
    private fun connect(): Connection {
        val connection = DriverManager.getConnection(config.url)
        LOGGER.debug("Connected to DB with url: ${config.url}")
        return connection
    }

    /**
     * Возвращает объект для обработки запросов
     *
     * @param queryPattern шаблон INSERT запроса на выполнение, необязательное поле
     */
    private fun Connection.statement(
            queryPattern: String = ""
    ): Statement {
        val statement = when {
            queryPattern.isNotBlank() -> this.prepareStatement(queryPattern)
            else -> this.createStatement()
        }
        LOGGER.debug("Created a Statement for query execution")
        return statement
    }
}