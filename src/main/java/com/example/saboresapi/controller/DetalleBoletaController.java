package com.example.saboresapi.controller;

import com.example.saboresapi.model.DetalleBoleta;
import com.example.saboresapi.service.DetalleBoletaService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detalles")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DetalleBoletaController {

    private final DetalleBoletaService detalleBoletaService;

    @PostMapping
    public ResponseEntity<DetalleBoleta> crear(@RequestBody DetalleBoletaRequest request) {
        try {
            DetalleBoleta detalle = detalleBoletaService.crearDetalle(
                    request.getBoletaId(),
                    request.getProductoId(),
                    request.getCantidad()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/boleta/{boletaId}")
    public ResponseEntity<List<DetalleBoleta>> listarPorBoleta(@PathVariable Long boletaId) {
        List<DetalleBoleta> detalles = detalleBoletaService.listarPorBoleta(boletaId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleBoleta> buscarPorId(@PathVariable Long id) {
        try {
            DetalleBoleta detalle = detalleBoletaService.buscarPorId(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            detalleBoletaService.eliminarDetalle(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Data
    public static class DetalleBoletaRequest {
        private Long boletaId;
        private Long productoId;
        private int cantidad;
    }
}
