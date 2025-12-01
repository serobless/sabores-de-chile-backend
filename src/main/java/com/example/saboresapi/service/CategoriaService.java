package com.example.saboresapi.service;

import com.example.saboresapi.model.Categoria;
import com.example.saboresapi.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Inyección de dependencias por constructor (mejor práctica)
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Crear categoría
    public Categoria crearCategoria(Categoria categoria) {
        // Verificar que el nombre no exista
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("La categoría con ese nombre ya existe");
        }
        return categoriaRepository.save(categoria);
    }

    // Listar todas las categorías
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Buscar categoría por ID
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    // Buscar categoría por nombre
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    // Actualizar categoría
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = buscarPorId(id);

        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());

        return categoriaRepository.save(categoriaExistente);
    }

    // Eliminar categoría
    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}
