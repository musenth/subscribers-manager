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

@Service
class SubInfoManager(
        private val persistenceInteractor: PersistenceInteractor
) : SubInfoService {

    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val DATE_FORMAT_REGEX = """\d{4}[-]\d{2}[-]\d{2}\s\d{2}[:]\d{2}[:]\d{2}""".toRegex()
    }

    override fun addCellIdToCtnBind(
            ctn: Long,
            cellId: Long
    ): OperationResult {
        val result = OperationResult()
        if (ctn < 1) {
            result.status = OperationStatus.ERROR
            result.errors.add(NEGATIVE_CTN)
        }
        if (cellId < 1) {
            result.status = OperationStatus.ERROR
            result.errors.add(NEGATIVE_CELL_ID)
        }
        if (result.status != OperationStatus.ERROR) {
            val answer = persistenceInteractor.addCellIdToCtnBind(ctn, cellId)
            if (!answer) {
                result.status = OperationStatus.ERROR
                result.errors.add(QUERY_EXECUTION_FAILED)
            }
        }
        return result
    }

    override fun addSubInfo(
            ctn: Long,
            name: String,
            email: String,
            activateDate: String
    ): OperationResult {
        val result = OperationResult()
        if (ctn < 1) {
            result.status = OperationStatus.ERROR
            result.errors.add(NEGATIVE_CTN)
        }
        if (name.isBlank()) {
            result.status = OperationStatus.ERROR
            result.errors.add(EMPTY_NAME)
        }
        if (email.isBlank()) {
            result.status = OperationStatus.ERROR
            result.errors.add(EMPTY_EMAIL)
        }
        if (activateDate.isBlank()) {
            result.status = OperationStatus.ERROR
            result.errors.add(EMPTY_ACTIVATE_DATE)
        }
        if (!activateDate.matches(DATE_FORMAT_REGEX)) {
            result.status = OperationStatus.ERROR
            result.errors.add(WRONG_FORMATED_TIMESTAMP)
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            result.status = OperationStatus.ERROR
            result.errors.add(INVALID_EMAIL)
        }
        if (result.status != OperationStatus.ERROR) {
            val answer = persistenceInteractor.addSubInfo(ctn, name, email, Timestamp(DATE_FORMAT.parse(activateDate).time))
            if (!answer) {
                result.status = OperationStatus.ERROR
                result.errors.add(QUERY_EXECUTION_FAILED)
            }
        }
        return result
    }

    override fun getAllCellIdEntries(): Pair<OperationResult, MutableList<CellIdEntry>> {
        val queryResult = persistenceInteractor.getAllCellIdEntries()
        return if (queryResult == null) Pair(
                OperationResult(
                        OperationStatus.ERROR,
                        mutableListOf(QUERY_EXECUTION_FAILED)
                ),
                mutableListOf()
        )
        else Pair(
                OperationResult(),
                queryResult
        )
    }

    override fun getAllSubInfo(): Pair<OperationResult, MutableList<SubInfoEntry>> {
        val queryResult = persistenceInteractor.getAllSubInfo()
        return if (queryResult == null) Pair(
                OperationResult(
                        OperationStatus.ERROR,
                        mutableListOf(QUERY_EXECUTION_FAILED)
                ),
                mutableListOf()
        )
        else Pair(
                OperationResult(),
                queryResult
        )
    }

    override fun getSubInfoByCellId(cellId: Long): Pair<OperationResult, MutableList<SubInfoEntry>> {
        val queryResult = persistenceInteractor.getSubInfoByCellId(cellId)
        return if (queryResult == null) Pair(
                OperationResult(
                        OperationStatus.ERROR,
                        mutableListOf(QUERY_EXECUTION_FAILED)
                ),
                mutableListOf()
        )
        else Pair(
                OperationResult(),
                queryResult
        )
    }

}