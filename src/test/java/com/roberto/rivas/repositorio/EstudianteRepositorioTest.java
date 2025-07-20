package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el repositorio de Estudiantes
 * Ciclos TDD 5-8: Operaciones CRUD básicas
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas del Repositorio de Estudiantes")
public class EstudianteRepositorioTest {

    @Mock
    private IEstudianteRepositorio repositorioMock;
    
    private IEstudianteRepositorio repositorio;
    private Estudiante estudianteEjemplo;

    @BeforeEach
    void configurarPrueba() {
        MockitoAnnotations.openMocks(this);
        repositorio = new EstudianteRepositorioMemoria(); // Implementación en memoria
        estudianteEjemplo = new Estudiante("Roberto", "Rivas", 
                                         "roberto.rivas@test.com", "Ingeniería DevOps");
    }

    @Nested
    @DisplayName("Ciclo TDD 5: Operación Crear")
    class OperacionCrear {
        
        @Test
        @DisplayName("Debe crear estudiante exitosamente")
        void debeCrearEstudianteExitosamente() throws RepositorioException {
            // Act
            Estudiante estudianteCreado = repositorio.crear(estudianteEjemplo);
            
            // Assert
            assertNotNull(estudianteCreado);
            assertNotNull(estudianteCreado.getId());
            assertEquals(estudianteEjemplo.getNombre(), estudianteCreado.getNombre());
            assertEquals(estudianteEjemplo.getEmail(), estudianteCreado.getEmail());
            assertTrue(repositorio.existe(estudianteCreado.getId()));
        }
        
        @Test
        @DisplayName("Debe fallar al crear estudiante nulo")
        void debeFallarAlCrearEstudianteNulo() {
            // Act & Assert
            assertThrows(RepositorioException.class, 
                        () -> repositorio.crear(null),
                        "No se puede crear un estudiante nulo");
        }
        
        @Test
        @DisplayName("Debe fallar al crear estudiante con email duplicado")
        void debeFallarAlCrearEstudianteConEmailDuplicado() throws RepositorioException {
            // Arrange
            repositorio.crear(estudianteEjemplo);
            Estudiante estudianteDuplicado = new Estudiante("Ana", "García", 
                                                           "roberto.rivas@test.com", "Medicina");
            
            // Act & Assert
            assertThrows(RepositorioException.class, 
                        () -> repositorio.crear(estudianteDuplicado),
                        "Ya existe un estudiante con este email");
        }
        
        @Test
        @DisplayName("Debe incrementar contador al crear estudiante")
        void debeIncrementarContadorAlCrearEstudiante() throws RepositorioException {
            // Arrange
            long contadorInicial = repositorio.contarEstudiantes();
            
            // Act
            repositorio.crear(estudianteEjemplo);
            
            // Assert
            assertEquals(contadorInicial + 1, repositorio.contarEstudiantes());
        }
    }

    @Nested
    @DisplayName("Ciclo TDD 6: Operación Leer")
    class OperacionLeer {
        
        @Test
        @DisplayName("Debe buscar estudiante por ID exitosamente")
        void debeBuscarEstudiantePorIdExitosamente() throws RepositorioException {
            // Arrange
            Estudiante estudianteCreado = repositorio.crear(estudianteEjemplo);
            
            // Act
            Optional<Estudiante> encontrado = repositorio.buscarPorId(estudianteCreado.getId());
            
            // Assert
            assertTrue(encontrado.isPresent());
            assertEquals(estudianteCreado.getId(), encontrado.get().getId());
            assertEquals(estudianteCreado.getEmail(), encontrado.get().getEmail());
        }
        
        @Test
        @DisplayName("Debe retornar empty al buscar ID inexistente")
        void debeRetornarEmptyAlBuscarIdInexistente() throws RepositorioException {
            // Act
            Optional<Estudiante> encontrado = repositorio.buscarPorId(999L);
            
            // Assert
            assertFalse(encontrado.isPresent());
        }
        
        @Test
        @DisplayName("Debe buscar estudiante por email exitosamente")
        void debeBuscarEstudiantePorEmailExitosamente() throws RepositorioException {
            // Arrange
            Estudiante estudianteCreado = repositorio.crear(estudianteEjemplo);
            
            // Act
            Optional<Estudiante> encontrado = repositorio.buscarPorEmail(estudianteCreado.getEmail());
            
            // Assert
            assertTrue(encontrado.isPresent());
            assertEquals(estudianteCreado.getEmail(), encontrado.get().getEmail());
        }
        
        @Test
        @DisplayName("Debe obtener todos los estudiantes")
        void debeObtenerTodosLosEstudiantes() throws RepositorioException {
            // Arrange
            repositorio.crear(estudianteEjemplo);
            repositorio.crear(new Estudiante("Ana", "García", "ana@test.com", "Medicina"));
            
            // Act
            List<Estudiante> estudiantes = repositorio.obtenerTodos();
            
            // Assert
            assertEquals(2, estudiantes.size());
            assertTrue(estudiantes.stream().allMatch(Estudiante::isActivo));
        }
        
        @Test
        @DisplayName("Debe obtener estudiantes por carrera")
        void debeObtenerEstudiantesPorCarrera() throws RepositorioException {
            // Arrange
            repositorio.crear(estudianteEjemplo);
            repositorio.crear(new Estudiante("Ana", "García", "ana@test.com", "Ingeniería DevOps"));
            repositorio.crear(new Estudiante("Carlos", "López", "carlos@test.com", "Medicina"));
            
            // Act
            List<Estudiante> estudiantes = repositorio.obtenerPorCarrera("Ingeniería DevOps");
            
            // Assert
            assertEquals(2, estudiantes.size());
            assertTrue(estudiantes.stream()
                      .allMatch(e -> "Ingeniería DevOps".equals(e.getCarrera())));
        }
    }

