package com.pallas.security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String name;
    private String surname;
    private String username;
    @JsonIgnore
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean deleted;
    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles;
}
