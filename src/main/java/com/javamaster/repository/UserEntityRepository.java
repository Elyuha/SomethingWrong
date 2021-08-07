package com.javamaster.repository;

import com.javamaster.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);

    UserEntity findByEmail(String email);

    UserEntity findByActivationCode(String code);
}
