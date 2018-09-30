package com.interminable.subscribers.manager.util

import com.interminable.subscribers.manager.service.SubInfoManager.Companion.DATE_FORMAT

object ServiceErrorMessages {
    val NEGATIVE_CTN = "Номер абонента (CTN) не может быть отрицательным и нулевым"
    val NEGATIVE_CELL_ID = "Номер базовой станции (Cell ID) не может быть отрицательным и нулевым"

    val INVALID_EMAIL = "Введен неверный email"
    val WRONG_FORMATED_TIMESTAMP = "Дата активации не соответствует формату: ${DATE_FORMAT.toPattern()}"
    val EMPTY_NAME = "Поле name должно быть заполнено"
    val EMPTY_EMAIL = "Поле email должно быть заполнено"
    val EMPTY_ACTIVATE_DATE = "Поле activateDate должно быть заполнено"

    val QUERY_EXECUTION_FAILED = "Не удалось выполнить запрос"
}