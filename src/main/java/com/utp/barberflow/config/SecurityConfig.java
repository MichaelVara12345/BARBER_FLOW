package com.utp.barberflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desactiva protección CSRF para APIs REST
            .cors(cors -> cors.configure(http)) // Habilita los Cors que configuramos en los controllers
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // Permite todo el tráfico por ahora
        return http.build();
    }
}