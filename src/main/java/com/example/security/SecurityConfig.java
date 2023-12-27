package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
        //metodo para configurar la seguridad
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        DefaultSecurityFilterChain defaultSecurityFilterChain = httpSecurity
                .csrf(config -> config.disable())   //se inhabilita ya que no se usaaran formularios
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello").permitAll();   //aqui se configura acceso a las url sin contraseña
                    auth.anyRequest().authenticated();   //aqui se configura que todas las demas se deben logear
                })
                .sessionManagement(session -> {   //aqui se manejan las sesiones
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //se configura las politicas de sesion
                })
                .httpBasic()
                .and()
                .build();
        return defaultSecurityFilterChain;
        
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();  //encriptacion
    }

    @Bean
    UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("Jesus").password("123456").roles().build());
        return manager;
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManager authM = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
        return authM;
    }
}
