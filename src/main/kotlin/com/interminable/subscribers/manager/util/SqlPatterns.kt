package com.interminable.subscribers.manager.util

/**
 * Объект, содержащий шаблоны для формирования SQL запросов
 */
object SqlPatterns {

    /**
     * Таблица, содержащая связь номера базовой станции (Cell ID) с номером абонента (CTN)
     */
    private val CELL_ID_TABLE_NAME = "cell_id_info"

    /**
     * Таблица, содержащая информацию об абоненте
     */
    private val SUB_INFO_TABLE_NAME = "sub_info"

    /* SQL CREATE PATTERNS */
    val CELL_ID_TABLE_CREATE_QUERY = """CREATE TABLE IF NOT EXISTS $CELL_ID_TABLE_NAME
        |(ctn LONG PRIMARY KEY, cell_id LONG NOT NULL)
        |""".replaceLineBreaks()
    val SUB_INFO_TABLE_CREATE_QUERY = """CREATE TABLE IF NOT EXISTS $SUB_INFO_TABLE_NAME
        |(ctn LONG PRIMARY KEY, name VARCHAR NOT NULL, email VARCHAR NOT NULL, activate_date TIMESTAMP NOT NULL)
        |""".replaceLineBreaks()

    /* SQL INSERT PATTERNS */
    val CELL_ID_TABLE_INSERT_QUERY_PATTERN = "INSERT INTO $CELL_ID_TABLE_NAME (ctn, cell_id) VALUES (?, ?)"
    val SUB_INFO_TABLE_INSERT_QUERY_PATTERN = "INSERT INTO $SUB_INFO_TABLE_NAME (ctn, name, email, activate_date) VALUES (?, ?, ?, ?)"

    /* SQL SELECT PATTERNS */
    val CELL_ID_TABLE_SELECT_QUERY = "SELECT * FROM $CELL_ID_TABLE_NAME"
    val SUB_INFO_TABLE_SELECT_QUERY = "SELECT * FROM $SUB_INFO_TABLE_NAME"

    val SUB_INFO_BY_CELL_ID_SELECT_QUERY_PATTERN = """SELECT $SUB_INFO_TABLE_NAME.ctn, name, email, activate_date
        |FROM $CELL_ID_TABLE_NAME
        |INNER JOIN $SUB_INFO_TABLE_NAME
        |ON $CELL_ID_TABLE_NAME.ctn=$SUB_INFO_TABLE_NAME.ctn
        |WHERE $CELL_ID_TABLE_NAME.cell_id=
        |""".replaceLineBreaks()

    /* SQL DROP PATTERNS */
    val CELL_ID_TABLE_DROP_PATTERN = "DROP TABLE IF EXISTS $CELL_ID_TABLE_NAME"
    val SUB_INFO_TABLE_DROP_PATTERN = "DROP TABLE IF EXISTS $SUB_INFO_TABLE_NAME"
}