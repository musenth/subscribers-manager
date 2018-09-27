package com.interminable.subscribers.manager.data

import com.interminable.subscribers.manager.model.SubInfoEntry
import com.interminable.subscribers.manager.util.SqlPatterns.CELL_ID_TABLE_CREATE_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.CELL_ID_TABLE_INSERT_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.CTN_TABLE_CREATE_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.CTN_TABLE_INSERT_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_BY_CELL_ID_SELECT_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_TABLE_CREATE_PATTERN
import com.interminable.subscribers.manager.util.SqlPatterns.SUB_INFO_TABLE_INSERT_PATTERN
import java.sql.Connection
import java.sql.Date
import java.sql.DriverManager
import java.sql.Statement

/**
 * Класс, взаимодействующий с базой данных Apache Ignite
 */
class IgnitePersistenceInteractor {

    /**
     * Абстракция над подключением к БД
     */
    private var connection: Connection

    /**
     * Абстракция над экзекьютором запросов (в данном кейсе для CREATE и SELECT запросов)
     */
    private var statement: Statement

    init {
        Class.forName("org.apache.ignite.IgniteJdbcDriver") // TODO вынести в пропертя
        connection = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/") // TODO заменить хардкор значение IP-адресса Ignite на переменную, берущуюся из ENV
        statement = connection.createStatement()
        // TODO обработка исключений и логи
    }

    /**
     * Создает таблицу, хранящую связи номеров базовой станции (Cell Id) и номеров абонента (CTN) (один ко многим)
     */
    fun createCellIdTable() {
        statement.executeUpdate(CELL_ID_TABLE_CREATE_PATTERN)
        // TODO обработка исключений и логи
    }

    /**
     * Создает таблицу, хранящую связи номеров абонента (CTN) и профили абонентов (SubInfo) (один к одному)
     */
    fun createCtnTable() {
        statement.executeUpdate(CTN_TABLE_CREATE_PATTERN)
        // TODO обработка исключений и логи
    }

    /**
     * Создает таблицу, хранящую профили абонентов (SubInfo)
     */
    fun createSubInfoTable() {
        statement.executeUpdate(SUB_INFO_TABLE_CREATE_PATTERN)
        // TODO обработка исключений и логи
    }

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    fun addCellIdToCtnBuild(
            id: Long,
            cellId: Long,
            ctn: Long
    ) {
        val statement = connection.prepareStatement(CELL_ID_TABLE_INSERT_PATTERN)
        statement.setLong(1, id)
        statement.setLong(2, cellId)
        statement.setLong(3, ctn)
        statement.execute()
        // TODO обработка исключений и логи
    }

    /**
     * Добавляет новую запись, содержащую связь номера абонента (CTN) и профиля абонента (SubInfo)
     */
    fun addCtnToSubInfoBind(
            ctn: Long,
            subInfoId: Long
    ) {
        val statement = connection.prepareStatement(CTN_TABLE_INSERT_PATTERN)
        statement.setLong(1, ctn)
        statement.setLong(2, subInfoId)
        statement.execute()
        // TODO обработка исключений и логи
    }

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    fun addSubInfo(
            id: Long,
            name: String,
            email: String,
            activateDate: Date
    ) {
        val statement = connection.prepareStatement(SUB_INFO_TABLE_INSERT_PATTERN)
        statement.setLong(1, id)
        statement.setString(2, name)
        statement.setString(3, email)
        statement.setDate(4, activateDate)
        statement.execute()
        // TODO обработка исключений и логи
    }

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    fun getSubInfoWithCtn(
            cellId: Long
    ): MutableList<SubInfoEntry> {
        val result = statement.executeQuery("$SUB_INFO_BY_CELL_ID_SELECT_PATTERN$cellId")
        val listOfMatches = mutableListOf<SubInfoEntry>()
        while (result.next()) {
            listOfMatches.add(SubInfoEntry(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getDate(4)
            ))
        }
        return listOfMatches
    }

    fun dropTables() {
        statement.executeUpdate("""DROP TABLE cell_id_info""")
        statement.executeUpdate("""DROP TABLE ctn_info""")
        statement.executeUpdate("""DROP TABLE sub_info""")
    }
}