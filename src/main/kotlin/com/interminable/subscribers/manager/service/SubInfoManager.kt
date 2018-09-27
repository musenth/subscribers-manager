package com.interminable.subscribers.manager.service

import com.interminable.subscribers.manager.data.IgnitePersistenceInteractor
import com.interminable.subscribers.manager.model.SubInfoEntry
import org.springframework.stereotype.Service
import java.sql.Date
import java.text.SimpleDateFormat

@Service
class SubInfoManager {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    fun addCellIdToCtnBuild(
            id: Long,
            cellId: Long,
            ctn: Long
    ) {
        interactor().addCellIdToCtnBuild(id, cellId, ctn)
    }

    /**
     * Добавляет новую запись, содержащую связь номера абонента (CTN) и профиля абонента (SubInfo)
     */
    fun addCtnToSubInfoBind(
            ctn: Long,
            subInfoId: Long
    ) {
        interactor().addCtnToSubInfoBind(ctn, subInfoId)
    }

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    fun addSubInfo(
            id: Long,
            name: String,
            email: String,
            activateDate: String
    ) {
        interactor().addSubInfo(id, name, email, Date(dateFormat.parse(activateDate).time))
    }

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    fun getSubInfoWithCtn(
            cellId: Long
    ): MutableList<SubInfoEntry> {
        return interactor().getSubInfoWithCtn(cellId)
    }

    /**
     * Создает объект класса, взаимодействующего с Apache Ignite
     */
    private fun interactor() = IgnitePersistenceInteractor()
}