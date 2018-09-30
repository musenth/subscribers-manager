package com.interminable.subscribers.manager.api

import com.interminable.subscribers.manager.model.*
import com.interminable.subscribers.manager.service.SubInfoManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Класс, реализующий REST API для добавления и получения информации о профилях абонентов
 */
@RestController
class WebApiProvider {

    @Autowired
    lateinit var service: SubInfoManager

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    @PostMapping("/addCell")
    fun addCellIdToCtnBind(
            @RequestParam ctn: Long,
            @RequestParam cellId: Long
    ): OperationResult {
        return service.addCellIdToCtnBind(ctn, cellId)
    }

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    @PostMapping("/addSub")
    fun addSubInfo(
            @RequestParam ctn: Long,
            @RequestParam name: String,
            @RequestParam email: String,
            @RequestParam activateDate: String
    ): OperationResult {
        return service.addSubInfo(ctn, name, email, activateDate)
    }

    /**
     * Возвращает все записи из таблицы, хранящей связи номеров базовой станции (Cell Id) и номеров абонента (CTN)
     */
    @GetMapping("getCells")
    fun getAllCellIdEntries(): Any {
        val result = service.getAllCellIdEntries()
        return if (result.first.status == OperationStatus.ERROR) result.first
        else result.second
    }

    /**
     * Возвращает все записи из таблицы, хранящей профили абонентов (SubInfo)
     */
    @GetMapping("getSubs")
    fun getAllSubInfo(): Any {
        val result = service.getAllSubInfo()
        return if (result.first.status == OperationStatus.ERROR) result.first
        else result.second
    }

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    @GetMapping("getSubsByCell")
    fun getSubInfoByCellId(
            @RequestParam cellId: Long
    ): Any {
        val result = service.getSubInfoByCellId(cellId)
        return if (result.first.status == OperationStatus.ERROR) result.first
        else SubInfoRequestBody(
                result.second.size,
                result.second
        )
    }
}