package com.ecoair;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Test de contexto: verifica que el contexto de Spring Boot carga correctamente
 * y que el cableado de beans (servicio, repositorio, controlador) es valido.
 */
@SpringBootTest
class EcoAirApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void elContextoCarga() {
        org.springframework.util.Assert.notNull(context, "El contexto no debe ser nulo");
    }
}
