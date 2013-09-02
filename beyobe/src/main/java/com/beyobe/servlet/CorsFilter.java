package com.beyobe.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Tried this to permit Chrome & Firefox to debug tagtheday
 * @author donohue
 *
 */
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getHeader("Access-Control-Request-Method") != null && "options".equalsIgnoreCase(request.getMethod())) {
            // CORS "pre-flight" request
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
            response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");
            response.addHeader("Access-Control-Max-Age", "1728000");
//            response.addHeader("Access-Control-Expose-Headers", "X-CUSTOM-HEADER-PING,X-CUSTOM-HEADER-PONG");
//            response.addHeader("Access-Control-Allow-Credentials", "false");
        }
        filterChain.doFilter(request, response);
    }
}