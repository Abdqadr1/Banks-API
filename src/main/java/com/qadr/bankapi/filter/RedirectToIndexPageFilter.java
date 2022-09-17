package com.qadr.bankapi.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class RedirectToIndexPageFilter implements Filter {
    List<String> strings = List.of(
            "/bank/", "/country", "/static", "/manifest","/robots.txt",
            "/logo", "/favicon", "/manage", "/site-logo", "/service", "/auth", "/docs"
    );
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

//        System.out.println(requestURI);

        if(strings.stream().anyMatch(requestURI::startsWith)){
//            System.out.println("request uri: "+requestURI);
            chain.doFilter(request, response);
            return;
        }

        // all requests path not in the list above will be redirected and handled by react.
        request.getRequestDispatcher("/").forward(request, response);
    }
}
