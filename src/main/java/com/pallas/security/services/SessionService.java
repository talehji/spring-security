package com.pallas.security.services;

import com.pallas.security.entities.Session;
import com.pallas.security.models.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

public interface SessionService {

    void save(Session session);
    void deletePreviousSessionByUser(long idUser);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    Response logout(String token) throws IOException;
    Optional<Session> selectByUserIdAndExpiredIsFalse(long idUser);
}
