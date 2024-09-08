package com.sk.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class CacheController {

    @Autowired
    @Qualifier("ignite")
    private Ignite ignite;

    @PostMapping(path = "/cache/{cacheName}")
    public ResponseEntity<Boolean> publishToCache(@PathVariable("cacheName") String cacheName) {
        log.info("Updating Cache : {} ", cacheName);
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache(cacheName);
        for (int i = 0; i < 10000; i++) {
            cache.getAndPut(i, "SK" + "_" + i);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @RequestMapping(path = "/cache/{cacheName}/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFromCache(@PathVariable("cacheName") String cacheName, @PathVariable("key") Integer key) {
        log.info("Fetch from cache and key: {}, {}", cacheName, key);
        IgniteCache<Integer, String> cache = ignite.cache(cacheName);
        return new ResponseEntity<>(cache.get(key), HttpStatus.OK);
    }

}
