package com.javamaster.controller;

import com.javamaster.config.jwt.JwtProvider;
import com.javamaster.controller.request.AuthRequest;
import com.javamaster.controller.request.RegistrationRequest;
import com.javamaster.controller.response.AuthResponse;
import com.javamaster.entity.UserEntity;
import com.javamaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        if(userService.findByLogin(registrationRequest.getLogin()) != null)
            return new ResponseEntity<>("This login is already used!", HttpStatus.CONFLICT);
        if(userService.findByEmail(registrationRequest.getEmail()) != null){
            return new ResponseEntity<>("This email is already used!", HttpStatus.CONFLICT);
        }
        UserEntity u = new UserEntity();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        u.setEmail(registrationRequest.getEmail());
        userService.saveUser(u);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if(userEntity == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(userEntity.getActive())
            return new ResponseEntity<>(HttpStatus.LOCKED);
        userEntity.setLastVisit(LocalDateTime.now());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @RequestMapping(value = "/activate/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> activateUser(@PathVariable String code){
        boolean isActive = userService.activateUser(code);
        if(isActive == true)
            return new ResponseEntity<>("User successfully activated", HttpStatus.OK);
        else
            return new ResponseEntity<>("Activation code is not found", HttpStatus.NOT_FOUND);
    }
}
