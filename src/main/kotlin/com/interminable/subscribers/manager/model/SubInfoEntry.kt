package com.interminable.subscribers.manager.model

import java.sql.Timestamp

/**
 * Класс, хранящий информацию о профиле абонента
 */
data class SubInfoEntry(
        val ctn: Long,
        val name: String,
        val email: String,
        val activateDate: String
)