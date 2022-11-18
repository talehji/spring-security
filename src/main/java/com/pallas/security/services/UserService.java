package com.pallas.security.services;

import com.pallas.security.entities.User;
import com.pallas.security.models.Response;

import java.util.Optional;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

public interface UserService {

    Response add(User user);
    Optional<User> getUserByUsername(String username);
}
