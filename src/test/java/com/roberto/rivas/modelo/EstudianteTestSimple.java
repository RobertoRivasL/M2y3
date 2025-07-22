package com.roberto.rivas.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Estudiante - Version Simplificada
 * Sin clases anidadas para asegurar ejecución
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas del Modelo Estudiante - Simplificado")
class EstudianteTestSimple {

    private Estudiante estudiante;
    
    @BeforeEach
    void configurarPrueba() {
        estudiante = new Estudiante();
    }

    // =================== PRUEBAS DE CREACIÓN ===================

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
        
        System.out.println("✅ Constructor por defecto funciona");
    }
    
    @Test
    @DisplayName("Debe crear estudiante con constructor completo")
    void debeCrearEstudianteConConstructorCompleto() {
        // Arrange & Act
        Estudiante nuevoEstudiante = new Estudiante(1L, "Roberto", "Rivas", 
                                                   "roberto@test.com", "Ingeniería DevOps");
        
        // Assert
        assertEquals(1L, nuevoEstudiante.getId());
        assertEquals("Roberto", nuevoEstudiante.getNombre());
        assertEquals("Rivas", nuevoEstudiante.getApellido());
        assertEquals("roberto@test.com", nuevoEstudiante.getEmail());
        assertEquals("Ingeniería DevOps", nuevoEstudiante.getCarrera());
        assertTrue(nuevoEstudiante.isActivo());
        
        System.out.println("✅ Constructor completo funciona");
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
        
        System.out.println("✅ Constructor sin ID funciona");
    }

    // =================== PRUEBAS DE VALIDACIONES ===================

    @Test
    @DisplayName("Debe fallar al asignar nombre nulo")
    void debeFallarAlAsignarNombreNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, 
                    () -> estudiante.setNombre(null),
                    "El nombre no puede ser nulo o vacío");
        
        System.out.println("✅ Validación nombre nulo funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al asignar nombre vacío")
    void debeFallarAlAsignarNombreVacio() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, 
                    () -> estudiante.setNombre("   "),
                    "El nombre no puede ser nulo o vacío");
        
        System.out.println("✅ Validación nombre vacío funciona");
    }
    
    @Test
    @DisplayName("Debe trimear espacios en nombre")
    void debeTrimearEspaciosEnNombre() {
        // Arrange & Act
        estudiante.setNombre("  Roberto  ");
        
        // Assert
        assertEquals("Roberto", estudiante.getNombre());
        
        System.out.println("✅ Trimeo de espacios funciona");
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
        
        System.out.println("✅ Validaciones email funcionan");
    }
    
    @Test
    @DisplayName("Debe aceptar email válido y convertir a minúsculas")
    void debeAceptarEmailValidoYConvertirAMinusculas() {
        // Arrange & Act
        estudiante.setEmail("ROBERTO@TEST.COM");
        
        // Assert
        assertEquals("roberto@test.com", estudiante.getEmail());
        
        System.out.println("✅ Conversión email a minúsculas funciona");
    }

    // =================== PRUEBAS DE FECHA ===================

    @Test
    @DisplayName("Debe fallar al asignar fecha nula")
    void debeFallarAlAsignarFechaNula() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, 
                    () -> estudiante.setFechaIngreso(null),
                    "La fecha de ingreso no puede ser nula");
        
        System.out.println("✅ Validación fecha nula funciona");
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
        
        System.out.println("✅ Validación fecha futura funciona");
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
        
        System.out.println("✅ Asignación fecha válida funciona");
    }

    // =================== PRUEBAS DE MÉTODOS DE NEGOCIO ===================

    @Test
    @DisplayName("Debe obtener nombre completo")
    void debeObtenerNombreCompleto() {
        // Arrange
        estudiante.setNombre("Roberto");
        estudiante.setApellido("Rivas");
        
        // Act
        String nombreCompleto = estudiante.getNombreCompleto();
        
        // Assert
        assertEquals("Roberto Rivas", nombreCompleto);
        
        System.out.println("✅ Nombre completo funciona");
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
        
        System.out.println("✅ Activación funciona");
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
        
        System.out.println("✅ Desactivación funciona");
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
        
        System.out.println("✅ Comparación equals funciona");
    }

    @Test
    @DisplayName("Debe generar hashCode consistente")
    void debeGenerarHashCodeConsistente() {
        // Arrange
        Estudiante estudiante1 = new Estudiante(1L, "Roberto", "Rivas", 
                                               "roberto@test.com", "Ingeniería");
        Estudiante estudiante2 = new Estudiante(1L, "Ana", "García", 
                                               "roberto@test.com", "Medicina");
        
        // Act & Assert
        assertEquals(estudiante1.hashCode(), estudiante2.hashCode());
        
        System.out.println("✅ HashCode consistente funciona");
    }

    @Test
    @DisplayName("Debe generar toString informativo")
    void debeGenerarToStringInformativo() {
        // Arrange
        Estudiante est = new Estudiante(1L, "Roberto", "Rivas", 
                                      "roberto@test.com", "DevOps");
        
        // Act
        String resultado = est.toString();
        
        // Assert
        assertAll(
            () -> assertTrue(resultado.contains("Estudiante")),
            () -> assertTrue(resultado.contains("id=1")),
            () -> assertTrue(resultado.contains("Roberto")),
            () -> assertTrue(resultado.contains("roberto@test.com"))
        );
        
        System.out.println("✅ ToString funciona: " + resultado);
    }
}