package com.sk.filter;

import com.sk.constants.FilterConstants;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class JsonWebTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = ((HttpServletRequest) request).getHeader(FilterConstants.SIGN_IN_TOKEN);
        log.info("Log in {} ", token);
        chain.doFilter(request, response);
    }
}
