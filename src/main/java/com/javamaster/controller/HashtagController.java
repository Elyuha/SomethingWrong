package com.javamaster.controller;

import com.javamaster.entity.MessageEntity;
import com.javamaster.service.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HashtagController {
    @Autowired
    HashtagService hashtagService;

    @RequestMapping(value = "/get/{hashtag}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageEntity>> getMessageByHashtag(@PathVariable("hashtag") String hashtag){
        if (hashtagService.findByName(hashtag) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<MessageEntity> messageByHashtagName = hashtagService.getMessageByHashtagName(hashtag);
        if(messageByHashtagName.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(messageByHashtagName, HttpStatus.OK);
    }
}
