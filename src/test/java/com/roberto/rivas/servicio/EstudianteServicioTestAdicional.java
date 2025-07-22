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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas adicionales del servicio de Estudiantes
 * Casos edge y cobertura adicional para alcanzar 80%
 * 
 * @author Roberto Rivas Lopez
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Adicionales del Servicio de Estudiantes")
public class EstudianteServicioTestAdicional {

    @Mock
    private IEstudianteRepositorio repositorioMock;
    
    private IEstudianteServicio servicio;

    @BeforeEach
    void configurarPrueba() {
        servicio = new EstudianteServicioImpl(repositorioMock);
    }

    @Nested
    @DisplayName("Pruebas de Validación de Email Único")
    class ValidacionEmailUnico {
        
        @Test
        @DisplayName("Debe retornar true para email único")
        void debeRetornarTrueParaEmailUnico() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorEmail("nuevo@test.com")).thenReturn(Optional.empty());
            
            // Act
            boolean resultado = servicio.validarEmailUnico("nuevo@test.com", null);
            
            // Assert
            assertTrue(resultado);
            verify(repositorioMock).buscarPorEmail("nuevo@test.com");
        }
        
        @Test
        @DisplayName("Debe retornar false para email duplicado")
        void debeRetornarFalseParaEmailDuplicado() throws Exception {
            // Arrange
            Estudiante estudianteExistente = new Estudiante(1L, "Juan", "Pérez", 
                                                           "existente@test.com", "Ingeniería");
            when(repositorioMock.buscarPorEmail("existente@test.com"))
                .thenReturn(Optional.of(estudianteExistente));
            
            // Act
            boolean resultado = servicio.validarEmailUnico("existente@test.com", 2L);
            
            // Assert
            assertFalse(resultado);
            verify(repositorioMock).buscarPorEmail("existente@test.com");
        }
        
        @Test
        @DisplayName("Debe retornar true para mismo estudiante en actualización")
        void debeRetornarTrueParaMismoEstudianteEnActualizacion() throws Exception {
            // Arrange
            Estudiante estudianteExistente = new Estudiante(1L, "Juan", "Pérez", 
                                                           "mismo@test.com", "Ingeniería");
            when(repositorioMock.buscarPorEmail("mismo@test.com"))
                .thenReturn(Optional.of(estudianteExistente));
            
            // Act
            boolean resultado = servicio.validarEmailUnico("mismo@test.com", 1L);
            
            // Assert
            assertTrue(resultado); // Es el mismo estudiante
            verify(repositorioMock).buscarPorEmail("mismo@test.com");
        }
        
        @Test
        @DisplayName("Debe lanzar excepción con email vacío")
        void debeLanzarExcepcionConEmailVacio() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.validarEmailUnico("", null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("EMAIL_VACIO", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe manejar excepción del repositorio")
        void debeManejarExcepcionDelRepositorio() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorEmail(anyString()))
                .thenThrow(new RepositorioException("Error BD"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.validarEmailUnico("test@test.com", null));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertTrue(excepcion.getMessage().contains("validar email único"));
        }
    }

    @Nested
    @DisplayName("Pruebas de Conteo de Estudiantes")
    class ConteoEstudiantes {
        
        @Test
        @DisplayName("Debe contar estudiantes activos correctamente")
        void debeContarEstudiantesActivosCorrectamente() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes()).thenReturn(5L);
            
            // Act
            long resultado = servicio.contarEstudiantesActivos();
            
            // Assert
            assertEquals(5L, resultado);
            verify(repositorioMock).contarEstudiantes();
        }
        
        @Test
        @DisplayName("Debe manejar excepción en conteo")
        void debeManejarExcepcionEnConteo() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes())
                .thenThrow(new RepositorioException("Error conteo"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.contarEstudiantesActivos());
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("CONTEO_ERROR", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas de Estadísticas Avanzadas")
    class EstadisticasAvanzadas {
        
        @Test
        @DisplayName("Debe generar estadísticas con estudiantes por carrera")
        void debeGenerarEstadisticasConEstudiantesPorCarrera() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes()).thenReturn(3L);
            
            List<Estudiante> estudiantes = Arrays.asList(
                new Estudiante(1L, "Ana", "García", "ana@test.com", "Informática"),
                new Estudiante(2L, "Luis", "Pérez", "luis@test.com", "Informática"),
                new Estudiante(3L, "María", "López", "maria@test.com", "Medicina")
            );
            
            when(repositorioMock.obtenerTodos()).thenReturn(estudiantes);
            
            // Act
            Map<String, Object> estadisticas = servicio.obtenerEstadisticas();
            
            // Assert
            assertNotNull(estadisticas);
            assertEquals(3L, estadisticas.get("totalEstudiantesActivos"));
            assertTrue(estadisticas.containsKey("estudiantesPorCarrera"));
            assertTrue(estadisticas.containsKey("fechaGeneracion"));
            
            @SuppressWarnings("unchecked")
            Map<String, Long> porCarrera = (Map<String, Long>) estadisticas.get("estudiantesPorCarrera");
            assertEquals(2L, porCarrera.get("Informática"));
            assertEquals(1L, porCarrera.get("Medicina"));
            
            verify(repositorioMock).contarEstudiantes();
            verify(repositorioMock).obtenerTodos();
        }
        
        @Test
        @DisplayName("Debe manejar estadísticas sin estudiantes")
        void debeManejarEstadisticasSinEstudiantes() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes()).thenReturn(0L);
            when(repositorioMock.obtenerTodos()).thenReturn(Arrays.asList());
            
            // Act
            Map<String, Object> estadisticas = servicio.obtenerEstadisticas();
            
            // Assert
            assertNotNull(estadisticas);
            assertEquals(0L, estadisticas.get("totalEstudiantesActivos"));
            
            @SuppressWarnings("unchecked")
            Map<String, Long> porCarrera = (Map<String, Long>) estadisticas.get("estudiantesPorCarrera");
            assertTrue(porCarrera.isEmpty());
        }
    }

    @Nested
    @DisplayName("Pruebas de Validaciones Estrictas")
    class ValidacionesEstrictas {
        
        @Test
        @DisplayName("Debe validar DTO nulo en registro")
        void debeValidarDTONuloEnRegistro() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.registrarEstudiante(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("DTO_NULO", excepcion.getCodigoError());
            assertEquals("Los datos del estudiante son requeridos", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe validar ID nulo en búsqueda")
        void debeValidarIdNuloEnBusqueda() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.buscarEstudiantePorId(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("ID_NULO", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe validar email vacío en búsqueda por email")
        void debeValidarEmailVacioEnBusquedaPorEmail() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.buscarEstudiantePorEmail(""));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("EMAIL_VACIO", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe validar carrera vacía en listado por carrera")
        void debeValidarCarreraVaciaEnListadoPorCarrera() {
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.listarEstudiantesPorCarrera(null));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("CARRERA_VACIA", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe validar datos incompletos en DTO")
        void debeValidarDatosIncompletosEnDTO() {
            // Arrange
            EstudianteDTO dtoInvalido = new EstudianteDTO("", "", "", "");
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.registrarEstudiante(dtoInvalido));
            
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("DATOS_INVALIDOS", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas de Manejo de Errores del Repositorio")
    class ManejoErroresRepositorio {
        
        @Test
        @DisplayName("Debe manejar error en búsqueda por email")
        void debeManejarErrorEnBusquedaPorEmail() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorEmail(anyString()))
                .thenThrow(new RepositorioException("Error BD búsqueda"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.buscarEstudiantePorEmail("test@test.com"));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("BUSQUEDA_EMAIL_ERROR", excepcion.getCodigoError());
            assertTrue(excepcion.getMessage().contains("buscar el estudiante por email"));
        }
        
        @Test
        @DisplayName("Debe manejar error en listado general")
        void debeManejarErrorEnListadoGeneral() throws Exception {
            // Arrange
            when(repositorioMock.obtenerTodos())
                .thenThrow(new RepositorioException("Error BD listado"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.listarEstudiantesActivos());
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("LISTADO_ERROR", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe manejar error en listado por carrera")
        void debeManejarErrorEnListadoPorCarrera() throws Exception {
            // Arrange
            when(repositorioMock.obtenerPorCarrera(anyString()))
                .thenThrow(new RepositorioException("Error BD carrera"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.listarEstudiantesPorCarrera("Informática"));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("LISTADO_CARRERA_ERROR", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe manejar error en reactivación")
        void debeManejarErrorEnReactivacion() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorId(anyLong()))
                .thenThrow(new RepositorioException("Error BD reactivación"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.reactivarEstudiante(1L));
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("REACTIVACION_ERROR", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas de Conversión y Mapeo")
    class ConversionMapeo {
        
        @Test
        @DisplayName("Debe convertir DTO a entidad correctamente")
        void debeConvertirDTOAEntidadCorrectamente() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "Usuario", "test@test.com", "Test");
            dto.setId(1L);
            dto.setFechaIngreso(LocalDate.of(2024, 1, 15));
            dto.setActivo(true);
            
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
            assertNotNull(resultado);
            assertEquals("Test", resultado.getNombre());
            assertEquals("Usuario", resultado.getApellido());
            assertEquals("test@test.com", resultado.getEmail());
            assertEquals("Test", resultado.getCarrera());
            assertTrue(resultado.isActivo());
        }
        
        @Test
        @DisplayName("Debe manejar fecha de ingreso nula en conversión")
        void debeManejarFechaIngresoNulaEnConversion() throws Exception {
            // Arrange
            EstudianteDTO dto = new EstudianteDTO("Test", "Usuario", "test@test.com", "Test");
            dto.setFechaIngreso(null); // Fecha nula
            
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
            assertNotNull(resultado);
            // La fecha debe ser la fecha actual por defecto
            assertNotNull(resultado.getFechaIngreso());
        }
    }

    @Nested
    @DisplayName("Pruebas de Constructor del Servicio")
    class ConstructorServicio {
        
        @Test
        @DisplayName("Debe lanzar excepción con repositorio nulo")
        void debeLanzarExcepcionConRepositorioNulo() {
            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class,
                () -> new EstudianteServicioImpl(null));
            
            assertEquals("El repositorio es requerido", excepcion.getMessage());
        }
    }

    @Nested
    @DisplayName("Pruebas de Casos Edge Complejos")
    class CasosEdgeComplejos {
        
        @Test
        @DisplayName("Debe manejar reactivación de estudiante inexistente")
        void debeManejarReactivacionEstudianteInexistente() throws Exception {
            // Arrange
            when(repositorioMock.buscarPorId(999L)).thenReturn(Optional.empty());
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.reactivarEstudiante(999L));
            
            assertTrue(excepcion.esErrorDeNegocio());
            assertEquals("ESTUDIANTE_NO_ENCONTRADO", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe procesar reactivación exitosa")
        void debeProcesarReactivacionExitosa() throws Exception {
            // Arrange
            Estudiante estudianteInactivo = new Estudiante(1L, "Test", "Reactivar", 
                                                          "reactivar@test.com", "Test");
            estudianteInactivo.desactivar();
            
            when(repositorioMock.buscarPorId(1L)).thenReturn(Optional.of(estudianteInactivo));
            when(repositorioMock.actualizar(any(Estudiante.class)))
                .thenAnswer(invocation -> {
                    Estudiante estudiante = invocation.getArgument(0);
                    estudiante.activar();
                    return estudiante;
                });
            
            // Act
            EstudianteDTO resultado = servicio.reactivarEstudiante(1L);
            
            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isActivo());
            verify(repositorioMock).buscarPorId(1L);
            verify(repositorioMock).actualizar(any(Estudiante.class));
        }
        
        @Test
        @DisplayName("Debe manejar estadísticas con error en conteo")
        void debeManejarEstadisticasConErrorEnConteo() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes())
                .thenThrow(new RepositorioException("Error en conteo"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.obtenerEstadisticas());
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("ESTADISTICAS_ERROR", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Debe manejar estadísticas con error en listado")
        void debeManejarEstadisticasConErrorEnListado() throws Exception {
            // Arrange
            when(repositorioMock.contarEstudiantes()).thenReturn(5L);
            when(repositorioMock.obtenerTodos())
                .thenThrow(new RepositorioException("Error en listado"));
            
            // Act & Assert
            ServicioException excepcion = assertThrows(ServicioException.class,
                () -> servicio.obtenerEstadisticas());
            
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("ESTADISTICAS_ERROR", excepcion.getCodigoError());
        }
    }
}