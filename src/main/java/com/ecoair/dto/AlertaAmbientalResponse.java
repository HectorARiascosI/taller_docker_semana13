package com.ecoair.dto;

import com.ecoair.domain.AlertaAmbiental;
import com.ecoair.domain.Severidad;
import java.time.Instant;

public record AlertaAmbientalResponse(
        Long id,
        String contaminante,
        Double valorMedido,
        String unidad,
        String ubicacion,
        Severidad severidad,
        Instant timestamp
) {
    public static AlertaAmbientalResponse from(AlertaAmbiental alerta) {
        return new AlertaAmbientalResponse(
                alerta.getId(),
                alerta.getContaminante(),
                alerta.getValorMedido(),
                alerta.getUnidad(),
                alerta.getUbicacion(),
                alerta.getSeveridad(),
                alerta.getTimestamp());
    }
}
