package com.javamaster.service;

import com.javamaster.entity.RoleEntity;
import com.javamaster.entity.UserEntity;
import com.javamaster.repository.RoleEntityRepository;
import com.javamaster.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleEntityRepository roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    MailSenderService mailSender;

    @Transactional
    public void saveUser(UserEntity userEntity) {
        userEntity.setActive(true);
        userEntity.setActivationCode(UUID.randomUUID().toString());
        RoleEntity userRole = roleEntityRepository.findByName("ROLE_USER");
        userEntity.setRoleEntity(userRole);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if (!StringUtils.isEmpty(userEntity.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "For activated password, please, visit next link: http://localhost:9002/activate/%s",
                    userEntity.getLogin(),
                    userEntity.getActivationCode()
            );

            mailSender.send(userEntity.getEmail(), "Activation code", message);
            userEntityRepository.save(userEntity);
        }
    }

    @Transactional
    public void updateUser(UserEntity userEntity) {
        userEntityRepository.save(userEntity);
    }

    public UserEntity findById(Long id){
        Optional<UserEntity> byId = userEntityRepository.findById(id);
        try {
            return byId.get();
        }
        catch (NoSuchElementException e){
            return new UserEntity();
        }
    }

    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    public UserEntity findByEmail(String email){
        return userEntityRepository.findByEmail(email);
    }

    public UserEntity findByLoginAndPassword(String login, String password) {
        UserEntity userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        userEntity = findByEmail(login);
        if(userEntity != null){
            if (passwordEncoder.matches(password, userEntity.getPassword())){
                return userEntity;
            }
        }
        return null;
    }

    public boolean activateUser(String code) {
        UserEntity user = userEntityRepository.findByActivationCode(code);

        if(user == null){
            return false;
        }

        user.setActive(false);
        user.setActivationCode(null);
        userEntityRepository.save(user);

        return true;
    }
}
