package com.pallas.security.filters;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.security.entities.Session;
import com.pallas.security.models.UserSessionData;
import com.pallas.security.security.UserSession;
import com.pallas.security.services.SessionService;
import com.pallas.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final SessionService sessionService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = ((User) authentication.getPrincipal());
        Algorithm algorithm = Algorithm.HMAC256("s3cr3t".getBytes());
        Date expiredDate = new Date(System.currentTimeMillis() + (10 * 24 * 60 * 60 * 1000L));
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiredDate)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (60 * 24 * 60 * 60 * 1000L)))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Optional<com.pallas.security.entities.User> myUser = userService.getUserByUsername(user.getUsername());
        if (myUser.isPresent()) {
            sessionService.deletePreviousSessionByUser(myUser.get().getId());

            UserSession.removeDataByUserId(myUser.get().getId());

            UserSession.data.add(new UserSessionData().setUserId(myUser.get().getId()).setToken(token));

            sessionService.save(new Session(
                    0,
                    myUser.get(),
                    request.getRemoteAddr(),
                    expiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    LocalDateTime.now(),
                    null,
                    null,
                    false
            ));
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("refreshToken", refreshToken);

            response.setStatus(OK.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", "Authentication Error!");
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }
}
