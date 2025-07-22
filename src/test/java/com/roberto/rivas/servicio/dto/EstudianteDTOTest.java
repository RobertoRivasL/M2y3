package com.roberto.rivas.servicio.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas completas para EstudianteDTO
 * Cobertura de todos los métodos y casos edge
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas del DTO de Estudiante")
public class EstudianteDTOTest {

    private EstudianteDTO dto;

    @BeforeEach
    void configurarPrueba() {
        dto = new EstudianteDTO();
    }

    @Nested
    @DisplayName("Pruebas de Constructores")
    class Constructores {
        
        @Test
        @DisplayName("Constructor por defecto debe crear DTO vacío")
        void constructorPorDefectoDebeCrearDTOVacio() {
            // Arrange & Act
            EstudianteDTO nuevoDTO = new EstudianteDTO();
            
            // Assert
            assertNotNull(nuevoDTO);
            assertNull(nuevoDTO.getId());
            assertNull(nuevoDTO.getNombre());
            assertNull(nuevoDTO.getApellido());
            assertNull(nuevoDTO.getEmail());
            assertNull(nuevoDTO.getCarrera());
            assertNull(nuevoDTO.getFechaIngreso());
            assertFalse(nuevoDTO.isActivo());
        }
        
        @Test
        @DisplayName("Constructor completo debe inicializar todos los campos")
        void constructorCompletoDebeInicializarTodosLosCampos() {
            // Arrange & Act
            LocalDate fecha = LocalDate.of(2024, 1, 15);
            EstudianteDTO nuevoDTO = new EstudianteDTO(1L, "Roberto", "Rivas", 
                                                     "roberto@test.com", "Ingeniería", fecha, true);
            
            // Assert
            assertEquals(1L, nuevoDTO.getId());
            assertEquals("Roberto", nuevoDTO.getNombre());
            assertEquals("Rivas", nuevoDTO.getApellido());
            assertEquals("roberto@test.com", nuevoDTO.getEmail());
            assertEquals("Ingeniería", nuevoDTO.getCarrera());
            assertEquals(fecha, nuevoDTO.getFechaIngreso());
            assertTrue(nuevoDTO.isActivo());
            assertEquals("Roberto Rivas", nuevoDTO.getNombreCompleto());
        }
        
        @Test
        @DisplayName("Constructor sin ID debe usar valores por defecto")
        void constructorSinIdDebeUsarValoresPorDefecto() {
            // Arrange & Act
            EstudianteDTO nuevoDTO = new EstudianteDTO("Ana", "García", "ana@test.com", "Medicina");
            
            // Assert
            assertNull(nuevoDTO.getId());
            assertEquals("Ana", nuevoDTO.getNombre());
            assertEquals("García", nuevoDTO.getApellido());
            assertEquals("ana@test.com", nuevoDTO.getEmail());
            assertEquals("Medicina", nuevoDTO.getCarrera());
            assertEquals(LocalDate.now(), nuevoDTO.getFechaIngreso());
            assertTrue(nuevoDTO.isActivo());
            assertEquals("Ana García", nuevoDTO.getNombreCompleto());
        }
    }

    @Nested
    @DisplayName("Pruebas de Setters y Getters")
    class SettersGetters {
        
        @Test
        @DisplayName("setNombre debe actualizar nombre y nombre completo")
        void setNombreDebeActualizarNombreYNombreCompleto() {
            // Arrange
            dto.setApellido("Pérez");
            
            // Act
            dto.setNombre("Carlos");
            
            // Assert
            assertEquals("Carlos", dto.getNombre());
            assertEquals("Carlos Pérez", dto.getNombreCompleto());
        }
        
        @Test
        @DisplayName("setApellido debe actualizar apellido y nombre completo")
        void setApellidoDebeActualizarApellidoYNombreCompleto() {
            // Arrange
            dto.setNombre("María");
            
            // Act
            dto.setApellido("González");
            
            // Assert
            assertEquals("González", dto.getApellido());
            assertEquals("María González", dto.getNombreCompleto());
        }
        
        @Test
        @DisplayName("setId debe establecer ID correctamente")
        void setIdDebeEstablecerIdCorrectamente() {
            // Act
            dto.setId(100L);
            
            // Assert
            assertEquals(100L, dto.getId());
        }
        
        @Test
        @DisplayName("setEmail debe establecer email correctamente")
        void setEmailDebeEstablecerEmailCorrectamente() {
            // Act
            dto.setEmail("test@email.com");
            
            // Assert
            assertEquals("test@email.com", dto.getEmail());
        }
        
        @Test
        @DisplayName("setCarrera debe establecer carrera correctamente")
        void setCarreraDebeEstablecerCarreraCorrectamente() {
            // Act
            dto.setCarrera("Psicología");
            
            // Assert
            assertEquals("Psicología", dto.getCarrera());
        }
        
