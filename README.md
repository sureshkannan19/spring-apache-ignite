### Apache Ignite Setup
 <a href="https://ignite.apache.org/download.cgi">Download binary</a> ignite.

### Apache Ignite in Persistent mode:
1. Update your local 'C:\Users\{UserName}\ignite\apache-ignite-2.16.0-bin\config\default-config.xml' file as <a href ="https://github.com/sureshkannan19/spring-apache-ignite/tree/main/src/main/resources/default-config.xml">persistent config file</a>
2. Execute "C:\Users\{UserName}\ignite\apache-ignite-2.16.0-bin\bin\ignite.bat"
3. Checkout <a href ="https://github.com/sureshkannan19/spring-apache-ignite/blob/main/src/main/java/com/sk/configuration/IgniteConfig.java">Ignite config</a>

### Destroy and reload cache on configuration change:
1. In case of adding new column or new index in POJO's, atomicity - reload cache.