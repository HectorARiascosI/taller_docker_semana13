package com.ecoair.repository;

import com.ecoair.domain.AlertaAmbiental;
import com.ecoair.domain.Severidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaAmbientalRepository extends JpaRepository<AlertaAmbiental, Long> {

    long countBySeveridad(Severidad severidad);
}
