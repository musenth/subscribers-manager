package com.interminable.subscribers.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Класс, описывающий взаимодействие с DB через JDBC.
 *
 * По умолчанию, настроен на работу с Apache Ignite в качестве персистенса на локальной машине (localhost)
 */
@ConfigurationProperties(prefix = "sub.manager.jdbc")
data class JdbcPersistenceInteractorConfig(
        val className: String = "org.apache.ignite.IgniteJdbcDriver",
        val url: String = "jdbc:ignite:thin://127.0.0.1"
)