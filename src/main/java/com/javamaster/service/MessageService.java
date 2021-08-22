package com.javamaster.service;

import com.javamaster.config.SecurityConfig;
import com.javamaster.entity.MessageEntity;
import com.javamaster.repository.MessageEntityRepository;
import com.javamaster.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Autowired
    MessageEntityRepository messageEntityRepository;

    @Autowired
    UserEntityRepository userEntityRepository;

    @Transactional
    public void save(MessageEntity messageEntity){
        messageEntity.setUserEntity(
                userEntityRepository.findByLogin(SecurityConfig.getCurrentUsername())
        );
        messageEntity.setDate(LocalDateTime.now());
        messageEntityRepository.save(messageEntity);
    }

    public List<MessageEntity> getAllMessage(){
        return messageEntityRepository
                .findAll(Sort.by(Sort.Direction.ASC, "date"));
    }
//
//    public List<MessageEntity> getAllMessageById(Long id){
//        return messageEntityRepository.findById(id)
//                .stream().sorted(Comparator.comparing(MessageEntity::getDate))
//                .collect(Collectors.toList());
//    }
}
