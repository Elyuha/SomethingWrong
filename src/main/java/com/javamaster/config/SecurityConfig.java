package com.javamaster.config;

import com.javamaster.config.jwt.JwtFilter;
import com.javamaster.entity.UserEntity;
import com.javamaster.repository.RoleEntityRepository;
import com.javamaster.repository.UserEntityRepository;
import com.javamaster.service.MailSenderService;
import com.javamaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @Autowired
    private MailSenderService mailSender;


    public static String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("USER")
                .antMatchers("/register", "/auth", "/activate/*").permitAll()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PrincipalExtractor principalExtractor(UserService userService){
        return map -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .build();

            String email = (String) map.get("email");
            UserEntity userEntity = userService.findByEmail(email);
            if (userEntity == null){
                String password = passwordGenerator.generate(8);
                userEntity.setLogin((String) map.get("email"));
                userEntity.setPassword(passwordEncoder.encode(password) );
                userEntity.setUserpic((String) map.get("picture"));
                userEntity.setGender((String) map.get("gender"));
                userEntity.setName((String) map.get("name"));
                userEntity.setRoleEntity(roleEntityRepository.findByName("ROLE_USER"));
                userEntity.setEmail((String) map.get("email"));
                userEntity.setLocale((String) map.get("locale"));
                String message = String.format("Hello %s!"+
                        "Your password: %s", userEntity.getLogin(), password);
                mailSender.send(userEntity.getEmail(), "Password", message);
                userService.saveUser(userEntity);
            }



            return userEntity;
        };
    }

}
