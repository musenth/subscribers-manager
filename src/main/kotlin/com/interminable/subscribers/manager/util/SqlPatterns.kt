package com.interminable.subscribers.manager.util

/**
 * Объект, содержащий шаблоны для формирования SQL запросов
 */
object SqlPatterns {

    /* SQL CREATE PATTERNS */
    val CELL_ID_TABLE_CREATE_PATTERN = """CREATE TABLE IF NOT EXISTS cell_id_info
        |(id LONG PRIMARY KEY, cell_id LONG NOT NULL, ctn LONG NOT NULL)
        |""".replaceLineBreaks()
    val CTN_TABLE_CREATE_PATTERN = """CREATE TABLE IF NOT EXISTS ctn_info
        |(ctn LONG PRIMARY KEY, sub_info_id LONG NOT NULL)
        |""".replaceLineBreaks()
    val SUB_INFO_TABLE_CREATE_PATTERN = """CREATE TABLE IF NOT EXISTS sub_info
        |(id LONG PRIMARY KEY, name VARCHAR NOT NULL, email VARCHAR NOT NULL, activate_date TIMESTAMP NOT NULL)
        |""".replaceLineBreaks()

    /* SQL INSERT PATTERNS */
    val CELL_ID_TABLE_INSERT_PATTERN = """INSERT INTO cell_id_info (id, cell_id, ctn) VALUES (?, ?, ?)"""
    val CTN_TABLE_INSERT_PATTERN = """INSERT INTO ctn_info (ctn, sub_info_id) VALUES (?, ?)"""
    val SUB_INFO_TABLE_INSERT_PATTERN = """INSERT INTO sub_info (id, name, email, activate_date) VALUES (?, ?, ?, ?)"""

    /* SQL SELECT PATTERNS */
    val SUB_INFO_BY_CELL_ID_SELECT_PATTERN = """SELECT ctn_info.ctn, name, email, activate_date
        |FROM cell_id_info
        |INNER JOIN ctn_info
        |ON cell_id_info.ctn=ctn_info.ctn
        |INNER JOIN sub_info
        |ON ctn_info.sub_info_id=sub_info.id
        |WHERE cell_id_info.cell_id=
        |""".replaceLineBreaks()
}