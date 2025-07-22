package com.roberto.rivas.servicio;

import com.roberto.rivas.modelo.Estudiante;
import com.roberto.rivas.repositorio.IEstudianteRepositorio;
import com.roberto.rivas.repositorio.RepositorioException;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
 * Pruebas unitarias para el servicio de Estudiantes - Version Simplificada
 * Sin clases anidadas para asegurar ejecución
 * 
 * @author Roberto Rivas Lopez
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del Servicio de Estudiantes - Simplificado")
class EstudianteServicioTestSimple {

    @Mock
    private IEstudianteRepositorio repositorioMock;
    
    private IEstudianteServicio servicio;
    private EstudianteDTO estudianteDTOEjemplo;
    private Estudiante estudianteEjemplo;

    @BeforeEach
    void configurarPrueba() {
        servicio = new EstudianteServicioImpl(repositorioMock);
        
        estudianteDTOEjemplo = new EstudianteDTO("Roberto", "Rivas", 
                                               "roberto.rivas@test.com", "Ingeniería DevOps");
        
        estudianteEjemplo = new Estudiante(1L, "Roberto", "Rivas", 
                                         "roberto.rivas@test.com", "Ingeniería DevOps");
    }

    // =================== PRUEBAS DE REGISTRO ===================

    @Test
    @DisplayName("Debe registrar estudiante válido exitosamente")
    void debeRegistrarEstudianteValidoExitosamente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.existePorEmail(anyString())).thenReturn(false);
        when(repositorioMock.crear(any(Estudiante.class))).thenReturn(estudianteEjemplo);
        
        // Act
        EstudianteDTO resultado = servicio.registrarEstudiante(estudianteDTOEjemplo);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(estudianteDTOEjemplo.getNombre(), resultado.getNombre());
        assertEquals(estudianteDTOEjemplo.getEmail(), resultado.getEmail());
        assertTrue(resultado.isActivo());
        
        verify(repositorioMock, times(1)).existePorEmail(estudianteDTOEjemplo.getEmail());
        verify(repositorioMock, times(1)).crear(any(Estudiante.class));
        
        System.out.println("✅ Registro de estudiante válido funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al registrar estudiante con DTO nulo")
    void debeFallarAlRegistrarEstudianteConDTONulo() throws RepositorioException {
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.registrarEstudiante(null));
        
        assertTrue(excepcion.esErrorDeValidacion());
        assertEquals("Los datos del estudiante son requeridos", excepcion.getMessage());
        
        verify(repositorioMock, never()).crear(any(Estudiante.class));
        
        System.out.println("✅ Validación DTO nulo funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al registrar estudiante con datos inválidos")
    void debeFallarAlRegistrarEstudianteConDatosInvalidos() throws RepositorioException {
        // Arrange
        EstudianteDTO dtoInvalido = new EstudianteDTO("", "", "email-invalido", "");
        
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.registrarEstudiante(dtoInvalido));
        
        assertTrue(excepcion.esErrorDeValidacion());
        assertTrue(excepcion.getMessage().contains("Los datos del estudiante no son válidos"));
        
        verify(repositorioMock, never()).crear(any(Estudiante.class));
        
        System.out.println("✅ Validación datos inválidos funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al registrar estudiante con email duplicado")
    void debeFallarAlRegistrarEstudianteConEmailDuplicado() throws RepositorioException {
        // Arrange
        when(repositorioMock.existePorEmail(anyString())).thenReturn(true);
        
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.registrarEstudiante(estudianteDTOEjemplo));
        
        assertTrue(excepcion.esErrorDeNegocio());
        assertEquals("Ya existe un estudiante con este email", excepcion.getMessage());
        
        verify(repositorioMock, times(1)).existePorEmail(estudianteDTOEjemplo.getEmail());
        verify(repositorioMock, never()).crear(any(Estudiante.class));
        
        System.out.println("✅ Validación email duplicado funciona");
    }

    // =================== PRUEBAS DE BÚSQUEDA ===================

    @Test
    @DisplayName("Debe buscar estudiante por ID exitosamente")
    void debeBuscarEstudiantePorIdExitosamente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.buscarPorId(1L)).thenReturn(Optional.of(estudianteEjemplo));
        
        // Act
        Optional<EstudianteDTO> resultado = servicio.buscarEstudiantePorId(1L);
        
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(estudianteEjemplo.getId(), resultado.get().getId());
        assertEquals(estudianteEjemplo.getEmail(), resultado.get().getEmail());
        
        verify(repositorioMock, times(1)).buscarPorId(1L);
        
        System.out.println("✅ Búsqueda por ID funciona");
    }
    
    @Test
    @DisplayName("Debe retornar empty al buscar ID inexistente")
    void debeRetornarEmptyAlBuscarIdInexistente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.buscarPorId(999L)).thenReturn(Optional.empty());
        
        // Act
        Optional<EstudianteDTO> resultado = servicio.buscarEstudiantePorId(999L);
        
        // Assert
        assertFalse(resultado.isPresent());
        
        verify(repositorioMock, times(1)).buscarPorId(999L);
        
        System.out.println("✅ Búsqueda ID inexistente funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al buscar con ID nulo")
    void debeFallarAlBuscarConIdNulo() throws RepositorioException {
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.buscarEstudiantePorId(null));
        
        assertTrue(excepcion.esErrorDeValidacion());
        assertEquals("El ID es requerido", excepcion.getMessage());
        
        verify(repositorioMock, never()).buscarPorId(any());
        
        System.out.println("✅ Validación ID nulo funciona");
    }
    
    @Test
    @DisplayName("Debe listar estudiantes activos exitosamente")
    void debeListarEstudiantesActivosExitosamente() throws RepositorioException, ServicioException {
        // Arrange
        List<Estudiante> estudiantes = Arrays.asList(
            estudianteEjemplo,
            new Estudiante(2L, "Ana", "García", "ana@test.com", "Medicina")
        );
        when(repositorioMock.obtenerTodos()).thenReturn(estudiantes);
        
        // Act
        List<EstudianteDTO> resultado = servicio.listarEstudiantesActivos();
        
        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Roberto Rivas", resultado.get(0).getNombreCompleto());
        assertEquals("Ana García", resultado.get(1).getNombreCompleto());
        
        verify(repositorioMock, times(1)).obtenerTodos();
        
        System.out.println("✅ Listado estudiantes activos funciona");
    }

    // =================== PRUEBAS DE ACTUALIZACIÓN ===================

    @Test
    @DisplayName("Debe actualizar estudiante exitosamente")
    void debeActualizarEstudianteExitosamente() throws RepositorioException, ServicioException {
        // Arrange
        EstudianteDTO dtoActualizar = new EstudianteDTO("Roberto Carlos", "Rivas López", 
                                                      "roberto.rivas@test.com", "Ingeniería en Sistemas");
        dtoActualizar.setId(1L);
        
        Estudiante estudianteActualizado = new Estudiante(1L, "Roberto Carlos", "Rivas López",
                                                        "roberto.rivas@test.com", "Ingeniería en Sistemas");
        
        when(repositorioMock.existe(1L)).thenReturn(true);
        when(repositorioMock.buscarPorEmail(anyString())).thenReturn(Optional.empty());
        when(repositorioMock.actualizar(any(Estudiante.class))).thenReturn(estudianteActualizado);
        
        // Act
        EstudianteDTO resultado = servicio.actualizarEstudiante(dtoActualizar);
        
        // Assert
        assertEquals("Roberto Carlos", resultado.getNombre());
        assertEquals("Rivas López", resultado.getApellido());
        assertEquals("Ingeniería en Sistemas", resultado.getCarrera());
        
        verify(repositorioMock, times(1)).existe(1L);
        verify(repositorioMock, times(1)).actualizar(any(Estudiante.class));
        
        System.out.println("✅ Actualización estudiante funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al actualizar estudiante inexistente")
    void debeFallarAlActualizarEstudianteInexistente() throws RepositorioException {
        // Arrange
        EstudianteDTO dtoActualizar = new EstudianteDTO("Test", "Test", "test@test.com", "Test");
        dtoActualizar.setId(999L);
        
        when(repositorioMock.existe(999L)).thenReturn(false);
        
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.actualizarEstudiante(dtoActualizar));
        
        assertTrue(excepcion.esErrorDeNegocio());
        assertEquals("El estudiante no existe", excepcion.getMessage());
        
        verify(repositorioMock, times(1)).existe(999L);
        verify(repositorioMock, never()).actualizar(any(Estudiante.class));
        
        System.out.println("✅ Validación estudiante inexistente funciona");
    }

    // =================== PRUEBAS DE ELIMINACIÓN ===================

    @Test
    @DisplayName("Debe eliminar estudiante exitosamente")
    void debeEliminarEstudianteExitosamente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.existe(1L)).thenReturn(true);
        when(repositorioMock.eliminar(1L)).thenReturn(true);
        
        // Act
        boolean resultado = servicio.eliminarEstudiante(1L);
        
        // Assert
        assertTrue(resultado);
        
        verify(repositorioMock, times(1)).existe(1L);
        verify(repositorioMock, times(1)).eliminar(1L);
        
        System.out.println("✅ Eliminación estudiante funciona");
    }
    
    @Test
    @DisplayName("Debe fallar al eliminar estudiante inexistente")
    void debeFallarAlEliminarEstudianteInexistente() throws RepositorioException {
        // Arrange
        when(repositorioMock.existe(999L)).thenReturn(false);
        
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.eliminarEstudiante(999L));
        
        assertTrue(excepcion.esErrorDeNegocio());
        assertEquals("El estudiante no existe", excepcion.getMessage());
        
        verify(repositorioMock, times(1)).existe(999L);
        verify(repositorioMock, never()).eliminar(any());
        
        System.out.println("✅ Validación eliminación inexistente funciona");
    }

    // =================== PRUEBAS DE ESTADÍSTICAS ===================

    @Test
    @DisplayName("Debe obtener estadísticas correctamente")
    void debeObtenerEstadisticasCorrectamente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.contarEstudiantes()).thenReturn(10L);
        when(repositorioMock.obtenerTodos()).thenReturn(Arrays.asList(estudianteEjemplo));
        
        // Act
        Map<String, Object> estadisticas = servicio.obtenerEstadisticas();
        
        // Assert
        assertNotNull(estadisticas);
        assertTrue(estadisticas.containsKey("totalEstudiantesActivos"));
        assertEquals(10L, estadisticas.get("totalEstudiantesActivos"));
        
        verify(repositorioMock, times(1)).contarEstudiantes();
        
        System.out.println("✅ Estadísticas funciona");
    }

    @Test
    @DisplayName("Debe contar estudiantes activos correctamente")
    void debeContarEstudiantesActivosCorrectamente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.contarEstudiantes()).thenReturn(5L);
        
        // Act
        long resultado = servicio.contarEstudiantesActivos();
        
        // Assert
        assertEquals(5L, resultado);
        
        verify(repositorioMock, times(1)).contarEstudiantes();
        
        System.out.println("✅ Conteo estudiantes activos funciona");
    }

    @Test
    @DisplayName("Debe validar email único correctamente")
    void debeValidarEmailUnicoCorrectamente() throws RepositorioException, ServicioException {
        // Arrange
        when(repositorioMock.buscarPorEmail("nuevo@test.com")).thenReturn(Optional.empty());
        
        // Act
        boolean resultado = servicio.validarEmailUnico("nuevo@test.com", null);
        
        // Assert
        assertTrue(resultado);
        
        verify(repositorioMock, times(1)).buscarPorEmail("nuevo@test.com");
        
        System.out.println("✅ Validación email único funciona");
    }

    @Test
    @DisplayName("Debe manejar error de repositorio en registro")
    void debeManejarErrorDeRepositorioEnRegistro() throws RepositorioException {
        // Arrange
        when(repositorioMock.existePorEmail(anyString())).thenReturn(false);
        when(repositorioMock.crear(any(Estudiante.class)))
            .thenThrow(new RepositorioException("Error en base de datos"));
        
        // Act & Assert
        ServicioException excepcion = assertThrows(ServicioException.class, 
                                                 () -> servicio.registrarEstudiante(estudianteDTOEjemplo));
        
        assertTrue(excepcion.esErrorDeDatos());
        assertTrue(excepcion.getMessage().contains("Error al registrar el estudiante"));
        
        System.out.println("✅ Manejo error repositorio funciona");
    }
}