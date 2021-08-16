package com.javamaster.repository;

import com.javamaster.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long> {
}
