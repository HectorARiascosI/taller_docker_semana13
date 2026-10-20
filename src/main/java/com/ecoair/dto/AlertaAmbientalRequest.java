package com.ecoair.dto;

import com.ecoair.domain.Severidad;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlertaAmbientalRequest(
        @NotBlank String contaminante,
        @NotNull @Min(0) Double valorMedido,
        @NotBlank String unidad,
        @NotBlank String ubicacion,
        @NotNull Severidad severidad
) {
}
