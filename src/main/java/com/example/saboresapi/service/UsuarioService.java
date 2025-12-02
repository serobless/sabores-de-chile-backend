package com.example.saboresapi.service;

import com.example.saboresapi.dto.UsuarioRequestDTO;
import com.example.saboresapi.dto.UsuarioResponseDTO;
import com.example.saboresapi.dto.UsuarioUpdateDTO;
import com.example.saboresapi.exception.DuplicateResourceException;
import com.example.saboresapi.exception.ResourceNotFoundException;
import com.example.saboresapi.mapper.UsuarioMapper;
import com.example.saboresapi.model.Role;
import com.example.saboresapi.model.Usuario;
import com.example.saboresapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    // Crear usuario (registro público)
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioDTO) {
        // Verificar que el email no exista
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", usuarioDTO.getEmail());
        }

        // Convertir DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        // Forzar el rol a "CLIENTE" para cualquier nuevo registro desde el endpoint público
        usuario.setRol(Role.CLIENTE);

        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    // Crear usuario como ADMIN (con rol específico)
    public UsuarioResponseDTO crearUsuarioAdmin(UsuarioRequestDTO usuarioDTO) {
        // Verificar que el email no exista
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", usuarioDTO.getEmail());
        }

        // Convertir DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        // Validar que el rol no sea null
        if (usuario.getRol() == null) {
            usuario.setRol(Role.CLIENTE);
        }

        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    // Listar todos los usuarios
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuario por ID
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    // Buscar usuario por email (para uso interno)
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Actualizar usuario
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // Verificar email duplicado si se está cambiando
        if (usuarioUpdateDTO.getEmail() != null &&
            !usuarioUpdateDTO.getEmail().equals(usuarioExistente.getEmail()) &&
            usuarioRepository.existsByEmail(usuarioUpdateDTO.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", usuarioUpdateDTO.getEmail());
        }

        // Actualizar campos desde DTO
        usuarioMapper.updateEntityFromDTO(usuarioUpdateDTO, usuarioExistente);

        // Solo actualizar password si se proporciona uno nuevo
        if (usuarioUpdateDTO.getPassword() != null && !usuarioUpdateDTO.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioUpdateDTO.getPassword()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    // Eliminar usuario
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        usuarioRepository.delete(usuario);
    }
}
