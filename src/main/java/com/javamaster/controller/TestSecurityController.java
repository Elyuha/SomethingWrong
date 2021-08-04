package com.javamaster.controller;

import com.javamaster.config.SecurityConfig;
import com.javamaster.entity.UserEntity;
import com.javamaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecurityController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/admin/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAdmin() {
        return "Hi admin";
    }

    @RequestMapping(value = "/user/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUser() {
        return "Hi user";
    }

    @RequestMapping(value = "/user/getcurrent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserEntity getCurrentUser(){
        return userService.findByLogin(SecurityConfig.getCurrentUsername());
    }
}
