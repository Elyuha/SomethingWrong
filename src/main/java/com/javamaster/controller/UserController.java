package com.javamaster.controller;

import com.javamaster.entity.UserEntity;
import com.javamaster.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/user/setnewpassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePassword(UserEntity userEntity){
        try {
            if (userEntity == null)
                return new ResponseEntity<>("User is empty", HttpStatus.BAD_REQUEST);

            if (userService.findByEmail(userEntity.getEmail())== null)
                return new ResponseEntity<>("User ot found", HttpStatus.BAD_REQUEST);

            if(userService.findByEmail(userEntity.getEmail()).getPassword().equals(userEntity.getPassword()))
                return new ResponseEntity<>("The old password must not be equal new password", HttpStatus.CONFLICT);

            userService.updateUser(userEntity);

            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        catch (Exception e){
            log.info(e.getStackTrace().toString());
            return new ResponseEntity<String>("Something wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateName(UserEntity userEntity){
        try {
            if (userEntity == null)
                return new ResponseEntity<>("User is empty", HttpStatus.BAD_REQUEST);

            if (userService.findByEmail(userEntity.getEmail()) == null)
                return new ResponseEntity<>("User ot found", HttpStatus.BAD_REQUEST);

            userService.updateUser(userEntity);

            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        catch (Exception e){
            log.info(e.getStackTrace().toString());
            return new ResponseEntity<String>("Something wrong", HttpStatus.BAD_REQUEST);
        }
    }



}
