package com.example.saboresapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                                // Productos - RUTAS CORREGIDAS con /api/v1/
                                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR", "ROLE_MESERO", "ROLE_CLIENTE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasAuthority("ROLE_ADMIN")

                                // Categorias - RUTAS CORREGIDAS con /api/v1/
                                .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR", "ROLE_MESERO", "ROLE_CLIENTE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasAuthority("ROLE_ADMIN")

                                // Usuarios - RUTAS CORREGIDAS con /api/v1/
                                .requestMatchers("/api/v1/usuarios/**").hasAuthority("ROLE_ADMIN")

                                // Boletas - RUTAS CORREGIDAS con /api/v1/
                                .requestMatchers(HttpMethod.GET, "/api/v1/boletas/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR")
                                .requestMatchers(HttpMethod.POST, "/api/v1/boletas/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR", "ROLE_MESERO", "ROLE_CLIENTE")

                                // DetallesBoleta - RUTAS CORREGIDAS con /api/v1/
                                .requestMatchers(HttpMethod.GET, "/api/v1/detalles-boleta/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR", "ROLE_MESERO", "ROLE_CLIENTE")
                                .requestMatchers(HttpMethod.POST, "/api/v1/detalles-boleta/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_VENDEDOR", "ROLE_MESERO", "ROLE_CLIENTE")

                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}