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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DetalleBoletaService {

    private final DetalleBoletaRepository detalleBoletaRepository;
    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;

    public DetalleBoleta crearDetalle(Long boletaId, Long productoId, int cantidad) {
        // 1. Validar que Boleta y Producto existan.
        Boleta boleta = boletaRepository.findById(boletaId)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada con id: " + boletaId));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        // 2. Obtener el precio del producto.
        int precioUnitario = producto.getPrecio();

        // 3. Calcular subtotal.
        int subtotal = precioUnitario * cantidad;

        // 4. Crear y guardar el DetalleBoleta.
        DetalleBoleta detalle = DetalleBoleta.builder()
                .boleta(boleta)
                .producto(producto)
                .cantidad(cantidad)
                .precioUnitario(precioUnitario)
                .subtotal(subtotal)
                .build();
        
        DetalleBoleta detalleGuardado = detalleBoletaRepository.save(detalle);

        // 5. ACTUALIZAR BOLETA: Sumar el nuevo subtotal al total actual de la Boleta.
        boleta.setTotal(boleta.getTotal() + subtotal);
        boletaRepository.save(boleta);

        return detalleGuardado;
    }

    public List<DetalleBoleta> listarPorBoleta(Long boletaId) {
        return detalleBoletaRepository.findByBoletaId(boletaId);
    }

    public DetalleBoleta buscarPorId(Long id) {
        Optional<DetalleBoleta> detalleOpt = detalleBoletaRepository.findById(id);
        return detalleOpt.orElseThrow(() -> new RuntimeException("DetalleBoleta no encontrado con id: " + id));
    }

    public void eliminarDetalle(Long id) {
        // Buscar el detalle a eliminar
        DetalleBoleta detalle = detalleBoletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleBoleta no encontrado con id: " + id));

        // Obtener la boleta asociada
        Boleta boleta = detalle.getBoleta();

        // Restar el subtotal del detalle al total de la boleta
        boleta.setTotal(boleta.getTotal() - detalle.getSubtotal());
        boletaRepository.save(boleta);

        // Finalmente, eliminar el detalle
        detalleBoletaRepository.deleteById(id);
    }
}
