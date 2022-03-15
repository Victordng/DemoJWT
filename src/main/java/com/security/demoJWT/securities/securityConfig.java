package com.security.demoJWT.securities;

import com.security.demoJWT.filters.CustomAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter=new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login","/api/refresToken").permitAll();
        http.addFilter(customAuthenticationFilter);
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/users/**","/api/courses/**","/api/courses/**").hasAnyAuthority("STUDENT","PROFESSOR");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/users/**","/api/courses/**","/api/courses/**").hasAnyAuthority("PROFESSOR");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/users/**","/api/courses/**","/api/courses/**").hasAnyAuthority("PROFESSOR");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/users/**","/api/courses/**","/api/courses/**").hasAnyAuthority("PROFESSOR");
      //  http.addFilterBefore()

    }
}
