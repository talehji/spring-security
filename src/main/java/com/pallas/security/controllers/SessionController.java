package com.pallas.security.controllers;

import com.pallas.security.models.Response;
import com.pallas.security.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/refresh/token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        sessionService.refreshToken(request, response);
    }

    @GetMapping("/logout")
    public Response logout(@RequestHeader String token) throws IOException {
        return sessionService.logout(token);
    }
}
