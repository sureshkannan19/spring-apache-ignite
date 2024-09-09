package com.sk.configuration;

import com.sk.enums.Caches;
import com.sk.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Objects;

@Configuration
@Slf4j
public class IgniteConfig {

    @Bean("igniteNodeConfiguration")
    public IgniteConfiguration igniteConfiguration() {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
        storageCfg.getDefaultDataRegionConfiguration().setName("POC_IGNITE_REGION");
        storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        storageCfg.getDefaultDataRegionConfiguration().setMaxSize(100L * 1024 * 1024); // 100MB

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setDataStorageConfiguration(storageCfg);
        // The node will be started as a client node.
        cfg.setClientMode(true);
        cfg.setIgniteInstanceName("my-ignite");
        cfg.setPeerClassLoadingEnabled(true);

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        return cfg;
    }

    @Bean("ignite")
    public Ignite startIgnite(@Qualifier("igniteNodeConfiguration") IgniteConfiguration cfg) {
        Ignite ignite = Ignition.getOrStart(cfg);
        if (ignite.cluster().state() != ClusterState.ACTIVE) {
            ignite.cluster().state(ClusterState.ACTIVE);
        }
        return ignite;
    }

    @Bean("igniteCacheConfiguration")
    public Ignite populateIgniteCacheConfig(@Qualifier("ignite") Ignite ignite) {
        for (Caches cache : Caches.values()) {
            log.info("Setting up cache config for {} ", cache.getCacheName());
            IgniteCache<Object, Object> igniteCache = ignite.cache(cache.getCacheName());
            CacheConfiguration cc = null;
            if (Objects.nonNull(igniteCache)) {
                if (!cache.getAtomicityMode().equals((cc = igniteCache.getConfiguration(CacheConfiguration.class)).getAtomicityMode())) {
                    log.info("Destroying & Re-loading cache config for {} ", cache.getCacheName());
                    ignite.destroyCache(cache.getCacheName());
                    updateCacheConfiguration(ignite, cache);
                }
            } else {
                log.info("Loading cache config for {} ", cache.getCacheName());
                updateCacheConfiguration(ignite, cache);
            }
        }
        return ignite;
    }

    private void updateCacheConfiguration(Ignite ignite, Caches cache) {
        CacheConfiguration<Long, Employee> clientCacheConfig = new CacheConfiguration<>(cache.getCacheName());
        clientCacheConfig.setIndexedTypes(cache.getIndexedTypes());
        clientCacheConfig.setCacheMode(CacheMode.REPLICATED);           // Override cache mode at client side
        clientCacheConfig.setAtomicityMode(cache.getAtomicityMode());  // Override atomicity mode
        clientCacheConfig.setBackups(1);
        ignite.addCacheConfiguration(clientCacheConfig);
        ignite.getOrCreateCache(clientCacheConfig);
    }

}
