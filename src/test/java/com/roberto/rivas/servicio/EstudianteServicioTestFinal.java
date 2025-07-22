package com.roberto.rivas.servicio;

import com.roberto.rivas.repositorio.EstudianteRepositorioMemoria;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas finales del servicio para alcanzar 80% de cobertura
 * Usa implementación real sin mocks
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas Finales del Servicio - Implementación Real")
class EstudianteServicioTestFinal {

    private IEstudianteServicio servicio;

    @BeforeEach
    void configurar() {
        // Usar implementación REAL sin mocks para ejecutar TODO el código
        EstudianteRepositorioMemoria repositorio = new EstudianteRepositorioMemoria();
        servicio = new EstudianteServicioImpl(repositorio);
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de registro de estudiante")
    void debeEjecutarFlujoCompletoDeRegistroEstudiante() throws Exception {
        // Arrange
        EstudianteDTO dto = new EstudianteDTO("Roberto", "Rivas", "roberto@test.com", "Ingeniería DevOps");
        dto.setFechaIngreso(LocalDate.of(2024, 1, 15));
        dto.setActivo(true);
        
        // Act - EJECUTAR CÓDIGO REAL
        EstudianteDTO resultado = servicio.registrarEstudiante(dto);
        
        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Roberto", resultado.getNombre());
        assertEquals("Rivas", resultado.getApellido());
        assertEquals("roberto@test.com", resultado.getEmail());
        assertEquals("Ingeniería DevOps", resultado.getCarrera());
        assertTrue(resultado.isActivo());
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de búsqueda por ID")
    void debeEjecutarFlujoCompletoDeBusquedaPorId() throws Exception {
        // Arrange - registrar estudiante primero
        EstudianteDTO dto = new EstudianteDTO("Ana", "García", "ana@test.com", "Medicina");
        EstudianteDTO registrado = servicio.registrarEstudiante(dto);
        
        // Act - EJECUTAR CÓDIGO REAL de búsqueda
        var encontrado = servicio.buscarEstudiantePorId(registrado.getId());
        
        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("Ana", encontrado.get().getNombre());
        assertEquals("ana@test.com", encontrado.get().getEmail());
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de búsqueda por email")
    void debeEjecutarFlujoCompletoDeBusquedaPorEmail() throws Exception {
        // Arrange
        EstudianteDTO dto = new EstudianteDTO("Carlos", "López", "carlos@test.com", "Psicología");
        servicio.registrarEstudiante(dto);
        
        // Act - EJECUTAR CÓDIGO REAL
        var encontrado = servicio.buscarEstudiantePorEmail("carlos@test.com");
        
        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("Carlos", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de listado de estudiantes activos")
    void debeEjecutarFlujoCompletoDeListadoEstudiantesActivos() throws Exception {
        // Arrange - registrar múltiples estudiantes
        servicio.registrarEstudiante(new EstudianteDTO("Est1", "Apellido1", "est1@test.com", "Carrera1"));
        servicio.registrarEstudiante(new EstudianteDTO("Est2", "Apellido2", "est2@test.com", "Carrera2"));
        servicio.registrarEstudiante(new EstudianteDTO("Est3", "Apellido3", "est3@test.com", "Carrera3"));
        
        // Act - EJECUTAR CÓDIGO REAL
        List<EstudianteDTO> lista = servicio.listarEstudiantesActivos();
        
        // Assert
        assertEquals(3, lista.size());
        assertTrue(lista.stream().allMatch(EstudianteDTO::isActivo));
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de listado por carrera")
    void debeEjecutarFlujoCompletoDeListadoPorCarrera() throws Exception {
        // Arrange
        servicio.registrarEstudiante(new EstudianteDTO("Ana", "García", "ana@test.com", "Informática"));
        servicio.registrarEstudiante(new EstudianteDTO("Luis", "Pérez", "luis@test.com", "Informática"));
        servicio.registrarEstudiante(new EstudianteDTO("María", "López", "maria@test.com", "Medicina"));
        
        // Act - EJECUTAR CÓDIGO REAL
        List<EstudianteDTO> informatica = servicio.listarEstudiantesPorCarrera("Informática");
        List<EstudianteDTO> medicina = servicio.listarEstudiantesPorCarrera("Medicina");
        
        // Assert
        assertEquals(2, informatica.size());
        assertEquals(1, medicina.size());
        assertTrue(informatica.stream().allMatch(e -> "Informática".equals(e.getCarrera())));
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de actualización de estudiante")
    void debeEjecutarFlujoCompletoDeActualizacionEstudiante() throws Exception {
        // Arrange - registrar estudiante
        EstudianteDTO dto = new EstudianteDTO("Original", "Nombre", "original@test.com", "Carrera Original");
        EstudianteDTO registrado = servicio.registrarEstudiante(dto);
        
        // Modificar datos
        registrado.setNombre("Nombre Actualizado");
        registrado.setCarrera("Carrera Actualizada");
        registrado.setEmail("actualizado@test.com");
        
        // Act - EJECUTAR CÓDIGO REAL de actualización
        EstudianteDTO actualizado = servicio.actualizarEstudiante(registrado);
        
        // Assert
        assertEquals("Nombre Actualizado", actualizado.getNombre());
        assertEquals("Carrera Actualizada", actualizado.getCarrera());
        assertEquals("actualizado@test.com", actualizado.getEmail());
        assertEquals(registrado.getId(), actualizado.getId());
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de eliminación de estudiante")
    void debeEjecutarFlujoCompletoDeEliminacionEstudiante() throws Exception {
        // Arrange
        EstudianteDTO dto = new EstudianteDTO("Para", "Eliminar", "eliminar@test.com", "Test");
        EstudianteDTO registrado = servicio.registrarEstudiante(dto);
        
        // Verificar que está en la lista activa
        List<EstudianteDTO> antesEliminar = servicio.listarEstudiantesActivos();
        assertEquals(1, antesEliminar.size());
        
        // Act - EJECUTAR CÓDIGO REAL de eliminación
        boolean eliminado = servicio.eliminarEstudiante(registrado.getId());
        
        // Assert
        assertTrue(eliminado);
        
        // Verificar que ya no está en la lista activa
        List<EstudianteDTO> despuesEliminar = servicio.listarEstudiantesActivos();
        assertEquals(0, despuesEliminar.size());
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de reactivación de estudiante")
    void debeEjecutarFlujoCompletoDeReactivacionEstudiante() throws Exception {
        // Arrange - registrar y eliminar estudiante
        EstudianteDTO dto = new EstudianteDTO("Para", "Reactivar", "reactivar@test.com", "Test");
        EstudianteDTO registrado = servicio.registrarEstudiante(dto);
        servicio.eliminarEstudiante(registrado.getId());
        
        // Verificar que está eliminado
        assertEquals(0, servicio.listarEstudiantesActivos().size());
        
        // Act - EJECUTAR CÓDIGO REAL de reactivación
        EstudianteDTO reactivado = servicio.reactivarEstudiante(registrado.getId());
        
        // Assert
        assertNotNull(reactivado);
        assertTrue(reactivado.isActivo());
        assertEquals(1, servicio.listarEstudiantesActivos().size());
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de generación de estadísticas")
    void debeEjecutarFlujoCompletoDeGeneracionEstadisticas() throws Exception {
        // Arrange - crear datos variados
        servicio.registrarEstudiante(new EstudianteDTO("Ana", "García", "ana@test.com", "Informática"));
        servicio.registrarEstudiante(new EstudianteDTO("Luis", "Pérez", "luis@test.com", "Informática"));
        servicio.registrarEstudiante(new EstudianteDTO("María", "López", "maria@test.com", "Medicina"));
        servicio.registrarEstudiante(new EstudianteDTO("Carlos", "Ruiz", "carlos@test.com", "Psicología"));
        
        // Act - EJECUTAR CÓDIGO REAL
        Map<String, Object> estadisticas = servicio.obtenerEstadisticas();
        
        // Assert
        assertNotNull(estadisticas);
        assertEquals(4L, estadisticas.get("totalEstudiantesActivos"));
        assertTrue(estadisticas.containsKey("estudiantesPorCarrera"));
        assertTrue(estadisticas.containsKey("fechaGeneracion"));
        
        @SuppressWarnings("unchecked")
        Map<String, Long> porCarrera = (Map<String, Long>) estadisticas.get("estudiantesPorCarrera");
        assertEquals(2L, porCarrera.get("Informática"));
        assertEquals(1L, porCarrera.get("Medicina"));
        assertEquals(1L, porCarrera.get("Psicología"));
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de validación de email único")
    void debeEjecutarFlujoCompletoDeValidacionEmailUnico() throws Exception {
        // Arrange
        servicio.registrarEstudiante(new EstudianteDTO("Existente", "Usuario", "existente@test.com", "Test"));
        
        // Act - EJECUTAR CÓDIGO REAL de validación
        boolean emailNuevoEsUnico = servicio.validarEmailUnico("nuevo@test.com", null);
        boolean emailExistenteNoEsUnico = servicio.validarEmailUnico("existente@test.com", null);
        boolean emailExistentePeroMismoUsuario = servicio.validarEmailUnico("existente@test.com", 1L);
        
        // Assert
        assertTrue(emailNuevoEsUnico);
        assertFalse(emailExistenteNoEsUnico);
        assertTrue(emailExistentePeroMismoUsuario); // Es el mismo usuario
    }

    @Test
    @DisplayName("Debe ejecutar flujo COMPLETO de conteo de estudiantes activos")
    void debeEjecutarFlujoCompletoDeConteoEstudiantesActivos() throws Exception {
        // Arrange
        assertEquals(0, servicio.contarEstudiantesActivos());
        
        // Registrar estudiantes
        servicio.registrarEstudiante(new EstudianteDTO("Est1", "Test", "est1@test.com", "Test"));
        servicio.registrarEstudiante(new EstudianteDTO("Est2", "Test", "est2@test.com", "Test"));
        EstudianteDTO paraEliminar = servicio.registrarEstudiante(new EstudianteDTO("Est3", "Test", "est3@test.com", "Test"));
        
        // Act - EJECUTAR CÓDIGO REAL
        long antesEliminar = servicio.contarEstudiantesActivos();
        
        // Eliminar uno
        servicio.eliminarEstudiante(paraEliminar.getId());
        long despuesEliminar = servicio.contarEstudiantesActivos();
        
        // Assert
        assertEquals(3, antesEliminar);
        assertEquals(2, despuesEliminar);
    }

    @Test
    @DisplayName("Debe manejar TODOS los tipos de errores de validación")
    void debeManejarTodosLosTiposErroresValidacion() {
        // Error DTO nulo
        assertThrows(ServicioException.class, () -> servicio.registrarEstudiante(null));
        
        // Error ID nulo en búsqueda
        assertThrows(ServicioException.class, () -> servicio.buscarEstudiantePorId(null));
        
        // Error email nulo/vacío
        assertThrows(ServicioException.class, () -> servicio.buscarEstudiantePorEmail(null));
        assertThrows(ServicioException.class, () -> servicio.buscarEstudiantePorEmail(""));
        assertThrows(ServicioException.class, () -> servicio.buscarEstudiantePorEmail("   "));
        
        // Error carrera nula/vacía
        assertThrows(ServicioException.class, () -> servicio.listarEstudiantesPorCarrera(null));
        assertThrows(ServicioException.class, () -> servicio.listarEstudiantesPorCarrera(""));
        assertThrows(ServicioException.class, () -> servicio.listarEstudiantesPorCarrera("   "));
        
        // Error ID nulo en eliminación
        assertThrows(ServicioException.class, () -> servicio.eliminarEstudiante(null));
        
        // Error ID nulo en reactivación
        assertThrows(ServicioException.class, () -> servicio.reactivarEstudiante(null));
        
        // Error DTO inválido
        EstudianteDTO dtoInvalido = new EstudianteDTO("", "", "email-invalido", "");
        assertThrows(ServicioException.class, () -> servicio.registrarEstudiante(dtoInvalido));
    }

    @Test
    @DisplayName("Debe manejar errores de negocio correctamente")
    void debeManejarErroresNegocioCorrectamente() throws Exception {
        // Arrange - registrar estudiante
        EstudianteDTO dto = new EstudianteDTO("Test", "User", "test@test.com", "Test");
        servicio.registrarEstudiante(dto);
        
        // Error email duplicado en registro
        EstudianteDTO duplicado = new EstudianteDTO("Otro", "Usuario", "test@test.com", "Otra");
        ServicioException errorDuplicado = assertThrows(ServicioException.class, 
            () -> servicio.registrarEstudiante(duplicado));
        assertTrue(errorDuplicado.esErrorDeNegocio());
        
        // Error estudiante no existe en eliminación
        ServicioException errorNoExiste = assertThrows(ServicioException.class,
            () -> servicio.eliminarEstudiante(999L));
        assertTrue(errorNoExiste.esErrorDeNegocio());
        
        // Error estudiante no existe en reactivación
        ServicioException errorReactivar = assertThrows(ServicioException.class,
            () -> servicio.reactivarEstudiante(999L));
        assertTrue(errorReactivar.esErrorDeNegocio());
    }

    @Test
    @DisplayName("Debe ejercitar TODAS las líneas de código del servicio")
    void debeEjercitarTodasLasLineasCodigoServicio() throws Exception {
        // Este test ejecuta un flujo completo que toca TODAS las funcionalidades
        
        // 1. Registro con validaciones
        EstudianteDTO dto1 = new EstudianteDTO("Completo", "Test", "completo@test.com", "Testing");
        dto1.setFechaIngreso(LocalDate.of(2024, 6, 15));
        EstudianteDTO registrado1 = servicio.registrarEstudiante(dto1);
        
        // 2. Registro adicional para pruebas de lista
        EstudianteDTO dto2 = new EstudianteDTO("Segundo", "Test", "segundo@test.com", "Testing");
        EstudianteDTO registrado2 = servicio.registrarEstudiante(dto2);
        
        // 3. Búsquedas
        assertTrue(servicio.buscarEstudiantePorId(registrado1.getId()).isPresent());
        assertTrue(servicio.buscarEstudiantePorEmail("completo@test.com").isPresent());
        assertFalse(servicio.buscarEstudiantePorId(999L).isPresent());
        assertFalse(servicio.buscarEstudiantePorEmail("noexiste@test.com").isPresent());
        
        // 4. Listados
        assertEquals(2, servicio.listarEstudiantesActivos().size());
        assertEquals(2, servicio.listarEstudiantesPorCarrera("Testing").size());
        assertEquals(0, servicio.listarEstudiantesPorCarrera("NoExiste").size());
        
        // 5. Actualización
        registrado1.setNombre("Nombre Cambiado");
        servicio.actualizarEstudiante(registrado1);
        
        // 6. Eliminación y reactivación
        servicio.eliminarEstudiante(registrado2.getId());
        assertEquals(1, servicio.listarEstudiantesActivos().size());
        
        servicio.reactivarEstudiante(registrado2.getId());
        assertEquals(2, servicio.listarEstudiantesActivos().size());
        
        // 7. Estadísticas y conteo
        assertEquals(2, servicio.contarEstudiantesActivos());
        Map<String, Object> stats = servicio.obtenerEstadisticas();
        assertNotNull(stats);
        
        // 8. Validaciones de email único
        assertTrue(servicio.validarEmailUnico("nuevo@test.com", null));
        assertFalse(servicio.validarEmailUnico("completo@test.com", 999L));
        assertTrue(servicio.validarEmailUnico("completo@test.com", registrado1.getId()));
        
        // Verificar que todo funcionó
        assertTrue(true); // Si llega aquí, se ejecutó todo el código
    }
}