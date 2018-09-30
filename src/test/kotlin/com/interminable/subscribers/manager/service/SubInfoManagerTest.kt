package com.interminable.subscribers.manager.service

import com.interminable.subscribers.manager.data.PersistenceInteractor
import com.interminable.subscribers.manager.model.OperationStatus
import com.interminable.subscribers.manager.util.ServiceErrorMessages.EMPTY_ACTIVATE_DATE
import com.interminable.subscribers.manager.util.ServiceErrorMessages.EMPTY_EMAIL
import com.interminable.subscribers.manager.util.ServiceErrorMessages.EMPTY_NAME
import com.interminable.subscribers.manager.util.ServiceErrorMessages.INVALID_EMAIL
import com.interminable.subscribers.manager.util.ServiceErrorMessages.NEGATIVE_CELL_ID
import com.interminable.subscribers.manager.util.ServiceErrorMessages.NEGATIVE_CTN
import com.interminable.subscribers.manager.util.ServiceErrorMessages.QUERY_EXECUTION_FAILED
import com.interminable.subscribers.manager.util.ServiceErrorMessages.WRONG_FORMATED_TIMESTAMP
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SubInfoManagerTest {

    private var persistenceInteractor = mockk<PersistenceInteractor>()
    private var serviceUnderTest = SubInfoManager(persistenceInteractor)

    /* POSITIVE SITUATIONS */

    @Test
    @DisplayName("1. При вводе валидных данных для таблицы c cell_id, сервис возвращает статус OK без ошибок")
    fun t1_addCellIdEntry_withValidData_Test() {
        every { persistenceInteractor.addCellIdToCtnBind(any(), any()) } answers { true }
        val result = serviceUnderTest.addCellIdToCtnBind(1, 1)
        assertTrue(result.status == OperationStatus.OK)
        assertTrue(result.errors.size == 0)
    }

    @Test
    @DisplayName("2. При вводе валидных данных для таблицы с профилями абонентов, сервис возвращает статус ОК без ошибок")
    fun t2_addSubInfoEntry_withValidData_Test() {
        every { persistenceInteractor.addSubInfo(any(), any(), any(), any()) } answers { true }
        val result = serviceUnderTest.addSubInfo(1, "Peter", "asdf@gmail.com", "2018-09-30 20:02:14")
        assertTrue(result.status == OperationStatus.OK)
        assertTrue(result.errors.size == 0)
    }

    /* NEGATIVE SITUATIONS */

    @Test
    @DisplayName("3. При вводе отрицательного ctn в таблицу с cell_id, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t3_addCellIdEntry_withNegativeCtn_Test() {
        val result = serviceUnderTest.addCellIdToCtnBind(-15, 3)
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(NEGATIVE_CTN, result.errors.first())
    }

    @Test
    @DisplayName("4. При вводе отрицательного cell_id, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t4_addCellIdEntry_withNegativeCellId_Test() {
        val result = serviceUnderTest.addCellIdToCtnBind(5, -11)
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(NEGATIVE_CELL_ID, result.errors.first())
    }

    @Test
    @DisplayName("5. При вводе отрицательного ctn в таблицу с профилями абонентов, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t5_addSubInfoEntry_withNegativeCtn_Test() {
        val result = serviceUnderTest.addSubInfo(-15, "Peter", "asdf@gmail.com", "2018-09-30 20:02:14")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(NEGATIVE_CTN, result.errors.first())
    }

    @Test
    @DisplayName("6. При вводе невалидного email, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t6_addSubInfoEntry_withInvalidEmail_Test() {
        val result = serviceUnderTest.addSubInfo(1, "Peter", "asdfwe", "2018-09-30 20:02:14")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(INVALID_EMAIL, result.errors.first())
    }

    @Test
    @DisplayName("7. При вводе даты активации в неправильном формате, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t7_addSubInfoEntry_withTimestampInWrongFormat_Test() {
        val result = serviceUnderTest.addSubInfo(1, "Peter", "asdf@gmail.com", "67124178 98972")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(WRONG_FORMATED_TIMESTAMP, result.errors.first())
    }

    @Test
    @DisplayName("8. Если поле name не заполнено, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t8_addSubInfoEntry_withoutName_Test() {
        val result = serviceUnderTest.addSubInfo(1, "", "asdf@gmail.com", "2018-09-30 20:02:14")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(EMPTY_NAME, result.errors.first())
    }

    @Test
    @DisplayName("9. Если поле email не заполнено, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t9_addSubInfoEntry_withoutEmail_Test() {
        val result = serviceUnderTest.addSubInfo(1, "Peter", "", "2018-09-30 20:02:14")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(EMPTY_EMAIL, result.errors.first())
    }

    @Test
    @DisplayName("10. Если поле activateDate не заполнено, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t10_addSubInfoEntry_withoutActivateDate_Test() {
        val result = serviceUnderTest.addSubInfo(1, "Peter", "asdf@gmail.com", "")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(EMPTY_ACTIVATE_DATE, result.errors.first())
    }

    @Test
    @DisplayName("11. Если при вводе валидных данных для таблицы c cell_id, запрос не удалось выполнить, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t11_addCellIdEntry_withSqlError_Test() {
        every { persistenceInteractor.addCellIdToCtnBind(any(), any()) } answers { false }
        val result = serviceUnderTest.addCellIdToCtnBind(1, 1)
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(QUERY_EXECUTION_FAILED, result.errors.first())
    }

    @Test
    @DisplayName("12. Если при вводе валидных данных для таблицы c профилями абонентов, запрос не удалось выполнить, сервис возвращает статус ERROR с пометкой в поле errors")
    fun t12_addSubInfoEntry_withValidData_Test() {
        every { persistenceInteractor.addSubInfo(any(), any(), any(), any()) } answers { false }
        val result = serviceUnderTest.addSubInfo(1, "Peter", "asdf@gmail.com", "2018-09-30 20:02:14")
        assertTrue(result.status == OperationStatus.ERROR)
        assertTrue(result.errors.size != 0)
        assertEquals(QUERY_EXECUTION_FAILED, result.errors.first())
    }
}