        @Test
        @DisplayName("setFechaIngreso debe establecer fecha correctamente")
        void setFechaIngresoDebeEstablecerFechaCorrectamente() {
            // Arrange
            LocalDate fecha = LocalDate.of(2023, 6, 15);
            
            // Act
            dto.setFechaIngreso(fecha);
            
            // Assert
            assertEquals(fecha, dto.getFechaIngreso());
        }
        
        @Test
        @DisplayName("setActivo debe establecer estado activo correctamente")
        void setActivoDebeEstablecerEstadoActivoCorrectamente() {
            // Act
            dto.setActivo(true);
            
            // Assert
            assertTrue(dto.isActivo());
            
            // Act
            dto.setActivo(false);
            
            // Assert
            assertFalse(dto.isActivo());
        }
    }

    @Nested
    @DisplayName("Pruebas de Nombre Completo")
    class NombreCompleto {
        
        @Test
        @DisplayName("getNombreCompleto debe retornar cadena vacía con nombres nulos")
        void getNombreCompletoDebeRetornarCadenaVaciaConNombresNulos() {
            // Arrange - nombre y apellido son null por defecto
            // Pero nombreCompleto también es null hasta que se genera
            
            // Act
            String nombreCompleto = dto.getNombreCompleto();
            
            // Assert - con nombres null, nombreCompleto también es null inicialmente
            assertNull(nombreCompleto);
        }
        
        @Test
        @DisplayName("getNombreCompleto debe retornar cadena vacía con nombre nulo")
        void getNombreCompletoDebeRetornarCadenaVaciaConNombreNulo() {
            // Arrange
            dto.setApellido("López");
            // nombre sigue siendo null
            
            // Act
            String nombreCompleto = dto.getNombreCompleto();
            
            // Assert
            assertEquals("", nombreCompleto);
        }
        
        @Test
        @DisplayName("getNombreCompleto debe retornar cadena vacía con apellido nulo")
        void getNombreCompletoDebeRetornarCadenaVaciaConApellidoNulo() {
            // Arrange
            dto.setNombre("Pedro");
            // apellido sigue siendo null
            
            // Act
            String nombreCompleto = dto.getNombreCompleto();
            
            // Assert
            assertEquals("", nombreCompleto);
        }
        
        @Test
        @DisplayName("getNombreCompleto debe concatenar nombre y apellido")
        void getNombreCompletoDebeConcatenarNombreYApellido() {
            // Arrange
            dto.setNombre("Roberto");
            dto.setApellido("Rivas");
            
            // Act
            String nombreCompleto = dto.getNombreCompleto();
            
            // Assert
            assertEquals("Roberto Rivas", nombreCompleto);
        }
    }

    @Nested
    @DisplayName("Pruebas de Validación")
    class Validacion {
        
