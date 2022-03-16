package com.security.demoJWT.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("attempt to login with username {} and password {}",username,password);
        UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        if(user != null){
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            String username = user.getUsername();
            List<String> roles = new ArrayList<>();
            user.getAuthorities().stream().forEach(auth->roles.add(auth.getAuthority()));

            String access_token = JWT.create()
                    .withSubject(username)
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles",roles)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 40*60*1000))
                    .sign(algorithm);

            String refresh_token = JWT.create()
                    .withSubject(username)
                    .withIssuer(request.getRequestURL().toString())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60*60*1000))
                    .sign(algorithm);

            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String , String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token",refresh_token);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        }

    }
}
