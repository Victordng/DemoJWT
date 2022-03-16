package com.security.demoJWT.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.chart.ScatterChart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static netscape.security.Privilege.FORBIDDEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/refreshToken")){
            filterChain.doFilter(request,response);
        }else{
                String authorizationHeader = request.getHeader(AUTHORIZATION);
                if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
                    try {
                        String token = authorizationHeader.substring("Bearer ".length());

                        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = verifier.verify(token);

                        String username = decodedJWT.getSubject();
                        List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("roles").asList(String.class)
                                .stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    }catch(Exception e){
                        log.error("Error logging in: {}",e.getMessage());
                        Map<String , String> errors = new HashMap<>();
                        response.setStatus(FORBIDDEN);
                        response.setContentType(APPLICATION_JSON_VALUE);
                        errors.put("error", e.getMessage());
                        new ObjectMapper().writeValue(response.getOutputStream(),errors);

                    }
                }else{
                    filterChain.doFilter(request,response);

            }
        }
    }
}
