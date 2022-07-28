package com.qadr.bankapi.filter;

import org.springframework.boot.actuate.trace.http.HttpExchangeTracer;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Component
public class CustomHttpTraceFilter extends HttpTraceFilter {
    public CustomHttpTraceFilter(HttpTraceRepository repository, HttpExchangeTracer tracer) {
        super(repository, tracer);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/manage") ||
                request.getServletPath().contains("/authenticate")||
                request.getServletPath().contains("/bank/id")||
                request.getServletPath().contains("/bank/admin")||
                request.getServletPath().contains("/country/admin")||
                request.getServletPath().contains("/static/")||
                request.getServletPath().contains("/favicon.ico")||
                request.getServletPath().contains("/logo")||
                request.getServletPath().contains("/login");
    }
}
