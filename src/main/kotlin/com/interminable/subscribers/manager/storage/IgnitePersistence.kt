package com.interminable.subscribers.manager.storage

import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.DataStorageConfiguration
import org.apache.ignite.configuration.IgniteConfiguration

/**
 * Класс для инициализации Apache Ignite в качестве персистенса
 */
class IgnitePersistence {

    /**
     * Запускает Ignite в качестве персистенса
     */
    fun start(): Ignite {
        val cfg = IgniteConfiguration()
        val storageCfg = DataStorageConfiguration()
        storageCfg.defaultDataRegionConfiguration.isPersistenceEnabled = true
        cfg.dataStorageConfiguration = storageCfg
        return Ignition.start(cfg) // TODO обработка исключения и логи
    }

}