package com.interminable.subscribers.manager.model

import java.sql.Date

/**
 * Структура с информацией по профилям абонентов
 */
data class SubInfoEntry(
        var ctn: String,
        var name: String,
        var email: String,
        var activateDate: Date
)