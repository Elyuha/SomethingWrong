package com.javamaster.config;

import com.javamaster.config.jwt.JwtFilter;
import com.javamaster.entity.UserEntity;
import com.javamaster.repository.RoleEntityRepository;
import com.javamaster.repository.UserEntityRepository;
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

    @Qualifier("oauth2ClientContext")
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private RoleEntityRepository roleEntityRepository;


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
                .antMatchers("/register", "/auth").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public FilterRegistrationBean oAuth2FilterRegistration(OAuth2ClientContextFilter contextFilter){
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(contextFilter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//    @Bean
//    private Filter ssoFilter(){
//        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/google");
//        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
//
//
//    }

    @Bean
    public PrincipalExtractor principalExtractor(UserService userService){
        return map -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String sub = (String) map.get("sub");
            Long id = Long.parseLong(sub.substring(10));
            UserEntity userEntity = userService.findById(id);
            if (userEntity.getEmail() == null){
                userEntity.setLogin((String) map.get("email"));
                userEntity.setPassword(passwordEncoder.encode((String) map.get("email")) );
                userEntity.setUserpic((String) map.get("picture"));
                userEntity.setGender((String) map.get("gender"));
                userEntity.setName((String) map.get("name"));
                userEntity.setRoleEntity(roleEntityRepository.findByName("ROLE_USER"));
                userEntity.setEmail((String) map.get("email"));
                userEntity.setLocale((String) map.get("locale"));
            }

            userService.saveUser(userEntity);

            return userEntity;
        };
    }

}
