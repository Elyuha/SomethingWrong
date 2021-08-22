package com.javamaster.repository;

import com.javamaster.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagEntityRepository extends JpaRepository<HashtagEntity, Long> {
    HashtagEntity findByName(String name);
}
