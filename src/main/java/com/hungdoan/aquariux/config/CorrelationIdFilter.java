package com.hungdoan.aquariux.config;

import com.hungdoan.aquariux.common.id_generator.IdGenerator;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorrelationIdFilter implements Filter {

    public static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-ID";
    
    public static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    private final IdGenerator idGenerator;

    @Autowired
    public CorrelationIdFilter(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER_NAME);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = idGenerator.getId();
        }

        httpResponse.setHeader(CORRELATION_ID_HEADER_NAME, correlationId);
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
        }
    }
}

