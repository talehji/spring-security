package com.pallas.security.repositories;

import com.pallas.security.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
