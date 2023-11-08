package com.sk.configuration;

import com.sk.constants.FilterConstants;
import com.sk.filter.JsonWebTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig {
    @Value("$(signInKey)")
    private String signInKey;

    @Bean
    public FilterRegistrationBean<JsonWebTokenFilter> registerFilters() {
        FilterRegistrationBean<JsonWebTokenFilter> fr = new FilterRegistrationBean<>();
        fr.setFilter(new JsonWebTokenFilter());
        fr.setOrder(1);
        Map<String, String> params = new HashMap<>();
        params.put(FilterConstants.SIGN_IN_TOKEN, signInKey);
        fr.setInitParameters(params);
        return fr;
    }

}
