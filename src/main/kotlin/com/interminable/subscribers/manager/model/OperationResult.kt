package com.interminable.subscribers.manager.model

/**
 * Класс, содержащий в себе информацию о выполнении операции и ошибки.
 */
data class OperationResult (
    var status: OperationStatus = OperationStatus.OK,
    val errors: MutableList<String> = mutableListOf()
)

/**
 * Статус выполнения операции
 */
enum class OperationStatus {
    OK, // Операция была завершена успешно
    ERROR // В процессе выполнения операции произошла(и) ошибка(и)
}