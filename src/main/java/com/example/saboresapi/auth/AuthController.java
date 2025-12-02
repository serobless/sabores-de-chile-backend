package com.example.saboresapi.auth;

import com.example.saboresapi.config.JwtService;
import com.example.saboresapi.dto.UsuarioRequestDTO;
import com.example.saboresapi.dto.UsuarioResponseDTO;
import com.example.saboresapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para autenticación y registro de usuarios")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Una vez autenticado, cargar los detalles reales del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Generar el token con los detalles correctos (incluyendo el rol)
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario (rol CLIENTE automático)")
    public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}
