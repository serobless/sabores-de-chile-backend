package com.example.saboresapi.controller;

import com.example.saboresapi.model.Producto;
import com.example.saboresapi.service.FileStorageService;
import com.example.saboresapi.service.ProductoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "*") // Permitir peticiones desde React
public class ProductoController {

    private final ProductoService productoService;
    private final FileStorageService fileStorageService;

    public ProductoController(ProductoService productoService, FileStorageService fileStorageService) {
        this.productoService = productoService;
        this.fileStorageService = fileStorageService;
    }

    // POST - Crear producto
    // http://localhost:8080/api/v1/productos
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") int precio,
            @RequestParam("stock") int stock,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Producto nuevoProducto = productoService.crearProducto(nombre, precio, stock, categoriaId, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET - Listar todos los productos
    // http://localhost:8080/api/v1/productos
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    // GET - Buscar producto por ID
    // http://localhost:8080/api/v1/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.buscarPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - Listar productos por categoría
    // http://localhost:8080/api/v1/productos/categoria/1
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    // PUT - Actualizar producto
    // http://localhost:8080/api/v1/productos/1
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") int precio,
            @RequestParam("stock") int stock,
            @RequestParam("categoriaId") Long categoriaId,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, nombre, precio, stock, categoriaId, imagen);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Eliminar producto
    // http://localhost:8080/api/v1/productos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - Servir imagen
    // http://localhost:8080/api/v1/productos/images/imagen.jpg
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> servirImagen(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(filename);

            // Determinar el tipo de contenido del archivo
            String contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
