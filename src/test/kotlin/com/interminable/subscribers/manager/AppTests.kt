package com.interminable.subscribers.manager

import com.interminable.subscribers.manager.data.JdbcPersistenceInteractor
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class AppTests {

    @Test
    fun contextLoads() {
    }

    @Test
    @Disabled
    fun dropAllIgniteTables() {
        JdbcPersistenceInteractor().apply {
            dropCellIdTable()
            dropSubInfo()
        }
    }

}
