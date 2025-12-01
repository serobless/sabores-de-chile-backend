package com.example.saboresapi.repository;

import com.example.saboresapi.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {

    List<DetalleBoleta> findByBoletaId(Long boletaId);

    List<DetalleBoleta> findByProductoId(Long productoId);
}
