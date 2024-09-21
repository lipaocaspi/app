package com.antiques.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.antiques.app.config.security.jwt.JwtUtils;
import com.antiques.app.config.security.filters.JwtAuthenticationFilter;
import com.antiques.app.config.security.filters.JwtAuthorizationFilter;
import com.antiques.app.config.security.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtUtils jwtUtils; 

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    
    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception{
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

         return httpSecurity
            .csrf( csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults())
            .sessionManagement( sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(http -> {
                http.requestMatchers(HttpMethod.POST, "/login").permitAll();/*.hasAnyRole("ADMIN", "USER")*/
                http.requestMatchers(HttpMethod.POST, "/accounts").permitAll();
                http.requestMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN");
                http.requestMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN");
                http.requestMatchers(HttpMethod.PUT, "/**").hasAnyRole("ADMIN");
                http.requestMatchers(HttpMethod.DELETE, "/**").hasAnyRole("ADMIN");
                http.anyRequest().authenticated();
            })
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .build()
            ;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImpl);
        return provider; 
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}