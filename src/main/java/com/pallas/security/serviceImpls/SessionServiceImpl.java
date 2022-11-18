package com.pallas.security.serviceImpls;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pallas.security.entities.Role;
import com.pallas.security.entities.Session;
import com.pallas.security.entities.User;
import com.pallas.security.exceptions.InternalServerErrorException;
import com.pallas.security.exceptions.NotAcceptableException;
import com.pallas.security.models.Response;
import com.pallas.security.models.UserSessionData;
import com.pallas.security.repositories.SessionRepository;
import com.pallas.security.security.UserSession;
import com.pallas.security.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public void save(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public void deletePreviousSessionByUser(long idUser) {
        List<Session> sessions = sessionRepository.findAllByUser_IdAndExpiredIsFalse(idUser);
        for (Session session : sessions) {
            session.setExpired(true);
            session.setExpiredAt(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("s3cr3t".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                Date expiresAt = decodedJWT.getExpiresAt();

                if (new Date().before(expiresAt)) {
                    Optional<Session> session = sessionRepository.findByUser_UsernameAndExpiredIsFalse(username);
                    if (session.isPresent()) {
                        User user = session.get().getUser();

                        Date expiredDate = new Date(System.currentTimeMillis() + (10 * 24 * 60 * 60 * 1000L));
                        String token = JWT.create()
                                .withSubject(user.getUsername())
                                .withExpiresAt(expiredDate)
                                .withIssuer(request.getRequestURL().toString())
                                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                                .sign(algorithm);

                        UserSession.removeDataByUserId(user.getId());
                        UserSession.data.add(new UserSessionData().setUserId(user.getId()).setToken(token));

                        Map<String, String> responseBody = new HashMap<>();
                        responseBody.put("token", token);
                        responseBody.put("refreshToken", refreshToken);

                        response.setStatus(OK.value());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                    } else {
                        Map<String, String> responseBody = new HashMap<>();
                        responseBody.put("error", "Refresh Token not found");
                        response.setStatus(UNAUTHORIZED.value());
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
                    }
                } else {
                    Map<String, String> responseBody = new HashMap<>();
                    responseBody.put("error", "Your refresh token is expired");
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

    @Override
    public Response logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String refreshToken = token.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("s3cr3t".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();

                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<Session> session = sessionRepository.findByUser_UsernameAndExpiredIsFalse(username);

                if (session.isPresent()) {
                    session.get().setLogoutAt(LocalDateTime.now());
                    session.get().setExpired(true);
                    sessionRepository.save(session.get());
                    UserSession.removeDataByUserId(session.get().getUser().getId());
                    return new Response();
                }else{
                    throw new AccessDeniedException("Token data not found");
                }
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        } else {
            throw new NotAcceptableException();
        }
    }

    @Override
    public Optional<Session> selectByUserIdAndExpiredIsFalse(long idUser) {
        return sessionRepository.findByUser_IdAndExpiredIsFalse(idUser);
    }
}
