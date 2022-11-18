package com.pallas.security.repositories;

import com.pallas.security.entities.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

public interface SessionRepository extends CrudRepository<Session, Long> {
    List<Session> findAllByUser_IdAndExpiredIsFalse(long idUser);
    Optional<Session> findByUser_IdAndExpiredIsFalse(long idUser);
    Optional<Session> findByUser_UsernameAndExpiredIsFalse(String username);
}
