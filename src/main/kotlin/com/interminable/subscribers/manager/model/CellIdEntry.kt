package com.interminable.subscribers.manager.model

/**
 * Класс, хранящий связь номера базовой станции (Cell Id) и номера абонента (CTN)
 */
data class CellIdEntry(
        val ctn: Long,
        val cellId: Long
)