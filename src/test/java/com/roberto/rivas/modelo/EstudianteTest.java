package com.roberto.rivas.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Estudiante
 * Ciclos TDD 1-4: Validación del modelo de dominio
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas del Modelo Estudiante")
public class EstudianteTest {

    private Estudiante estudiante;
    
    @BeforeEach
    void configurarPrueba() {
        estudiante = new Estudiante();
    }

    @Nested
    @DisplayName("Ciclo TDD 1: Creación de Estudiante")
    class CreacionEstudiante {
        
        @Test
        @DisplayName("Debe crear estudiante con constructor por defecto")
        void debeCrearEstudianteConConstructorPorDefecto() {
            // Arrange & Act
            Estudiante nuevoEstudiante = new Estudiante();
            
            // Assert
            assertNotNull(nuevoEstudiante);
            assertTrue(nuevoEstudiante.isActivo());
            assertNotNull(nuevoEstudiante.getFechaIngreso());
            assertEquals(LocalDate.now(), nuevoEstudiante.getFechaIngreso());
        }
        
        @Test
        @DisplayName("Debe crear estudiante con constructor completo")
        void debeCrearEstudianteConConstructorCompleto() {
            // Arrange & Act
            Estudiante nuevoEstudiante = new Estudiante(1L, "Roberto", "Rivas", 
                                                       "roberto@test.com", "Ingeniería");
            
            // Assert
            assertEquals(1L, nuevoEstudiante.getId());
            assertEquals("Roberto", nuevoEstudiante.getNombre());
            assertEquals("Rivas", nuevoEstudiante.getApellido());
            assertEquals("roberto@test.com", nuevoEstudiante.getEmail());
            assertEquals("Ingeniería", nuevoEstudiante.getCarrera());
            assertTrue(nuevoEstudiante.isActivo());
        }
        
        @Test
        @DisplayName("Debe crear estudiante sin ID para inserción")
        void debeCrearEstudianteSinIdParaInsercion() {
            // Arrange & Act
            Estudiante nuevoEstudiante = new Estudiante("Ana", "García", 
                                                       "ana@test.com", "Medicina");
            
            // Assert
            assertNull(nuevoEstudiante.getId());
            assertEquals("Ana", nuevoEstudiante.getNombre());
            assertEquals("García", nuevoEstudiante.getApellido());
            assertEquals("ana@test.com", nuevoEstudiante.getEmail());
            assertEquals("Medicina", nuevoEstudiante.getCarrera());
        }
    }

    @Nested
    @DisplayName("Ciclo TDD 2: Validaciones de Campos")
    class ValidacionesCampos {
        
        @Test
        @DisplayName("Debe fallar al asignar nombre nulo")
        void debeFallarAlAsignarNombreNulo() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, 
                        () -> estudiante.setNombre(null),
                        "El nombre no puede ser nulo o vacío");
        }
        
        @Test
        @DisplayName("Debe fallar al asignar nombre vacío")
        void debeFallarAlAsignarNombreVacio() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, 
                        () -> estudiante.setNombre("   "),
                        "El nombre no puede ser nulo o vacío");
        }
        
        @Test
        @DisplayName("Debe trimear espacios en nombre")
        void debeTrimearEspaciosEnNombre() {
            // Arrange & Act
            estudiante.setNombre("  Roberto  ");
            
            // Assert
            assertEquals("Roberto", estudiante.getNombre());
        }
        
        @Test
        @DisplayName("Debe fallar al asignar email inválido")
        void debeFallarAlAsignarEmailInvalido() {
            // Arrange, Act & Assert
            assertAll(
                () -> assertThrows(IllegalArgumentException.class, 
                                 () -> estudiante.setEmail("email-invalido")),
                () -> assertThrows(IllegalArgumentException.class, 
                                 () -> estudiante.setEmail("@dominio.com")),
                () -> assertThrows(IllegalArgumentException.class, 
                                 () -> estudiante.setEmail("usuario@"))
            );
        }
        
        @Test
        @DisplayName("Debe aceptar email válido y convertir a minúsculas")
        void debeAceptarEmailValidoYConvertirAMinusculas() {
            // Arrange & Act
            estudiante.setEmail("ROBERTO@TEST.COM");
            
            // Assert
            assertEquals("roberto@test.com", estudiante.getEmail());
        }
    }

    @Nested
    @DisplayName("Ciclo TDD 3: Validaciones de Fecha")
    class ValidacionesFecha {
        
        @Test
        @DisplayName("Debe fallar al asignar fecha nula")
        void debeFallarAlAsignarFechaNula() {
            // Arrange, Act & Assert
            assertThrows(IllegalArgumentException.class, 
                        () -> estudiante.setFechaIngreso(null),
                        "La fecha de ingreso no puede ser nula");
        }
        
        @Test
        @DisplayName("Debe fallar al asignar fecha futura")
        void debeFallarAlAsignarFechaFutura() {
            // Arrange
            LocalDate fechaFutura = LocalDate.now().plusDays(1);
            
            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                        () -> estudiante.setFechaIngreso(fechaFutura),
                        "La fecha de ingreso no puede ser futura");
        }
        
        @Test
        @DisplayName("Debe aceptar fecha válida")
        void debeAceptarFechaValida() {
            // Arrange
            LocalDate fechaValida = LocalDate.now().minusDays(30);
            
            // Act
            estudiante.setFechaIngreso(fechaValida);
            
            // Assert
            assertEquals(fechaValida, estudiante.getFechaIngreso());
        }
    }

    @Nested
    @DisplayName("Ciclo TDD 4: Métodos de Negocio")
    class MetodosNegocio {
        
        @BeforeEach
        void configurar() {
            estudiante.setNombre("Roberto");
            estudiante.setApellido("Rivas");
        }
        
        @Test
        @DisplayName("Debe obtener nombre completo")
        void debeObtenerNombreCompleto() {
            // Act
            String nombreCompleto = estudiante.getNombreCompleto();
            
            // Assert
            assertEquals("Roberto Rivas", nombreCompleto);
        }
        
        @Test
        @DisplayName("Debe activar estudiante")
        void debeActivarEstudiante() {
            // Arrange
            estudiante.setActivo(false);
            
            // Act
            estudiante.activar();
            
            // Assert
            assertTrue(estudiante.isActivo());
        }
        
        @Test
        @DisplayName("Debe desactivar estudiante")
        void debeDesactivarEstudiante() {
            // Arrange
            estudiante.setActivo(true);
            
            // Act
            estudiante.desactivar();
            
            // Assert
            assertFalse(estudiante.isActivo());
        }
        
        @Test
        @DisplayName("Debe comparar estudiantes por equals")
        void debeCompararEstudiantesPorEquals() {
            // Arrange
            Estudiante estudiante1 = new Estudiante(1L, "Roberto", "Rivas", 
                                                   "roberto@test.com", "Ingeniería");
            Estudiante estudiante2 = new Estudiante(1L, "Ana", "García", 
                                                   "roberto@test.com", "Medicina");
            Estudiante estudiante3 = new Estudiante(2L, "Carlos", "López", 
                                                   "carlos@test.com", "Derecho");
            
            // Act & Assert
            assertEquals(estudiante1, estudiante2); // Mismo ID y email
            assertNotEquals(estudiante1, estudiante3); // Diferente ID y email
        }
    }
}