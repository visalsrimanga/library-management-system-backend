package lk.ijse.dep9.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorsFilter extends HttpFilter {

    private String[] origins;
    @Override
    public void init() throws ServletException {
        String origin = getFilterConfig().getInitParameter("origins");
        origins = origin.split(", ");
    }

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        String requestedOrigin = req.getHeader("Origin");
        if (requestedOrigin != null){
            for (String origin : origins) {
                if (requestedOrigin.startsWith(origin.trim())){
                    res.setHeader("Access-Control-Allow-Origin", requestedOrigin);
                    break;
                }
            }
        }

        if (req.getMethod().equalsIgnoreCase("OPTIONS")){
            /* Let's handle pre-flighted requests */
            res.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, HEAD, POST, PATCH, DELETE");

            String requestedMethod = req.getHeader("Access-Control-Request-Method");
            String requestedHeaders = req.getHeader("Access-Control-Request-Headers");

            if ((requestedMethod.equalsIgnoreCase("POST") ||
                    requestedMethod.equalsIgnoreCase("PATCH")) &&
                    requestedHeaders.toLowerCase().contains("content-type")){
                res.setHeader("Access-Control-Allow-Headers", "Content-Type");
            }
        }else{
            if (req.getMethod().equalsIgnoreCase("GET") ||
                    req.getMethod().equalsIgnoreCase("HEAD")){
                res.setHeader("Access-Control-Expose-Headers", "X-Total-Count");
            }
        }
        chain.doFilter(req, res);
    }
}