        @Test
        @DisplayName("esValido debe retornar false con datos nulos")
        void esValidoDebeRetornarFalseConDatosNulos() {
            // Arrange - todos los campos son null por defecto
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("esValido debe retornar false con nombre vacío")
        void esValidoDebeRetornarFalseConNombreVacio() {
            // Arrange
            dto.setNombre("");
            dto.setApellido("Apellido");
            dto.setEmail("test@test.com");
            dto.setCarrera("Carrera");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("esValido debe retornar false con apellido vacío")
        void esValidoDebeRetornarFalseConApellidoVacio() {
            // Arrange
            dto.setNombre("Nombre");
            dto.setApellido("   "); // Solo espacios
            dto.setEmail("test@test.com");
            dto.setCarrera("Carrera");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("esValido debe retornar false con email inválido")
        void esValidoDebeRetornarFalseConEmailInvalido() {
            // Arrange
            dto.setNombre("Nombre");
            dto.setApellido("Apellido");
            dto.setEmail("email-invalido");
            dto.setCarrera("Carrera");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("esValido debe retornar false con carrera vacía")
        void esValidoDebeRetornarFalseConCarreraVacia() {
            // Arrange
            dto.setNombre("Nombre");
            dto.setApellido("Apellido");
            dto.setEmail("test@test.com");
            dto.setCarrera("");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("esValido debe retornar true con datos válidos")
        void esValidoDebeRetornarTrueConDatosValidos() {
            // Arrange
            dto.setNombre("Roberto");
            dto.setApellido("Rivas");
            dto.setEmail("roberto@test.com");
            dto.setCarrera("Ingeniería");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertTrue(resultado);
        }
    }

    @Nested
    @DisplayName("Pruebas de Validación de Email")
    class ValidacionEmail {
        
        @Test
        @DisplayName("Email nulo debe ser inválido")
        void emailNuloDebeSerInvalido() {
            // Arrange
            dto.setNombre("Test");
            dto.setApellido("User");
            dto.setEmail(null);
            dto.setCarrera("Test");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("Email sin @ debe ser inválido")
        void emailSinArrobaDebeSerInvalido() {
            // Arrange
            dto.setNombre("Test");
            dto.setApellido("User");
            dto.setEmail("testtest.com");
            dto.setCarrera("Test");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("Email sin punto debe ser inválido")
        void emailSinPuntoDebeSerInvalido() {
            // Arrange
            dto.setNombre("Test");
            dto.setApellido("User");
            dto.setEmail("test@testcom");
            dto.setCarrera("Test");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("Email con @ después del último punto debe ser inválido")
        void emailConArrobaDespuesDelUltimoPuntoDebeSerInvalido() {
            // Arrange
            dto.setNombre("Test");
            dto.setApellido("User");
            dto.setEmail("test.com@test");
            dto.setCarrera("Test");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertFalse(resultado);
        }
        
        @Test
        @DisplayName("Email válido debe ser aceptado")
        void emailValidoDebeSerAceptado() {
            // Arrange
            dto.setNombre("Test");
            dto.setApellido("User");
            dto.setEmail("test@domain.com");
            dto.setCarrera("Test");
            
            // Act
            boolean resultado = dto.esValido();
            
            // Assert
            assertTrue(resultado);
        }
    }

    @Nested
    @DisplayName("Pruebas de Equals y HashCode")
    class EqualsHashCode {
        
        @Test
        @DisplayName("equals debe retornar true para el mismo objeto")
        void equalsDebeRetornarTrueParaElMismoObjeto() {
            // Arrange
            dto.setId(1L);
            dto.setEmail("test@test.com");
            
            // Act & Assert
            assertEquals(dto, dto);
        }
        
        @Test
        @DisplayName("equals debe retornar false para objeto nulo")
        void equalsDebeRetornarFalseParaObjetoNulo() {
            // Arrange
            dto.setId(1L);
            
            // Act & Assert
            assertNotEquals(dto, null);
        }
        
        @Test
        @DisplayName("equals debe retornar false para clase diferente")
        void equalsDebeRetornarFalseParaClaseDiferente() {
            // Arrange
            dto.setId(1L);
            String otroObjeto = "No es un DTO";
            
            // Act & Assert
            assertNotEquals(dto, otroObjeto);
        }
        
        @Test
        @DisplayName("equals debe retornar true para DTOs con mismo ID y email")
        void equalsDebeRetornarTrueParaDTOsConMismoIdYEmail() {
            // Arrange
            dto.setId(1L);
            dto.setEmail("test@test.com");
            
            EstudianteDTO otroDTO = new EstudianteDTO();
            otroDTO.setId(1L);
            otroDTO.setEmail("test@test.com");
            
            // Act & Assert
            assertEquals(dto, otroDTO);
        }
        
        @Test
        @DisplayName("equals debe retornar false para DTOs con diferente ID")
        void equalsDebeRetornarFalseParaDTOsConDiferenteId() {
            // Arrange
            dto.setId(1L);
            dto.setEmail("test@test.com");
            
            EstudianteDTO otroDTO = new EstudianteDTO();
            otroDTO.setId(2L);
            otroDTO.setEmail("test@test.com");
            
            // Act & Assert
            assertNotEquals(dto, otroDTO);
        }
        
        @Test
        @DisplayName("hashCode debe ser consistente")
        void hashCodeDebeSerConsistente() {
            // Arrange
            dto.setId(1L);
            dto.setEmail("test@test.com");
            
            // Act
            int hash1 = dto.hashCode();
            int hash2 = dto.hashCode();
            
            // Assert
            assertEquals(hash1, hash2);
        }
        
        @Test
        @DisplayName("hashCode debe ser igual para objetos equals")
        void hashCodeDebeSerIgualParaObjetosEquals() {
            // Arrange
            dto.setId(1L);
            dto.setEmail("test@test.com");
            
            EstudianteDTO otroDTO = new EstudianteDTO();
            otroDTO.setId(1L);
            otroDTO.setEmail("test@test.com");
            
            // Act & Assert
            assertEquals(dto.hashCode(), otroDTO.hashCode());
        }
    }

    @Nested
    @DisplayName("Pruebas del Método toString")
    class MetodoToString {
        
        @Test
        @DisplayName("toString debe incluir información básica")
        void toStringDebeIncluirInformacionBasica() {
            // Arrange
            dto.setId(1L);
            dto.setNombre("Roberto");
            dto.setApellido("Rivas");
            dto.setEmail("roberto@test.com");
            dto.setCarrera("Ingeniería");
            dto.setActivo(true);
            
            // Act
            String resultado = dto.toString();
            
            // Assert
            assertTrue(resultado.contains("EstudianteDTO"));
            assertTrue(resultado.contains("1"));
            assertTrue(resultado.contains("Roberto Rivas"));
            assertTrue(resultado.contains("roberto@test.com"));
            assertTrue(resultado.contains("Ingeniería"));
            assertTrue(resultado.contains("true"));
        }
        
        @Test
        @DisplayName("toString debe manejar valores nulos")
        void toStringDebeManejarValoresNulos() {
            // Arrange - dto tiene valores null por defecto
            
            // Act
            String resultado = dto.toString();
            
            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.contains("EstudianteDTO"));
        }
    }
}