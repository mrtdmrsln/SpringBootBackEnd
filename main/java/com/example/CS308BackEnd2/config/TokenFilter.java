package com.example.CS308BackEnd2.config;

import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TokenFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
       
        chain.doFilter(request, response);

        removeHttpOnlyFlag(request, response);
    }

    private void removeHttpOnlyFlag(HttpServletRequest request, HttpServletResponse response) {
        String cookieHeader = response.getHeader("Set-Cookie");
        
        if (cookieHeader != null) {
            response.setHeader("Set-Cookie", cookieHeader.replace("; HttpOnly", ""));
           
        }
    }




}
