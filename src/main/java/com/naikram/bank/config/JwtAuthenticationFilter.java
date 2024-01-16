package com.naikram.bank.config;

import com.naikram.bank.account.AccountController;
import com.naikram.bank.exceptions.JwtInvalidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // spring bean
@RequiredArgsConstructor //generates a constructor with required fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    @Override
    protected void doFilterInternal( //called for each incoming request
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        } // does not proceed if the request path is authentication
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        } //check if jwt is provided
        jwt = authHeader.substring(7); //extract the token

        LOGGER.info("Before extracting the username");

        userEmail = jwtService.extractUsername(jwt);

        LOGGER.info("After extracting the username");


        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            LOGGER.info("User email not null");
            //context holder check if the client is already authenticated
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                LOGGER.info("First exception");
                throw new JwtInvalidException("Invalid Token");
            }
        }else{
            LOGGER.info("Second exception");
            throw new JwtInvalidException("Invalid Token");
        }
        filterChain.doFilter(request, response);
    }
}
