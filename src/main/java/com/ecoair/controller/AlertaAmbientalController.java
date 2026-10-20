package com.ecoair.controller;

import com.ecoair.domain.AlertaAmbiental;
import com.ecoair.dto.AlertaAmbientalRequest;
import com.ecoair.dto.AlertaAmbientalResponse;
import com.ecoair.service.AlertaAmbientalService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alertas")
public class AlertaAmbientalController {

    private final AlertaAmbientalService service;

    public AlertaAmbientalController(AlertaAmbientalService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlertaAmbientalResponse> listar() {
        return service.listarTodas().stream()
                .map(AlertaAmbientalResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaAmbientalResponse> obtener(@PathVariable Long id) {
        AlertaAmbiental alerta = service.obtenerPorId(id);
        return ResponseEntity.ok(AlertaAmbientalResponse.from(alerta));
    }

    @PostMapping
    public ResponseEntity<AlertaAmbientalResponse> registrar(@Valid @RequestBody AlertaAmbientalRequest request) {
        AlertaAmbiental creada = service.registrar(request);
        URI ubicacion = URI.create("/api/alertas/" + creada.getId());
        return ResponseEntity.created(ubicacion).body(AlertaAmbientalResponse.from(creada));
    }

    @GetMapping("/criticas")
    public long contarCriticas() {
        return service.contarCriticas();
    }
}
