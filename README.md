### Apache Ignite Setup
 <a href="https://ignite.apache.org/download.cgi">Download binary</a> ignite.

### Apache Ignite in Persistent mode:
1. Update your local 'C:\Users\{UserName}\ignite\apache-ignite-2.16.0-bin\config\default-config.xml' file as <a href ="https://github.com/sureshkannan19/spring-apache-ignite/tree/main/src/main/resources/default-config.xml">persistent config file</a>
2. Execute "C:\Users\{UserName}\ignite\apache-ignite-2.16.0-bin\bin\ignite.bat"
3. Checkout <a href ="https://github.com/sureshkannan19/spring-apache-ignite/blob/main/src/main/java/com/sk/configuration/IgniteConfig.java">Ignite config</a>

### Ignite Cluster state:
```
For READ - WRITE mode: ignite.cluster().state(ClusterState.ACTIVE);
For READ mode: ignite.cluster().state(ClusterState.ACTIVE_READ_ONLY);
```

### Destroy and reload cache on configuration change:
In case of adding new column or new index in POJO's, changing atomicity - reload cache.
```
ignite.destroyCache(CACHE_NAME);
```
### Ignite as Search:
#### For Full-Text Search (FTS): 
    Dependency: implementation "org.apache.ignite:ignite-core:${igniteVersion}" - uses Lucene Engine
    Mark DTO property with
        1. @QueryTextField - to enable full text search
#### For Partial-Text Search:
    Dependency: implementation "org.apache.ignite:ignite-indexing:${igniteVersion}" - uses H2 SQL Engine and Calcite as beta version is available
    Mark DTO property with 
        1. @QuerySqlField - to enable sql search on the field 
        2. @QuerySqlField(index = true) - to enable sql search on the field with index for better performance
#### For Locking mechanism:
     Enable atomictyMode as CacheAtomicityMode.TRANSACTIONAL, to enable locking mechanism.  
        clientCacheConfig.setAtomicityMode(cache.getAtomicityMode());


