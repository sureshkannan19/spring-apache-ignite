package com.sk.filter;

import com.sk.constants.FilterConstants;
import com.sk.exception.UserException;
import com.sk.model.UsersModel;
import com.sk.service.JWTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonWebTokenFilter implements Filter {
    @Autowired

    private final JWTokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        log.info(" Full api {} ", httpServletRequest.getRequestURI());
        log.info(" Executing api {} ", httpServletRequest.getContextPath());
        if (httpServletRequest.getRequestURI().contains("protected")) {
            String token = httpServletRequest.getHeader(FilterConstants.SIGNED_IN_USER);
            if (token == null) {
                throw new UserException("Signing token is not present");
            }
            UsersModel user = tokenService.getUserDetailsFromToken(token);
            log.info("Log in {} ", token);
            log.info("Logged in user {} ", user);
        }
        chain.doFilter(request, response);
    }
}
