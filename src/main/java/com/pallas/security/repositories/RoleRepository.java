package com.pallas.security.repositories;

import com.pallas.security.entities.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

public interface RoleRepository extends CrudRepository<Role, Long> {

}
