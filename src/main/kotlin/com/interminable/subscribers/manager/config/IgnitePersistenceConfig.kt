package com.interminable.subscribers.manager.config

import org.apache.ignite.configuration.DataStorageConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.logger.slf4j.Slf4jLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Класс с методами, конфигурирующими Apache Ignite
 */
@Configuration
class IgnitePersistenceConfig {

    /**
     * Создает кастомную конфигурацию для Apache Ignite
     */
    @Bean
    fun igniteConfiguration() = IgniteConfiguration()
            .setDataStorageConfiguration(dataStorageConfiguration())
            .setGridLogger(Slf4jLogger())

    /**
     * Конфигурирует Apache Ignite для работы в режиме персистенса
     */
    @Bean
    fun dataStorageConfiguration() = DataStorageConfiguration().apply {
        defaultDataRegionConfiguration.isPersistenceEnabled = true
    }
}