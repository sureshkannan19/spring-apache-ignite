package com.sk.configuration;

import com.sk.filter.JsonWebTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final JsonWebTokenFilter jsonWebTokenFilter;

    @Bean
    public FilterRegistrationBean<JsonWebTokenFilter> registerFilters() {
        FilterRegistrationBean<JsonWebTokenFilter> fr = new FilterRegistrationBean<>();
        fr.setFilter(jsonWebTokenFilter);
        fr.setOrder(1);
        return fr;
    }

}
