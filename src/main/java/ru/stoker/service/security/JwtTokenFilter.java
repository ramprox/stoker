package ru.stoker.service.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private static final String BEARER_PREFIX = "Bearer ";

    public JwtTokenFilter(JwtService jwtService,
                          UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = getToken(request);
        if(token != null && jwtService.isTokenValid(token)) {
            String login = jwtService.getLoginFromToken(token);
            UserDetails userDetails = getUserDetails(login);
            if(userDetails == null) {
                filterChain.doFilter(request, response);
                return;
            }
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null) {
            return null;
        }
        if(authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private UserDetails getUserDetails(String login) {
        try {
            return userDetailsService.loadUserByUsername(login);
        } catch (UsernameNotFoundException ex) {
            return null;
        }
    }

}
