package com.roberto.rivas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba de diagnóstico para verificar que JUnit 5 funciona correctamente
 * Si esta prueba no se ejecuta, hay un problema de configuración
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Prueba de Diagnóstico JUnit 5")
class TestDiagnostico {

    @Test
    @DisplayName("Esta prueba debe ejecutarse correctamente")
    void estaPruebaDebeEjecutarseCorrectamente() {
        // Arrange
        String mensaje = "JUnit 5 funciona correctamente";
        
        // Act
        boolean esVerdadero = true;
        
        // Assert
        assertTrue(esVerdadero);
        assertEquals("JUnit 5 funciona correctamente", mensaje);
        
        System.out.println("✅ DIAGNÓSTICO: JUnit 5 está funcionando correctamente");
    }

    @Test
    @DisplayName("Prueba de números básicos")
    void pruebaDeNumerosBasicos() {
        assertEquals(4, 2 + 2);
        assertTrue(5 > 3);
        assertNotNull("test");
        
        System.out.println("✅ DIAGNÓSTICO: Assertions básicas funcionan");
    }

    @Test
    @DisplayName("Prueba de strings")
    void pruebaDeStrings() {
        String nombre = "Roberto";
        String apellido = "Rivas";
        
        assertEquals("Roberto Rivas", nombre + " " + apellido);
        assertTrue(nombre.contains("Rob"));
        
        System.out.println("✅ DIAGNÓSTICO: Pruebas de strings funcionan");
    }
}