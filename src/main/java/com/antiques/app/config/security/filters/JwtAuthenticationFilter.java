package com.antiques.app.config.security.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.antiques.app.config.security.jwt.JwtUtils;
import com.antiques.app.modules.person.persistence.Person;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtils jwtUtils;
    
    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        Person person = null;
        String email = "";
        String password = "";

        try {
            person = new ObjectMapper().readValue(request.getInputStream(), Person.class);

            email = person.getUsername();
            password = person.getPassword();
            System.out.println("Attempting authentication with email: " + email);


        } catch (StreamReadException e) {
            System.out.println("Error en la lectura: " +  e.getCause());
        } catch (DatabindException e) {
            System.out.println("Error en la databind: " +  e.getCause());
        } catch (IOException e) {
            System.out.println("Error en IOException: " +  e.getCause());
        }
    
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
            User user =  (User) authResult.getPrincipal();

            String token = jwtUtils.generateAccesToken(user.getUsername());

            response.addHeader("Authorization", token);

            Map<String, Object> httpResponse = new HashMap<>();
            
            httpResponse.put("token", token);
            httpResponse.put("Message", "Autenticacion correcta");
            httpResponse.put("username: ", user.getUsername());

            response.getWriter().write( new ObjectMapper().writeValueAsString(httpResponse));
            response.setContentType("application/json");
            response.setStatus(200);
            response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
    
}