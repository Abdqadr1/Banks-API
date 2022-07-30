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
            "/logo", "/favicon", "/manage", "/site-logo", "/service"
    );
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        if(strings.stream().anyMatch(requestURI::startsWith)){
//            System.out.println("request uri: "+requestURI);
            chain.doFilter(request, response);
            return;
        }

        // all requests not api or static will be forwarded to index page.
        request.getRequestDispatcher("/").forward(request, response);
    }
}
