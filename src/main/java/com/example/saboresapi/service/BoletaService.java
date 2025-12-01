package com.example.saboresapi.service;

import com.example.saboresapi.model.Boleta;
import com.example.saboresapi.model.Usuario;
import com.example.saboresapi.repository.BoletaRepository;
import com.example.saboresapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BoletaService {

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Boleta crearBoleta(Long usuarioId, int total) {
        try {
            // Validar que el usuario existe
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (!usuarioOpt.isPresent()) {
                throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
            }

            Usuario usuario = usuarioOpt.get();

            // Crear la boleta (la fecha se asigna automáticamente por @CreationTimestamp)
            Boleta boleta = Boleta.builder()
                    .usuario(usuario)
                    .total(total)
                    .build();

            return boletaRepository.save(boleta);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la boleta: " + e.getMessage(), e);
        }
    }

    public List<Boleta> listarTodas() {
        try {
            return boletaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las boletas: " + e.getMessage(), e);
        }
    }

    public List<Boleta> listarPorUsuario(Long usuarioId) {
        try {
            return boletaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar boletas por usuario: " + e.getMessage(), e);
        }
    }

    public Optional<Boleta> buscarPorId(Long id) {
        try {
            return boletaRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la boleta: " + e.getMessage(), e);
        }
    }

    public void eliminarBoleta(Long id) {
        try {
            if (!boletaRepository.existsById(id)) {
                throw new RuntimeException("Boleta no encontrada con id: " + id);
            }
            boletaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la boleta: " + e.getMessage(), e);
        }
    }
}
