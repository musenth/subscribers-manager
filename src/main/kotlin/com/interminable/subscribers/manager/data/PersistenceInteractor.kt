package com.interminable.subscribers.manager.data

import com.interminable.subscribers.manager.model.CellIdEntry
import com.interminable.subscribers.manager.model.SubInfoEntry
import java.sql.Timestamp

/**
 * Интерфейс, описывающий методы, которые должны быть реализованы для реализации
 */
interface PersistenceInteractor {

    /* CREATE TABLE */

    /**
     * Создает таблицу, хранящую связи номеров базовой станции (Cell Id) и номеров абонента (CTN) (один ко многим)
     */
    fun createCellIdTable(): Boolean

    /**
     * Создает таблицу, хранящую профили абонентов (SubInfo)
     */
    fun createSubInfoTable(): Boolean

    /* DROP TABLE */

    /**
     * Удаляет таблицу, хранящуюю связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    fun dropCellIdTable(): Boolean

    /**
     * Удаляет таблицу, хранящуюю профили абонентов
     */
    fun dropSubInfo(): Boolean

    /* INSERT */

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    fun addCellIdToCtnBind(
            ctn: Long,
            cellId: Long
    ): Boolean

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    fun addSubInfo(
            ctn: Long,
            name: String,
            email: String,
            activateDate: Timestamp
    ): Boolean

    /* SELECT */

    /**
     * Возвращает все записи из таблицы, хранящей связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    fun getAllCellIdEntries(): MutableList<CellIdEntry>?

    /**
     * Возвращает все записи из таблицы, хранящей профили абонентов (SubInfo)
     */
    fun getAllSubInfo(): MutableList<SubInfoEntry>?

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    fun getSubInfoByCellId(
            cellId: Long
    ): MutableList<SubInfoEntry>?
}