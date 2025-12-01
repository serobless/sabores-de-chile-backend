package com.example.saboresapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "boleta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    @Builder.Default
    private int total = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // <-- ¡Anotación correcta!
    private Usuario usuario;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleBoleta> detalles = new ArrayList<>();

    @Column
    private String mesa;

    @Column
    private String tipoEntrega;

    @Column
    private String infoCliente;

    @Column
    private String metodoPago;
}
