package com.interminable.subscribers.manager.config

import org.apache.ignite.configuration.DataStorageConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Класс с методами, конфигурирующими Apache Ignite
 */
@Configuration
class IgniteConfig {

    /**
     * Создает кастомную конфигурацию для Apache Ignite
     */
    @Bean
    fun igniteConfiguration() = IgniteConfiguration().setDataStorageConfiguration(dataStorageConfiguration())

    /**
     * Конфигурирует Apache Ignite для работы в режиме персистенса
     */
    @Bean
    fun dataStorageConfiguration() = DataStorageConfiguration().apply {
        defaultDataRegionConfiguration.isPersistenceEnabled = true
    }
}