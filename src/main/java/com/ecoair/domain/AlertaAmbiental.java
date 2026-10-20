package com.ecoair.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "alertas_ambientales")
public class AlertaAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contaminante;

    @Column(nullable = false)
    private Double valorMedido;

    @Column(nullable = false)
    private String unidad;

    @Column(nullable = false)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severidad severidad;

    @Column(nullable = false, updatable = false)
    private Instant timestamp;

    /** Requerido por JPA. */
    public AlertaAmbiental() {
    }

    public AlertaAmbiental(String contaminante, Double valorMedido, String unidad,
                           String ubicacion, Severidad severidad) {
        this.contaminante = contaminante;
        this.valorMedido = valorMedido;
        this.unidad = unidad;
        this.ubicacion = ubicacion;
        this.severidad = severidad;
        this.timestamp = Instant.now();
    }

    public Long getId() { return id; }
    public String getContaminante() { return contaminante; }
    public Double getValorMedido() { return valorMedido; }
    public String getUnidad() { return unidad; }
    public String getUbicacion() { return ubicacion; }
    public Severidad getSeveridad() { return severidad; }
    public Instant getTimestamp() { return timestamp; }
    public void setId(Long id) { this.id = id; }
}
