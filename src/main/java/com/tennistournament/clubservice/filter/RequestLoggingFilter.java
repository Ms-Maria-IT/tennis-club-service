package com.tennistournament.clubservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        
        // Wrap request/response to read body multiple times
        ContentCachingRequestWrapper requestWrapper = 
            new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = 
            new ContentCachingResponseWrapper(response);
        
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log basic request info
            log.info("{} {} -> {} [{}ms]", 
                    request.getMethod(), 
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
            
            // Log request body for debugging (if needed)
            if (log.isDebugEnabled()) {
                String requestBody = getStringValue(
                    requestWrapper.getContentAsByteArray(), 
                    request.getCharacterEncoding()
                );
                log.debug("Request body: {}", requestBody);
            }
            
            // Copy response body back
            responseWrapper.copyBodyToResponse();
        }
    }
    
    private String getStringValue(byte[] content, String charset) {
        if (content == null || content.length == 0) {
            return "";
        }
        try {
            return new String(content, charset != null ? charset : "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(content);
        }
    }
}