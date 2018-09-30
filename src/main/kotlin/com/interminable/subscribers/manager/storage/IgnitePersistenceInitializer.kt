package com.interminable.subscribers.manager.storage

import com.interminable.subscribers.manager.data.JdbcPersistenceInteractor
import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

/**
 * Класс, инициализирующий Apache Ignite
 */
@Component
class IgnitePersistenceInitializer {

    @Autowired
    private lateinit var igniteConfiguration: IgniteConfiguration

    /**
     * Стартует Apache Ignite
     *
     * Стартует при запуске программы, когда спрингом "подтягиваются" все бины
     */
    @Bean
    fun ignite(): Ignite {
        val ignite = Ignition.start(igniteConfiguration)
        ignite.cluster().active(true)

        val igniteInteractor = JdbcPersistenceInteractor()
        igniteInteractor.createCellIdTable()
        igniteInteractor.createSubInfoTable()

        return ignite
    }
}