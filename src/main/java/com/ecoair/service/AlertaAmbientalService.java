package com.ecoair.service;

import com.ecoair.domain.AlertaAmbiental;
import com.ecoair.domain.Severidad;
import com.ecoair.dto.AlertaAmbientalRequest;
import com.ecoair.repository.AlertaAmbientalRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertaAmbientalService {

    private final AlertaAmbientalRepository repository;

    public AlertaAmbientalService(AlertaAmbientalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AlertaAmbiental> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public AlertaAmbiental obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada: " + id));
    }

    @Transactional
    public AlertaAmbiental registrar(AlertaAmbientalRequest request) {
        AlertaAmbiental alerta = new AlertaAmbiental(
                request.contaminante(),
                request.valorMedido(),
                request.unidad(),
                request.ubicacion(),
                request.severidad());
        return repository.save(alerta);
    }

    @Transactional(readOnly = true)
    public long contarCriticas() {
        return repository.countBySeveridad(Severidad.CRITICA);
    }
}
