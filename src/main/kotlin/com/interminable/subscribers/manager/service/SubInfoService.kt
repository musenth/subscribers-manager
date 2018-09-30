package com.interminable.subscribers.manager.service

import com.interminable.subscribers.manager.model.CellIdEntry
import com.interminable.subscribers.manager.model.OperationResult
import com.interminable.subscribers.manager.model.SubInfoEntry

/**
 * Интерфейс, описывающий методы, которые должны присутствовать в сервисе для работы с информацией о профилях абонентов
 * и их связи с номерами базовых станций
 */
interface SubInfoService {

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    fun addCellIdToCtnBind(
            ctn: Long,
            cellId: Long
    ): OperationResult

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    fun addSubInfo(
            ctn: Long,
            name: String,
            email: String,
            activateDate: String
    ): OperationResult

    /**
     * Возвращает все записи из таблицы, хранящей связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    fun getAllCellIdEntries(): Pair<OperationResult, MutableList<CellIdEntry>>

    /**
     * Возвращает все записи из таблицы, хранящей профили абонентов (SubInfo)
     */
    fun getAllSubInfo(): Pair<OperationResult, MutableList<SubInfoEntry>>

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    fun getSubInfoByCellId(
            cellId: Long
    ): Pair<OperationResult, MutableList<SubInfoEntry>>
}