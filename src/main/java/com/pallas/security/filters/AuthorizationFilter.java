package com.pallas.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.security.entities.Session;
import com.pallas.security.models.UserSessionData;
import com.pallas.security.security.UserSession;
import com.pallas.security.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/auth/login")
            || request.getServletPath().equals("/api/v1/auth/register")
            || request.getServletPath().equals("/api/v1/session/refresh/token")
            || request.getServletPath().startsWith("/api/v1/media/download")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("s3cr3t".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);

                    String username = decodedJWT.getSubject();
                    Date expiresAt = decodedJWT.getExpiresAt();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    if (new Date().before(expiresAt)) {
                        if (UserSession.getDataByToken(token) != null) {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            filterChain.doFilter(request, response);
                        } else {
                            Map<String, String> responseBody = new HashMap<>();
                            responseBody.put("error", "Token not found");
                            response.setStatus(UNAUTHORIZED.value());
                            response.setContentType(APPLICATION_JSON_VALUE);
                            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                        }
                    } else {
                        UserSessionData sessionData = UserSession.getDataByToken(token);
                        if (sessionData != null) {
                            Optional<Session> session = sessionService.selectByUserIdAndExpiredIsFalse(sessionData.getUserId());
                            if (session.isPresent()) {
                                session.get().setExpiredAt(LocalDateTime.now());
                                session.get().setExpired(true);
                                sessionService.save(session.get());
                            }
                        }
                        Map<String, String> responseBody = new HashMap<>();
                        responseBody.put("error", "Your token is expired");
                        response.setStatus(UNAUTHORIZED.value());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("error", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.setStatus(INTERNAL_SERVER_ERROR.value());
                    new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                }
            } else {
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("error", "Invalid authentication token");
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
            }
        }
    }
}
