package com.edmitryv.ml_market_server.authentication.services;


import com.edmitryv.ml_market_server.authentication.repos.TokenRepository;
import com.edmitryv.ml_market_server.core.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final TokenRepository tokenRepository;

//  @Override
//  protected void doFilterInternal(
//      @NonNull HttpServletRequest request,
//      @NonNull HttpServletResponse response,
//      @NonNull FilterChain filterChain
//  ) throws ServletException, IOException {
////    if (request.getServletPath().contains("/authentication")) {
//////      filterChain.doFilter(request, response);
//////      return;
//////    }
//    var authHeader = request.getHeader(HEADER_NAME);
//    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
//      filterChain.doFilter(request, response);
//      return;
//    }
//    final String jwt;
//    final String userEmail;
//    jwt = authHeader.substring(7);
//    userEmail = jwtService.extractUsername(jwt);
//    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//      UserDetails userDetails = this.userService.userDetailsService().loadUserByUsername(userEmail);
//      var isTokenValid = tokenRepository.findByToken(jwt)
//          .map(t -> !t.isExpired() && !t.isRevoked())
//          .orElse(false);
//      if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//            userDetails,
//            null,
//            userDetails.getAuthorities()
//        );
//        authToken.setDetails(
//            new WebAuthenticationDetailsSource().buildDetails(request)
//        );
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//      }
//    }
//    filterChain.doFilter(request, response);
//  }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        var jwt = authHeader.substring(BEARER_PREFIX.length());
        var username = jwtService.extractUsername(jwt);

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

}
