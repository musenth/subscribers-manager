package com.interminable.subscribers.manager.service

import com.interminable.subscribers.manager.data.PersistenceInteractor
import com.interminable.subscribers.manager.model.CellIdEntry
import com.interminable.subscribers.manager.model.OperationResult
import com.interminable.subscribers.manager.model.OperationStatus
import com.interminable.subscribers.manager.model.SubInfoEntry
import com.interminable.subscribers.manager.util.ServiceErrorMessages.EMPTY_ACTIVATE_DATE
import com.interminable.subscribers.manager.util.ServiceErrorMessages.EMPTY_EMAIL
import com.interminable.subscribers.manager.util.ServiceErrorMessages.EMPTY_NAME
import com.interminable.subscribers.manager.util.ServiceErrorMessages.INVALID_EMAIL
import com.interminable.subscribers.manager.util.ServiceErrorMessages.NEGATIVE_CELL_ID
import com.interminable.subscribers.manager.util.ServiceErrorMessages.NEGATIVE_CTN
import com.interminable.subscribers.manager.util.ServiceErrorMessages.QUERY_EXECUTION_FAILED
import com.interminable.subscribers.manager.util.ServiceErrorMessages.WRONG_FORMATED_TIMESTAMP
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.text.SimpleDateFormat

/**
 * Класс являющийся бизнес-слоем приложения
 */
@Service
class SubInfoManager(
        private val persistenceInteractor: PersistenceInteractor
) : SubInfoService {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val DATE_FORMAT_REGEX = """\d{4}[-]\d{2}[-]\d{2}\s\d{2}[:]\d{2}[:]\d{2}""".toRegex()
    }

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    override fun addCellIdToCtnBind(
            ctn: Long,
            cellId: Long
    ): OperationResult {
        val result = OperationResult()
        if (ctn < 1) result.addError(NEGATIVE_CTN)
        if (cellId < 1) result.addError(NEGATIVE_CELL_ID)
        if (result.status != OperationStatus.ERROR) {
            val answer = persistenceInteractor.addCellIdToCtnBind(ctn, cellId)
            if (!answer) result.addError(QUERY_EXECUTION_FAILED)
        }
        return result
    }

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    override fun addSubInfo(
            ctn: Long,
            name: String,
            email: String,
            activateDate: String
    ): OperationResult {
        val result = OperationResult()
        if (ctn < 1) result.addError(NEGATIVE_CTN)
        if (name.isBlank()) result.addError(EMPTY_NAME)
        if (email.isBlank()) result.addError(EMPTY_EMAIL)
        if (activateDate.isBlank()) result.addError(EMPTY_ACTIVATE_DATE)
        if (!activateDate.matches(DATE_FORMAT_REGEX)) result.addError(WRONG_FORMATED_TIMESTAMP)
        if (!EmailValidator.getInstance().isValid(email)) result.addError(INVALID_EMAIL)
        if (result.status != OperationStatus.ERROR) {
            val answer = persistenceInteractor.addSubInfo(ctn, name, email, Timestamp(DATE_FORMAT.parse(activateDate).time))
            if (!answer) result.addError(QUERY_EXECUTION_FAILED)
        }
        return result
    }

    /**
     * Возвращает все записи из таблицы, хранящей связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    override fun getAllCellIdEntries() = persistenceInteractor.getAllCellIdEntries().queryResponse()

    /**
     * Возвращает все записи из таблицы, хранящей профили абонентов (SubInfo)
     */
    override fun getAllSubInfo()= persistenceInteractor.getAllSubInfo().queryResponse()

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    override fun getSubInfoByCellId(cellId: Long) = persistenceInteractor.getSubInfoByCellId(cellId).queryResponse()

    /**
     * Возвращает пару статус/результат выполненного запроса.
     *
     * При this == null возвращает стутус ERROR
     */
    private fun <T> MutableList<T>?.queryResponse(): Pair<OperationResult, MutableList<T>> =
            if (this == null) Pair(
                    OperationResult(
                            OperationStatus.ERROR,
                            mutableListOf(QUERY_EXECUTION_FAILED)
                    ),
                    mutableListOf()
            )
            else Pair(
                    OperationResult(),
                    this
            )

    /**
     * Меняет статус выполнения операции на ERROR и добавляет сообщение об ошибке
     */
    private fun OperationResult.addError(error: String) {
        this.status = OperationStatus.ERROR
        this.errors.add(error)
    }
}