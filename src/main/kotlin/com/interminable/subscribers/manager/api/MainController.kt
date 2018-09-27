package com.interminable.subscribers.manager.api

import com.interminable.subscribers.manager.data.IgnitePersistenceInteractor
import com.interminable.subscribers.manager.model.SubInfoEntry
import com.interminable.subscribers.manager.model.SubInfoRequestBody
import com.interminable.subscribers.manager.service.SubInfoManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.sql.Date

/**
 * Класс, реализующий REST API для добавления и получения информации о профилях абонентов
 */
@RestController
class MainController {

    @Autowired
    lateinit var service: SubInfoManager

    /**
     * Добавляет новую запись, содержащую связь номера базовой станции (Cell Id) и номера абонента (CTN)
     */
    @PostMapping("/addCell")
    fun addCellIdToCtnBuild(
            @RequestParam id: Long,
            @RequestParam cellId: Long,
            @RequestParam ctn: Long
    ): String {
        service.addCellIdToCtnBuild(id, cellId, ctn)
        return "Done" // TODO система фидбека
    }

    /**
     * Добавляет новую запись, содержащую связь номера абонента (CTN) и профиля абонента (SubInfo)
     */
    @PostMapping("/addCtn")
    fun addCtnToEmailBind(
            @RequestParam ctn: Long,
            @RequestParam subId: Long
    ): String {
        service.addCtnToSubInfoBind(ctn, subId)
        return "Done" // TODO система фидбека
    }

    /**
     * Добавляет новую запись, содержащую профиль абонента (SubInfo)
     */
    @PostMapping("/addSub")
    fun addSubInfo(
            @RequestParam id: Long,
            @RequestParam name: String,
            @RequestParam email: String,
            @RequestParam activateDate: String
    ): String {
        service.addSubInfo(id, name, email, activateDate)
        return "Done" // TODO система фидбека
    }

    /**
     * Возвращает список профилей (SubInfo) по номеру базовой станции (CellId)
     */
    @GetMapping("/getSub")
    fun getSubInfoWithCtn(
            @RequestParam cellId: Long
    ): SubInfoRequestBody {
        val result = service.getSubInfoWithCtn(cellId)
        return SubInfoRequestBody(result.size, result)
    }
}