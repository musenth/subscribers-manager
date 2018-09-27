package com.interminable.subscribers.manager.model

/**
 * Класс, в который записывается ответ
 */
data class SubInfoRequestBody (
        var total: Int,
        var results: MutableList<SubInfoEntry>
)