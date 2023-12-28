package com.example;

import com.example.models.Erole;
import com.example.models.RoleEntity;
import com.example.models.UserEntity;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Bean
    CommandLineRunner init(){
        return args -> {
            UserEntity userEntity = UserEntity.builder()
                    .email("jesus@gmail.com")
                    .username("jesus")
                    .password(passwordEncoder.encode("123456"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(Erole.valueOf(Erole.ADMIN.name()))
                            .build())).build();

            UserEntity userEntity2 = UserEntity.builder()
                    .email("jose@gmail.com")
                    .username("jose")
                    .password(passwordEncoder.encode("789456"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(Erole.valueOf(Erole.USER.name()))
                            .build())).build();

            UserEntity userEntity3 = UserEntity.builder()
                    .email("gerardo@gmail.com")
                    .username("gerardo")
                    .password(passwordEncoder.encode("123456"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(Erole.valueOf(Erole.INVITED.name()))
                            .build())).build();

            userRepository.save(userEntity);
            userRepository.save(userEntity2);
            userRepository.save(userEntity3);
        };
    }
}
