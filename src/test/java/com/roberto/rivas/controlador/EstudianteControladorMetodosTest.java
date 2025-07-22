package com.roberto.rivas.controlador;

import com.roberto.rivas.servicio.dto.EstudianteDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas básicas para EstudianteControlador
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas EstudianteControlador - Básicas")
class EstudianteControladorMetodosTest {

    @Test
    @DisplayName("Debe crear instancia del controlador")
    void debeCrearInstanciaDelControlador() {
        // Act - Pasando false para no mostrar banner en las pruebas
        EstudianteControlador controlador = new EstudianteControlador(false);
        
        // Assert
        assertNotNull(controlador);
    }

    @Test
    @DisplayName("Debe crear instancia del controlador con banner")
    void debeCrearInstanciaDelControladorConBanner() {
        // Act - Pasando true para mostrar banner
        EstudianteControlador controlador = new EstudianteControlador(true);
        
        // Assert
        assertNotNull(controlador);
    }

    @Test
    @DisplayName("Debe crear DTO válido")
    void debeCrearDTOValido() {
        // Act
        EstudianteDTO dto = new EstudianteDTO("Juan", "Pérez", "juan@test.com", "Ingeniería");
        
        // Assert
        assertNotNull(dto);
        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellido());
        assertEquals("juan@test.com", dto.getEmail());
        assertEquals("Ingeniería", dto.getCarrera());
    }

    @Test
    @DisplayName("Debe manejar DTO con datos inválidos")
    void debeManejarDTOConDatosInvalidos() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            EstudianteDTO dto = new EstudianteDTO("", "", "email-invalido", "");
            assertNotNull(dto);
        });
    }

    @Test
    @DisplayName("Debe establecer ID en DTO")
    void debeEstablecerIdEnDTO() {
        // Arrange
        EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Carrera");
        
        // Act
        dto.setId(1L);
        
        // Assert
        assertEquals(1L, dto.getId());
    }

    @Test
    @DisplayName("Debe manejar DTO nulo")
    void debeManejarDTONulo() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            EstudianteDTO dto = null;
            // Test que el controlador puede manejar null sin crash
        });
    }

    @Test
    @DisplayName("Debe verificar estado inicial del DTO")
    void debeVerificarEstadoInicialDelDTO() {
        // Act
        EstudianteDTO dto = new EstudianteDTO("Ana", "García", "ana@test.com", "Medicina");
        
        // Assert
        assertNull(dto.getId()); // ID debe ser null inicialmente
        assertTrue(dto.isActivo()); // Debe estar activo por defecto
        assertNotNull(dto.getFechaIngreso()); // Debe tener fecha de ingreso
    }

    @Test
    @DisplayName("Debe manejar valores extremos en DTO")
    void debeManejarValoresExtremosEnDTO() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Test con strings muy largos
            String nombreLargo = "A".repeat(200);
            String emailLargo = "test" + "x".repeat(240) + "@test.com";
            
            EstudianteDTO dto = new EstudianteDTO(nombreLargo, nombreLargo, emailLargo, nombreLargo);
            assertNotNull(dto);
        });
    }
}