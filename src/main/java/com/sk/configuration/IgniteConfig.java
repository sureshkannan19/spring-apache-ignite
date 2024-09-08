package com.sk.configuration;

import com.sk.enums.Caches;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@Slf4j
public class IgniteConfig {

    @Bean("igniteNodeConfiguration")
    public IgniteConfiguration igniteConfiguration() {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        IgniteConfiguration cfg = new IgniteConfiguration();

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
            log.info("Loading cache config for {} ", cache.getCacheName());
            CacheConfiguration cacheCfg = new CacheConfiguration<>(cache.getCacheName());
            cacheCfg.setIndexedTypes(Long.class, cache.getClazz());
            ignite.getOrCreateCache(cacheCfg);
            System.out.println(cacheCfg.getIndexedTypes());
        }
        return ignite;
    }

}
