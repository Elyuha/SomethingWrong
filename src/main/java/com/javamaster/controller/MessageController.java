package com.javamaster.controller;

import com.javamaster.controller.request.MessageRequest;
import com.javamaster.entity.MessageEntity;
import com.javamaster.entity.UserEntity;
import com.javamaster.service.HashtagService;
import com.javamaster.service.MessageService;
import com.javamaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    HashtagService hashtagService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/save",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(MessageRequest message){
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMess(message.getMessage());
        messageEntity.setHashtagEntitySet(
                hashtagService.getSetHashtagEntity(
                        Arrays.stream(message.getHashTags().split(" "))
                        .collect(Collectors.toSet())));
        messageService.save(messageEntity);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/message",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageEntity>> getAllMessage(){
        List<MessageEntity> list = messageService.getAllMessage();
        if(list == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/message/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageEntity>> getAllMessageById(@PathVariable("id") Long id){
        UserEntity userEntity = userService.findById(id);
        if(userEntity == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<MessageEntity> list = userEntity.getMessageEntitySet()
                .stream().collect(Collectors.toList());
        if(list == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
