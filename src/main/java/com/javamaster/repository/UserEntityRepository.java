package com.javamaster.repository;

import com.javamaster.entity.MessageEntity;
import com.javamaster.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByLogin(String login);

    UserEntity findByEmail(String email);

    UserEntity findByActivationCode(String code);

    @Query("select a from MessageEntity a where a.userEntity.id = :id")
    List<MessageEntity> findAllMessageById(Long id);
}
