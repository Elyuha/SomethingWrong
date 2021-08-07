package com.javamaster.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "user_table")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String userpic;

    @Column
    private String email;

    @Column
    private String gender;

    @Column
    private String locale;

    @Column
    private LocalDateTime lastVisit;

    @Column
    private boolean active;

    @Column
    private String activationCode;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    public void setRoleEntity(RoleEntity byId) {
        roleEntity = byId;
    }
}
