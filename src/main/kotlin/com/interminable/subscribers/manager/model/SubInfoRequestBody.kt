package com.interminable.subscribers.manager.model

/**
 * Класс, в который записываются профили абонентов по заданному номеру базовой станции и их количество
 */
data class SubInfoRequestBody (
        var total: Int,
        var results: MutableList<SubInfoEntry>
)