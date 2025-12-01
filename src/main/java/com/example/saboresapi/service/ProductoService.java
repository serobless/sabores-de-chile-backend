package com.example.saboresapi.service;

import com.example.saboresapi.model.Categoria;
import com.example.saboresapi.model.Producto;
import com.example.saboresapi.repository.CategoriaRepository;
import com.example.saboresapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FileStorageService fileStorageService;

    // Inyección de dependencias por constructor
    public ProductoService(ProductoRepository productoRepository,
                          CategoriaRepository categoriaRepository,
                          FileStorageService fileStorageService) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.fileStorageService = fileStorageService;
    }

    // Crear producto
    public Producto crearProducto(String nombre, int precio, int stock, Long categoriaId, MultipartFile imagen) {
        // Validar que la categoría existe
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoriaId));

        // Guardar imagen si se proporciona
        String nombreImagen = null;
        if (imagen != null && !imagen.isEmpty()) {
            nombreImagen = fileStorageService.storeFile(imagen);
        }

        // Crear y guardar producto
        Producto producto = Producto.builder()
                .nombre(nombre)
                .precio(precio)
                .stock(stock)
                .imagen(nombreImagen)
                .categoria(categoria)
                .build();

        return productoRepository.save(producto);
    }

    // Listar todos los productos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Listar productos por categoría
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    // Buscar producto por ID
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    // Actualizar producto
    public Producto actualizarProducto(Long id, String nombre, int precio, int stock, Long categoriaId, MultipartFile imagen) {
        // Buscar producto existente
        Producto productoExistente = buscarPorId(id);

        // Validar que la categoría existe
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoriaId));

        // Actualizar campos básicos
        productoExistente.setNombre(nombre);
        productoExistente.setPrecio(precio);
        productoExistente.setStock(stock);
        productoExistente.setCategoria(categoria);

        // Actualizar imagen si se proporciona una nueva
        if (imagen != null && !imagen.isEmpty()) {
            // Eliminar imagen anterior si existe
            if (productoExistente.getImagen() != null) {
                fileStorageService.deleteFile(productoExistente.getImagen());
            }
            // Guardar nueva imagen
            String nombreImagen = fileStorageService.storeFile(imagen);
            productoExistente.setImagen(nombreImagen);
        }

        return productoRepository.save(productoExistente);
    }

    // Eliminar producto
    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);

        // Eliminar imagen si existe
        if (producto.getImagen() != null) {
            fileStorageService.deleteFile(producto.getImagen());
        }

        productoRepository.delete(producto);
    }
}
