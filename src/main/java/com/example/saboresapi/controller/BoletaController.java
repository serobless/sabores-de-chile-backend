package com.example.saboresapi.controller;

import com.example.saboresapi.model.Boleta;
import com.example.saboresapi.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    // POST /api/v1/boletas - crear boleta
    @PostMapping
    public ResponseEntity<?> crearBoleta(@RequestBody BoletaRequest request) {
        try {
            Boleta boleta = boletaService.crearBoleta(
                    request.getUsuarioId(),
                    request.getTotal(),
                    request.getMesa(),
                    request.getTipoEntrega(),
                    request.getInfoCliente(),
                    request.getMetodoPago());
            return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /api/v1/boletas - listar todas
    @GetMapping
    public ResponseEntity<?> listarTodas() {
        try {
            List<Boleta> boletas = boletaService.listarTodas();
            return ResponseEntity.ok(boletas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // GET /api/v1/boletas/{id} - buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Boleta> boleta = boletaService.buscarPorId(id);
            if (boleta.isPresent()) {
                return ResponseEntity.ok(boleta.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Boleta no encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // GET /api/v1/boletas/usuario/{usuarioId} - listar por usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Boleta> boletas = boletaService.listarPorUsuario(usuarioId);
            return ResponseEntity.ok(boletas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // DELETE /api/v1/boletas/{id} - eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarBoleta(@PathVariable Long id) {
        try {
            boletaService.eliminarBoleta(id);
            return ResponseEntity.ok("Boleta eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Clase interna para recibir la solicitud de creación de boleta
    public static class BoletaRequest {
        private Long usuarioId;
        private int total;
        private String mesa;
        private String tipoEntrega;
        private String infoCliente;
        private String metodoPago;

        public Long getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(Long usuarioId) {
            this.usuarioId = usuarioId;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getMesa() {
            return mesa;
        }

        public void setMesa(String mesa) {
            this.mesa = mesa;
        }

        public String getTipoEntrega() {
            return tipoEntrega;
        }

        public void setTipoEntrega(String tipoEntrega) {
            this.tipoEntrega = tipoEntrega;
        }

        public String getInfoCliente() {
            return infoCliente;
        }

        public void setInfoCliente(String infoCliente) {
            this.infoCliente = infoCliente;
        }

        public String getMetodoPago() {
            return metodoPago;
        }

        public void setMetodoPago(String metodoPago) {
            this.metodoPago = metodoPago;
        }
    }
}
