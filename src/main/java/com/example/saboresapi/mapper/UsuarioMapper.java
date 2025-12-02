package com.example.saboresapi.mapper;

import com.example.saboresapi.dto.UsuarioRequestDTO;
import com.example.saboresapi.dto.UsuarioResponseDTO;
import com.example.saboresapi.dto.UsuarioUpdateDTO;
import com.example.saboresapi.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .build();
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    public void updateEntityFromDTO(UsuarioUpdateDTO dto, Usuario usuario) {
        if (dto.getNombre() != null) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        }
        // El password se maneja por separado en el service
    }
}
