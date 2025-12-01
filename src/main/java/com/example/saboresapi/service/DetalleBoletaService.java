package com.example.saboresapi.service;

import com.example.saboresapi.model.Boleta;
import com.example.saboresapi.model.DetalleBoleta;
import com.example.saboresapi.model.Producto;
import com.example.saboresapi.repository.BoletaRepository;
import com.example.saboresapi.repository.DetalleBoletaRepository;
import com.example.saboresapi.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DetalleBoletaService {

    private final DetalleBoletaRepository detalleBoletaRepository;
    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;

    public DetalleBoleta crearDetalle(Long boletaId, Long productoId, int cantidad) {
        // Validar que boleta existe
        Boleta boleta = boletaRepository.findById(boletaId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada con id: " + boletaId));

        // Validar que producto existe
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        // Calcular precioUnitario automáticamente desde producto.getPrecio()
        int precioUnitario = producto.getPrecio();

        // Crear y guardar el detalle
        DetalleBoleta detalle = DetalleBoleta.builder()
                .boleta(boleta)
                .producto(producto)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .build();

        return detalleBoletaRepository.save(detalle);
    }

    public List<DetalleBoleta> listarPorBoleta(Long boletaId) {
        return detalleBoletaRepository.findByBoletaId(boletaId);
    }

    public DetalleBoleta buscarPorId(Long id) {
        return detalleBoletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleBoleta no encontrado con id: " + id));
    }

    public void eliminarDetalle(Long id) {
        if (!detalleBoletaRepository.existsById(id)) {
            throw new RuntimeException("DetalleBoleta no encontrado con id: " + id);
        }
        detalleBoletaRepository.deleteById(id);
    }
}
