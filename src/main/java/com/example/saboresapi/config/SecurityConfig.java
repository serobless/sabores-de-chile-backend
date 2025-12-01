package com.example.saboresapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. DESACTIVAR CSRF (Obligatorio para que funcione el POST en Postman)
            .csrf(csrf -> csrf.disable())
            
            // 2. CONFIGURAR PERMISOS DE RUTAS
            .authorizeHttpRequests(authRequest ->
                authRequest
                    // PERMITIR TODO EL ACCESO A AUTH Y DOCUMENTACIÓN
                    .requestMatchers("/api/v1/auth/**").permitAll()  // <--- ¡AQUÍ ESTÁ LA CLAVE!
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    
                    // CUALQUIER OTRA COSA REQUIERE AUTENTICACIÓN
                    .anyRequest().authenticated()
            )
            
            // 3. STATELESS (No guardar sesión en servidor, usar Tokens)
            .sessionManagement(sessionManager ->
                sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}