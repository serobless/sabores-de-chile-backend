package com.example.saboresapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "detalle_boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private int precioUnitario;

    @Column(nullable = false)
    private int subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boleta_id", nullable = false)
    @JsonIgnore
    private Boleta boleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    // Se mantiene la serialización del producto para poder ver sus datos en el detalle
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // <-- ¡Anotación correcta!
    private Producto producto;
}
