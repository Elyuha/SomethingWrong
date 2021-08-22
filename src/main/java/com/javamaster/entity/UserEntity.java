package com.javamaster.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_table")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String userpic;

    @Column(unique = true)
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

    @Column
    private String image;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @OneToMany(mappedBy = "UserEntity")
    Set<MessageEntity> messageEntitySet;


    public void setRoleEntity(RoleEntity byId) {
        roleEntity = byId;
    }

    public boolean getActive() {
        return this.active;
    }
}
