package com.roberto.rivas.servicio;

import com.roberto.rivas.modelo.Estudiante;
import com.roberto.rivas.repositorio.IEstudianteRepositorio;
import com.roberto.rivas.repositorio.RepositorioException;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas adicionales del servicio para alcanzar 80% de cobertura
 * Cubre líneas y branches no cubiertos
 * 
 * @author Roberto Rivas Lopez
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Cobertura del Servicio")
public class EstudianteServicioTestCobertura {

    @Mock
    private IEstudianteRepositorio repositorioMock;
    
    private IEstudianteServicio servicio;

    @BeforeEach
    void configurarPrueba() {
        servicio = new EstudianteServicioImpl(repositorioMock);
    }

    @Nested
    @DisplayName("Cobertura de Validaciones Específicas")
    class ValidacionesEspecificas {
        
        @Test
        @DisplayName("Debe validar DTO nulo en actualización")
        void debeValidarDTONuloEnActualizacion() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.actualizarEstudiante(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("Los datos del estudiante son requeridos", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar ID nulo en actualización")
        void debeValidarIdNuloEnActualizacion() {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
            // ID es null por defecto
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.actualizarEstudiante(dto));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("El ID es requerido", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar ID nulo en eliminación")
        void debeValidarIdNuloEnEliminacion() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.eliminarEstudiante(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("El ID es requerido", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar ID nulo en reactivación")
        void debeValidarIdNuloEnReactivacion() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.reactivarEstudiante(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("El ID es requerido", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar email nulo en búsqueda")
        void debeValidarEmailNuloEnBusqueda() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.buscarEstudiantePorEmail(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("El email es requerido", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar carrera nula en listado")
        void debeValidarCarreraNulaEnListado() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.listarEstudiantesPorCarrera(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("La carrera es requerida", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Cobertura de Conversiones de Datos")
    class ConversionDatos {
        
        @Test
        @DisplayName("Debe convertir entidad a DTO con fecha nula")
        void debeConvertirEntidadADTOConFechaNula() throws Exception {
            // Arrange
            Estudiante estudiante = new Estudiante("Test", "User", "test@test.com", "Test");
            estudiante.setId(1L);
            estudiante.setFechaIngreso(null); // Fecha nula
            
            when(repositorioMock.buscarPorId(1L)).thenReturn(Optional.of(estudiante));
            
            // Act
            Optional<EstudianteDTO> resultado = servicio.buscarEstudiantePorId(1L);
            
            // Assert
            assertTrue(resultado.isPresent());
            assertNull(resultado.get().getFechaIngreso());
        }
        
        @Test
        @DisplayName("Debe convertir DTO a entidad preservando fecha")
        void debeConvertirDTOAEntidadPreservandoFecha() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
            LocalDate fechaEspecifica = LocalDate.of(2023, 6, 15);
            dto.setFechaIngreso(fechaEspecifica);
            
            when(repositorioMock.existePorEmail(anyString())).thenReturn(false);
            when(repositorioMock.crear(any(Estudiante.class)))
                .thenAnswer(invocation -> {
                    Estudiante estudiante = invocation.getArgument(0);
                    estudiante.setId(1L);
                    return estudiante;
                });
            
            // Act
            EstudianteDTO resultado = servicio.registrarEstudiante(dto);
            
            // Assert
            assertEquals(fechaEspecifica, resultado.getFechaIngreso());
        }
    }

    @Nested
    @DisplayName("Cobertura de Manejo de Errores Específicos")
    class ManejoErroresEspecificos {
        
        @Test
        @DisplayName("Debe manejar error de repositorio en registro")
        void debeManejarErrorRepositorioEnRegistro() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
            
            when(repositorioMock.existePorEmail(anyString())).thenReturn(false);
            when(repositorioMock.crear(any(Estudiante.class)))
                .thenThrow(new RepositorioException("Error BD en crear"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.registrarEstudiante(dto));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("REGISTRO_ERROR", excepcion.getCodigoError());
            assertTrue(excepcion.getMessage().contains("Error al registrar"));
        }
        
        @Test
        @DisplayName("Debe manejar error de repositorio en búsqueda por ID")
        void debeManejarErrorRepositorioEnBusquedaPorId() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorId(1L))
                .thenThrow(new RepositorioException("Error BD búsqueda"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.buscarEstudiantePorId(1L));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("BUSQUEDA_ERROR", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe manejar error de repositorio en existencia por email")
        void debeManejarErrorRepositorioEnExistenciaPorEmail() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
            
            when(repositorioMock.existePorEmail(anyString()))
                .thenThrow(new RepositorioException("Error BD existencia"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.registrarEstudiante(dto));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("REGISTRO_ERROR", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe manejar error de repositorio en eliminación")
        void debeManejarErrorRepositorioEnEliminacion() throws Exception {
            // Arrange
            when(repositorioMock.existe(1L)).thenReturn(true);
            when(repositorioMock.eliminar(1L))
                .thenThrow(new RepositorioException("Error BD eliminación"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.eliminarEstudiante(1L));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("ELIMINACION_ERROR", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Cobertura de Validaciones de Email Único")
    class ValidacionEmailUnico {
        
        @Test
        @DisplayName("Debe fallar actualización con email duplicado")
        void debeFallarActualizacionConEmailDuplicado() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
            dto.setId(1L);
            
            when(repositorioMock.existe(1L)).thenReturn(true);
            
            // Simular que el email ya existe en otro estudiante
            Estudiante otroEstudiante = new Estudiante(2L, "Otro", "Usuario", "test@test.com", "Otra");
            when(repositorioMock.buscarPorEmail("test@test.com"))
                .thenReturn(Optional.of(otroEstudiante));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.actualizarEstudiante(dto));
            
            assertTrue(excepcion.esErrorDeNegocio());
            assertEquals("Ya existe otro estudiante con este email", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe permitir actualización con mismo email del estudiante")
        void debePermitirActualizacionConMismoEmailDelEstudiante() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
            dto.setId(1L);
            
            when(repositorioMock.existe(1L)).thenReturn(true);
            
            // El email existe pero es del mismo estudiante
            Estudiante mismoEstudiante = new Estudiante(1L, "Test", "User", "test@test.com", "Test");
            when(repositorioMock.buscarPorEmail("test@test.com"))
                .thenReturn(Optional.of(mismoEstudiante));
            
            when(repositorioMock.actualizar(any(Estudiante.class)))
                .thenReturn(mismoEstudiante);
            
            // Act & Assert - no debe lanzar excepción
            assertDoesNotThrow(() -> servicio.actualizarEstudiante(dto));
        }
    }

    @Nested
    @DisplayName("Cobertura de Casos Edge de Estadísticas")
    class CasosEdgeEstadisticas {
        
        @Test
        @DisplayName("Debe generar estadísticas con lista vacía")
        void debeGenerarEstadisticasConListaVacia() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes()).thenReturn(0L);
            when(repositorioMock.obtenerTodos()).thenReturn(Arrays.asList());
            
            // Act
            var estadisticas = servicio.obtenerEstadisticas();
            
            // Assert
            assertEquals(0L, estadisticas.get("totalEstudiantesActivos"));
            var porCarrera = (java.util.Map<?, ?>) estadisticas.get("estudiantesPorCarrera");
            assertTrue(porCarrera.isEmpty());
        }
        
        @Test
        @DisplayName("Debe generar estadísticas con estudiantes duplicados por carrera")
        void debeGenerarEstadisticasConEstudiantesDuplicadosPorCarrera() throws Exception {
            // Arrange
            List<Estudiante> estudiantes = Arrays.asList(
                new Estudiante(1L, "Ana", "García", "ana@test.com", "Informática"),
                new Estudiante(2L, "Luis", "Pérez", "luis@test.com", "Informática"),
                new Estudiante(3L, "María", "López", "maria@test.com", "Informática"),
                new Estudiante(4L, "Carlos", "Ruiz", "carlos@test.com", "Medicina"),
                new Estudiante(5L, "Sofia", "Torres", "sofia@test.com", "Medicina")
            );
            
            when(repositorioMock.contarEstudiantes()).thenReturn(5L);
            when(repositorioMock.obtenerTodos()).thenReturn(estudiantes);
            
            // Act
            var estadisticas = servicio.obtenerEstadisticas();
            
            // Assert
            assertEquals(5L, estadisticas.get("totalEstudiantesActivos"));
            var porCarrera = (java.util.Map<String, Long>) estadisticas.get("estudiantesPorCarrera");
            assertEquals(3L, porCarrera.get("Informática"));
            assertEquals(2L, porCarrera.get("Medicina"));
        }
    }

    @Nested
    @DisplayName("Cobertura de Validaciones Auxiliares")
    class ValidacionesAuxiliares {
        
        @Test
        @DisplayName("Debe validar email con espacios en búsqueda por email")
        void debeValidarEmailConEspaciosEnBusquedaPorEmail() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.buscarEstudiantePorEmail("   "));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("El email es requerido", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar carrera con espacios en listado")
        void debeValidarCarreraConEspaciosEnListado() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.listarEstudiantesPorCarrera("   "));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("La carrera es requerida", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Cobertura Constructor del Servicio")
    class ConstructorServicio {
        
        @Test
        @DisplayName("Debe lanzar excepción con repositorio nulo en constructor")
        void debeLanzarExcepcionConRepositorioNuloEnConstructor() {
            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> new EstudianteServicioImpl(null));
            
            assertEquals("El repositorio es requerido", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Cobertura de Métodos de Validación de Email")
    class MetodosValidacionEmail {
        
        @Test
        @DisplayName("Debe validar email único con repositorio fallando")
        void debeValidarEmailUnicoConRepositorioFallando() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorEmail("test@fail.com"))
                .thenThrow(new RepositorioException("Error simulado"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.validarEmailUnico("test@fail.com", null));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("VALIDACION_EMAIL_ERROR", excepcion.getCodigoError());
        }
    }
}