    @Nested
    @DisplayName("Ciclo TDD 7: Operación Actualizar")
    class OperacionActualizar {
        
        @Test
        @DisplayName("Debe actualizar estudiante exitosamente")
        void debeActualizarEstudianteExitosamente() throws RepositorioException {
            // Arrange
            Estudiante estudianteCreado = repositorio.crear(estudianteEjemplo);
            estudianteCreado.setNombre("Roberto Carlos");
            estudianteCreado.setCarrera("Ingeniería en Sistemas");
            
            // Act
            Estudiante estudianteActualizado = repositorio.actualizar(estudianteCreado);
            
            // Assert
            assertEquals("Roberto Carlos", estudianteActualizado.getNombre());
            assertEquals("Ingeniería en Sistemas", estudianteActualizado.getCarrera());
            
            // Verificar que se actualizó en el repositorio
            Optional<Estudiante> verificacion = repositorio.buscarPorId(estudianteCreado.getId());
            assertTrue(verificacion.isPresent());
            assertEquals("Roberto Carlos", verificacion.get().getNombre());
        }
        
        @Test
        @DisplayName("Debe fallar al actualizar estudiante inexistente")
        void debeFallarAlActualizarEstudianteInexistente() {
            // Arrange
            Estudiante estudianteInexistente = new Estudiante(999L, "Test", "Test", 
                                                             "test@test.com", "Test");
            
            // Act & Assert
            assertThrows(RepositorioException.class, 
                        () -> repositorio.actualizar(estudianteInexistente),
                        "El estudiante no existe");
        }
        
        @Test
        @DisplayName("Debe fallar al actualizar con email duplicado")
        void debeFallarAlActualizarConEmailDuplicado() throws RepositorioException {
            // Arrange
            Estudiante estudiante1 = repositorio.crear(estudianteEjemplo);
            Estudiante estudiante2 = repositorio.crear(new Estudiante("Ana", "García", 
                                                                     "ana@test.com", "Medicina"));
            
            // Intentar cambiar email de estudiante2 al email de estudiante1
            estudiante2.setEmail(estudiante1.getEmail());
            
            // Act & Assert
            assertThrows(RepositorioException.class, 
                        () -> repositorio.actualizar(estudiante2),
                        "Ya existe otro estudiante con este email");
        }
    }

    @Nested
    @DisplayName("Ciclo TDD 8: Operación Eliminar")
    class OperacionEliminar {
        
        @Test
        @DisplayName("Debe eliminar estudiante exitosamente")
        void debeEliminarEstudianteExitosamente() throws RepositorioException {
            // Arrange
            Estudiante estudianteCreado = repositorio.crear(estudianteEjemplo);
            
            // Act
            boolean eliminado = repositorio.eliminar(estudianteCreado.getId());
            
            // Assert
            assertTrue(eliminado);
            
            // Verificar eliminación lógica
            Optional<Estudiante> verificacion = repositorio.buscarPorId(estudianteCreado.getId());
            assertTrue(verificacion.isPresent());
            assertFalse(verificacion.get().isActivo()); // Eliminación lógica
        }
        
        @Test
        @DisplayName("Debe fallar al eliminar estudiante inexistente")
        void debeFallarAlEliminarEstudianteInexistente() {
            // Act & Assert
            assertThrows(RepositorioException.class, 
                        () -> repositorio.eliminar(999L),
                        "El estudiante no existe");
        }
        
        @Test
        @DisplayName("No debe incluir estudiantes eliminados en listado")
        void noDebeIncluirEstudiantesEliminadosEnListado() throws RepositorioException {
            // Arrange
            Estudiante estudiante1 = repositorio.crear(estudianteEjemplo);
            Estudiante estudiante2 = repositorio.crear(new Estudiante("Ana", "García", 
                                                                     "ana@test.com", "Medicina"));
            repositorio.eliminar(estudiante1.getId());
            
            // Act
            List<Estudiante> estudiantes = repositorio.obtenerTodos();
            
            // Assert
            assertEquals(1, estudiantes.size());
            assertEquals(estudiante2.getId(), estudiantes.get(0).getId());
        }
    }

    @Nested
    @DisplayName("Pruebas con Mockito")
    class PruebasConMockito {
        
        @Test
        @DisplayName("Debe verificar llamada a crear con Mockito")
        void debeVerificarLlamadaACrearConMockito() throws RepositorioException {
            // Arrange
            when(repositorioMock.crear(any(Estudiante.class)))
                .thenReturn(new Estudiante(1L, "Roberto", "Rivas", 
                                         "roberto@test.com", "Ingeniería"));
            
            // Act
            Estudiante resultado = repositorioMock.crear(estudianteEjemplo);
            
            // Assert
            assertNotNull(resultado);
            assertEquals(1L, resultado.getId());
            verify(repositorioMock, times(1)).crear(estudianteEjemplo);
        }
        
        @Test
        @DisplayName("Debe simular excepción con Mockito")
        void debeSimularExcepcionConMockito() throws RepositorioException {
            // Arrange
            when(repositorioMock.crear(any(Estudiante.class)))
                .thenThrow(new RepositorioException("Error simulado", "MOCK_ERROR"));
            
            // Act & Assert
            RepositorioException excepcion = assertThrows(RepositorioException.class, 
                                                        () -> repositorioMock.crear(estudianteEjemplo));
            assertEquals("Error simulado", excepcion.getMessage());
            assertEquals("MOCK_ERROR", excepcion.getCodigoError());
        }
    }
}