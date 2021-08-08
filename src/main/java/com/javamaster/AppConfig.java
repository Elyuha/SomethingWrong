package com.javamaster;

import com.javamaster.config.PasswordGenerator;
import com.javamaster.entity.UserEntity;
import com.javamaster.repository.RoleEntityRepository;
import com.javamaster.service.MailSenderService;
import com.javamaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;
import javax.xml.crypto.Data;

@Configuration
public class AppConfig {

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @Autowired
    private MailSenderService mailSender;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserService userService) {
        return map -> {
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .build();

            String email = (String) map.get("email");
            UserEntity userEntity = userService.findByEmail(email);
            if (userEntity == null) {
                userEntity = new UserEntity();
                String password = passwordGenerator.generate(8);
                userEntity.setLogin((String) map.get("email"));
                userEntity.setPassword(password);
                userEntity.setUserpic((String) map.get("picture"));
                userEntity.setGender((String) map.get("gender"));
                userEntity.setName((String) map.get("name"));
                userEntity.setRoleEntity(roleEntityRepository.findByName("ROLE_USER"));
                userEntity.setEmail((String) map.get("email"));
                userEntity.setLocale((String) map.get("locale"));
                String message = String.format("Hello %s!\n" +
                        "Your password: %s", userEntity.getLogin(), password);
                mailSender.send(userEntity.getEmail(), "Password", message);
                userService.saveUser(userEntity);
            }


            return userEntity;
        };
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(1, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(1, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }

}
