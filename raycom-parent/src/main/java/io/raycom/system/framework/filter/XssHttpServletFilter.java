package io.raycom.system.framework.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import io.raycom.system.framework.filter.wrapper.XssHttpServletRequestWrapper; 

public class XssHttpServletFilter  extends OncePerRequestFilter {  
  
    private static final Logger LOGGER = LoggerFactory.getLogger(XssHttpServletFilter.class);  
  
    @Override  
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  
            throws ServletException, IOException {  
        try {  
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);  
            chain.doFilter(xssRequest, response);  
        } catch (Exception e) {  
            LOGGER.error("Xss过滤器，包装request对象失败");  
            chain.doFilter(request, response);  
        }                 
    }  
      
}  