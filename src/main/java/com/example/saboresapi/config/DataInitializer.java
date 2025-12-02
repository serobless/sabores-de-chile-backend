package com.example.saboresapi.config;

import com.example.saboresapi.model.Role;
import com.example.saboresapi.model.Usuario;
import com.example.saboresapi.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo crear usuarios si no existen para evitar duplicados en reinicios
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin User")
                    .email("admin@sabores.com")
                    .password(passwordEncoder.encode("password123"))
                    .rol(Role.ADMIN)
                    .build();

            Usuario vendedor = Usuario.builder()
                    .nombre("Vendedor User")
                    .email("vendedor@sabores.com")
                    .password(passwordEncoder.encode("password123"))
                    .rol(Role.VENDEDOR)
                    .build();

            Usuario cliente = Usuario.builder()
                    .nombre("Cliente User")
                    .email("cliente@sabores.com")
                    .password(passwordEncoder.encode("password123"))
                    .rol(Role.CLIENTE)
                    .build();
            
            Usuario mesero = Usuario.builder()
                    .nombre("Mesero User")
                    .email("mesero@sabores.com")
                    .password(passwordEncoder.encode("password123"))
                    .rol(Role.MESERO)
                    .build();

            usuarioRepository.saveAll(List.of(admin, vendedor, cliente, mesero));
            System.out.println("Usuarios de prueba creados correctamente.");
        }
    }
}
