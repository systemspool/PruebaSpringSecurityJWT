package com.example.security;

import com.example.security.filters.JwtAuthenticationFilter;
import com.example.security.filters.JwtAuthorizationFilter;
import com.example.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)  //con esto habilitamos las anotaciones para nuestr springsecurity para nuestros controladores
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtAuthorizationFilter authorizationFilter;



    @Bean
        //metodo para configurar la seguridad
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        DefaultSecurityFilterChain defaultSecurityFilterChain = httpSecurity
                .csrf(config -> config.disable())   //se inhabilita ya que no se usaaran formularios
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello").permitAll();   //aqui se configura acceso a las url sin contraseña
                    auth.anyRequest().authenticated();   //aqui se configura que todas las demas se deben logear
                })
                .sessionManagement(session -> {   //aqui se manejan las sesiones
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //se configura las politicas de sesion
                }).addFilter(jwtAuthenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        return defaultSecurityFilterChain;
        
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();  //encriptacion
    }

//    @Bean
//    UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("Jesus").password("123456").roles().build());
//        return manager;
//    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManager authM = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
        return authM;
    }

    //para encritar una contraseña que no lo este en la bd
//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }
}
