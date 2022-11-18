package com.pallas.security.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;
    private String remoteAddress;
    private LocalDateTime expiredDate;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime logoutAt;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean expired;
}
