package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas COMPLETAS para EstudianteRepositorioBD
 * Objetivo: Aumentar cobertura de repositorio de 22% a 60%+
 * Estas pruebas SÍ ejecutan código real con H2
 * 
 * @author Roberto Rivas Lopez
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Completas del Repositorio BD")
class EstudianteRepositorioBDTestCompleto {

    private EstudianteRepositorioBD repositorio;
    private static final String H2_URL = "jdbc:h2:mem:testdb_completo;DB_CLOSE_DELAY=-1;MODE=MYSQL";
    private static final String H2_USER = "sa";
    private static final String H2_PASSWORD = "";

    @BeforeEach
    void setUp() throws SQLException {
        // Configurar BD H2 en memoria
        repositorio = new EstudianteRepositorioBD(H2_URL, H2_USER, H2_PASSWORD);
        inicializarBaseDatos();
    }

    @AfterEach
    void tearDown() throws SQLException {
        limpiarBaseDatos();
    }

    private void inicializarBaseDatos() throws SQLException {
        try (Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD)) {
            String createTable = """
                CREATE TABLE IF NOT EXISTS estudiantes (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    apellido VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    carrera VARCHAR(100) NOT NULL,
                    fecha_ingreso DATE NOT NULL,
                    activo BOOLEAN NOT NULL DEFAULT TRUE,
                    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
                """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTable);
            }
        }
    }

    private void limpiarBaseDatos() throws SQLException {
        try (Connection conn = DriverManager.getConnection(H2_URL, H2_USER, H2_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM estudiantes");
            stmt.execute("ALTER TABLE estudiantes ALTER COLUMN id RESTART WITH 1");
        }
    }

    @Nested
    @DisplayName("Pruebas Exhaustivas de Creación")
    class CreacionExhaustiva {

        @Test
        @Order(1)
        @DisplayName("Debe crear estudiante con todos los campos válidos")
        void debeCrearEstudianteConTodosLosCamposValidos() throws RepositorioException {
            // Arrange
            Estudiante estudiante = new Estudiante(
                "Roberto Carlos", 
                "Rivas López", 
                "roberto.rivas@devops.edu", 
                "Ingeniería DevOps"
            );
            estudiante.setFechaIngreso(LocalDate.of(2024, 1, 15));

            // Act
            Estudiante resultado = repositorio.crear(estudiante);

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.getId() > 0);
            assertEquals("Roberto Carlos", resultado.getNombre());
            assertEquals("Rivas López", resultado.getApellido());
            assertEquals("roberto.rivas@devops.edu", resultado.getEmail());
            assertEquals("Ingeniería DevOps", resultado.getCarrera());
            assertEquals(LocalDate.of(2024, 1, 15), resultado.getFechaIngreso());
            assertTrue(resultado.isActivo());
        }

        @Test
        @Order(2)
        @DisplayName("Debe rechazar estudiante con nombre nulo")
        void debeRechazarEstudianteConNombreNulo() {
            // Arrange
            Estudiante estudiante = new Estudiante();
            estudiante.setApellido("Apellido");
            estudiante.setEmail("test@test.com");
            estudiante.setCarrera("Carrera");
            estudiante.setFechaIngreso(LocalDate.now());

            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.crear(estudiante)
            );
            
            assertEquals("NOMBRE_REQUERIDO", excepcion.getCodigoError());
            assertTrue(excepcion.getMessage().contains("nombre"));
        }

        @Test
        @Order(3)
        @DisplayName("Debe rechazar estudiante con apellido vacío")
        void debeRechazarEstudianteConApellidoVacio() {
            // Arrange
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre("Nombre");
            estudiante.setEmail("test@test.com");
            estudiante.setCarrera("Carrera");
            estudiante.setFechaIngreso(LocalDate.now());

            // Act & Assert - El modelo ya valida, esperamos IllegalArgumentException
            assertThrows(IllegalArgumentException.class, 
                () -> estudiante.setApellido("   "));
        }

        @Test
        @Order(4)
        @DisplayName("Debe rechazar estudiante con email nulo")
        void debeRechazarEstudianteConEmailNulo() {
            // Arrange
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre("Nombre");
            estudiante.setApellido("Apellido");
            estudiante.setCarrera("Carrera");
            estudiante.setFechaIngreso(LocalDate.now());

            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.crear(estudiante)
            );
            
            assertEquals("EMAIL_REQUERIDO", excepcion.getCodigoError());
        }

        @Test
        @Order(5)
        @DisplayName("Debe rechazar estudiante con carrera vacía")
        void debeRechazarEstudianteConCarreraVacia() {
            // Arrange
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre("Nombre");
            estudiante.setApellido("Apellido");
            estudiante.setEmail("test@test.com");
            estudiante.setFechaIngreso(LocalDate.now());

            // Act & Assert - El modelo ya valida, esperamos IllegalArgumentException
            assertThrows(IllegalArgumentException.class, 
                () -> estudiante.setCarrera(""));
        }

        @Test
        @Order(6)
        @DisplayName("Debe rechazar estudiante nulo")
        void debeRechazarEstudianteNulo() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.crear(null)
            );
            
            assertEquals("ESTUDIANTE_NULO", excepcion.getCodigoError());
            assertTrue(excepcion.getMessage().contains("nulo"));
        }

        @Test
        @Order(7)
        @DisplayName("Debe rechazar email duplicado")
        void debeRechazarEmailDuplicado() throws RepositorioException {
            // Arrange
            Estudiante estudiante1 = crearEstudiantePrueba("Juan", "Pérez", "duplicado@test.com");
            Estudiante estudiante2 = crearEstudiantePrueba("María", "García", "duplicado@test.com");
            
            repositorio.crear(estudiante1);

            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.crear(estudiante2)
            );
            
            assertEquals("EMAIL_DUPLICADO", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas Exhaustivas de Búsqueda")
    class BusquedaExhaustiva {

        @Test
        @Order(8)
        @DisplayName("Debe buscar por ID con resultado exitoso")
        void debeBuscarPorIdConResultadoExitoso() throws RepositorioException {
            // Arrange
            Estudiante estudiante = crearEstudiantePrueba("Ana", "Martínez", "ana@test.com");
            Estudiante creado = repositorio.crear(estudiante);

            // Act
            Optional<Estudiante> resultado = repositorio.buscarPorId(creado.getId());

            // Assert
            assertTrue(resultado.isPresent());
            assertEquals(creado.getId(), resultado.get().getId());
            assertEquals("Ana", resultado.get().getNombre());
            assertEquals("ana@test.com", resultado.get().getEmail());
        }

        @Test
        @Order(9)
        @DisplayName("Debe retornar vacío para ID inexistente")
        void debeRetornarVacioParaIdInexistente() throws RepositorioException {
            // Act
            Optional<Estudiante> resultado = repositorio.buscarPorId(999L);

            // Assert
            assertFalse(resultado.isPresent());
        }

        @Test
        @Order(10)
        @DisplayName("Debe rechazar ID nulo en búsqueda")
        void debeRechazarIdNuloEnBusqueda() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.buscarPorId(null)
            );
            
            assertEquals("ID_NULO", excepcion.getCodigoError());
        }

        @Test
        @Order(11)
        @DisplayName("Debe buscar por email con resultado exitoso")
        void debeBuscarPorEmailConResultadoExitoso() throws RepositorioException {
            // Arrange
            Estudiante estudiante = crearEstudiantePrueba("Carlos", "López", "carlos.lopez@test.com");
            repositorio.crear(estudiante);

            // Act
            Optional<Estudiante> resultado = repositorio.buscarPorEmail("carlos.lopez@test.com");

            // Assert
            assertTrue(resultado.isPresent());
            assertEquals("Carlos", resultado.get().getNombre());
            assertEquals("carlos.lopez@test.com", resultado.get().getEmail());
        }

        @Test
        @Order(12)
        @DisplayName("Debe buscar email ignorando mayúsculas")
        void debeBuscarEmailIgnorandoMayusculas() throws RepositorioException {
            // Arrange
            Estudiante estudiante = crearEstudiantePrueba("Luis", "González", "luis.gonzalez@test.com");
            repositorio.crear(estudiante);

            // Act
            Optional<Estudiante> resultado = repositorio.buscarPorEmail("LUIS.GONZALEZ@TEST.COM");

            // Assert
            assertTrue(resultado.isPresent());
            assertEquals("Luis", resultado.get().getNombre());
        }

        @Test
        @Order(13)
        @DisplayName("Debe retornar vacío para email inexistente")
        void debeRetornarVacioParaEmailInexistente() throws RepositorioException {
            // Act
            Optional<Estudiante> resultado = repositorio.buscarPorEmail("noexiste@test.com");

            // Assert
            assertFalse(resultado.isPresent());
        }

        @Test
        @Order(14)
        @DisplayName("Debe rechazar email nulo en búsqueda")
        void debeRechazarEmailNuloEnBusqueda() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.buscarPorEmail(null)
            );
            
            assertEquals("EMAIL_NULO", excepcion.getCodigoError());
        }

        @Test
        @Order(15)
        @DisplayName("Debe rechazar email vacío en búsqueda")
        void debeRechazarEmailVacioEnBusqueda() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.buscarPorEmail("   ")
            );
            
            assertEquals("EMAIL_NULO", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas Exhaustivas de Listado")
    class ListadoExhaustivo {

        @Test
        @Order(16)
        @DisplayName("Debe obtener todos los estudiantes activos")
        void debeObtenerTodosLosEstudiantesActivos() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Est1", "Activo", "est1@test.com"));
            repositorio.crear(crearEstudiantePrueba("Est2", "Activo", "est2@test.com"));
            
            Estudiante inactivo = crearEstudiantePrueba("Est3", "Inactivo", "est3@test.com");
            inactivo.setActivo(false);
            repositorio.crear(inactivo);

            // Act
            List<Estudiante> resultado = repositorio.obtenerTodos();

            // Assert
            assertEquals(2, resultado.size());
            assertTrue(resultado.stream().allMatch(Estudiante::isActivo));
            assertTrue(resultado.stream().anyMatch(e -> "Est1".equals(e.getNombre())));
            assertTrue(resultado.stream().anyMatch(e -> "Est2".equals(e.getNombre())));
            assertFalse(resultado.stream().anyMatch(e -> "Est3".equals(e.getNombre())));
        }

        @Test
        @Order(17)
        @DisplayName("Debe retornar lista vacía sin estudiantes")
        void debeRetornarListaVaciaSinEstudiantes() throws RepositorioException {
            // Act
            List<Estudiante> resultado = repositorio.obtenerTodos();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @Order(18)
        @DisplayName("Debe obtener estudiantes por carrera específica")
        void debeObtenerEstudiantesPorCarreraEspecifica() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Est1", "DevOps", "est1@test.com", "Ingeniería DevOps"));
            repositorio.crear(crearEstudiantePrueba("Est2", "DevOps", "est2@test.com", "Ingeniería DevOps"));
            repositorio.crear(crearEstudiantePrueba("Est3", "Sistemas", "est3@test.com", "Ingeniería de Sistemas"));

            // Act
            List<Estudiante> resultado = repositorio.obtenerPorCarrera("Ingeniería DevOps");

            // Assert
            assertEquals(2, resultado.size());
            assertTrue(resultado.stream().allMatch(e -> "Ingeniería DevOps".equals(e.getCarrera())));
        }

        @Test
        @Order(19)
        @DisplayName("Debe buscar carrera ignorando mayúsculas")
        void debeBuscarCarreraIgnorandoMayusculas() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Test", "User", "test@test.com", "Ciencia de Datos"));

            // Act
            List<Estudiante> resultado = repositorio.obtenerPorCarrera("CIENCIA DE DATOS");

            // Assert
            assertEquals(1, resultado.size());
            assertEquals("Test", resultado.get(0).getNombre());
        }

        @Test
        @Order(20)
        @DisplayName("Debe retornar lista vacía para carrera inexistente")
        void debeRetornarListaVaciaParaCarreraInexistente() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Test", "User", "test@test.com", "Carrera Real"));

            // Act
            List<Estudiante> resultado = repositorio.obtenerPorCarrera("Carrera Inexistente");

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        @Test
        @Order(21)
        @DisplayName("Debe rechazar carrera nula")
        void debeRechazarCarreraNula() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.obtenerPorCarrera(null)
            );
            
            assertEquals("CARRERA_NULA", excepcion.getCodigoError());
        }

        @Test
        @Order(22)
        @DisplayName("Debe rechazar carrera vacía")
        void debeRechazarCarreraVacia() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.obtenerPorCarrera("   ")
            );
            
            assertEquals("CARRERA_NULA", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas Exhaustivas de Actualización")
    class ActualizacionExhaustiva {

        @Test
        @Order(23)
        @DisplayName("Debe actualizar estudiante exitosamente")
        void debeActualizarEstudianteExitosamente() throws RepositorioException {
            // Arrange
            Estudiante original = crearEstudiantePrueba("Original", "Apellido", "original@test.com");
            Estudiante creado = repositorio.crear(original);
            
            creado.setNombre("Actualizado");
            creado.setCarrera("Nueva Carrera");

            // Act
            Estudiante actualizado = repositorio.actualizar(creado);

            // Assert
            assertEquals("Actualizado", actualizado.getNombre());
            assertEquals("Nueva Carrera", actualizado.getCarrera());
            assertEquals(creado.getId(), actualizado.getId());
            
            // Verificar en BD
            Optional<Estudiante> verificacion = repositorio.buscarPorId(creado.getId());
            assertTrue(verificacion.isPresent());
            assertEquals("Actualizado", verificacion.get().getNombre());
        }

        @Test
        @Order(24)
        @DisplayName("Debe rechazar actualización de estudiante inexistente")
        void debeRechazarActualizacionDeEstudianteInexistente() {
            // Arrange
            Estudiante inexistente = crearEstudiantePrueba("No", "Existe", "noexiste@test.com");
            inexistente.setId(999L);

            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.actualizar(inexistente)
            );
            
            assertEquals("ESTUDIANTE_NO_ENCONTRADO", excepcion.getCodigoError());
        }

        @Test
        @Order(25)
        @DisplayName("Debe rechazar actualización con email duplicado")
        void debeRechazarActualizacionConEmailDuplicado() throws RepositorioException {
            // Arrange
            Estudiante est1 = repositorio.crear(crearEstudiantePrueba("Est1", "Test", "est1@test.com"));
            Estudiante est2 = repositorio.crear(crearEstudiantePrueba("Est2", "Test", "est2@test.com"));
            
            // Intentar cambiar email de est2 al email de est1
            est2.setEmail("est1@test.com");

            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.actualizar(est2)
            );
            
            assertEquals("EMAIL_DUPLICADO", excepcion.getCodigoError());
        }

        @Test
        @Order(26)
        @DisplayName("Debe rechazar actualización con estudiante nulo")
        void debeRechazarActualizacionConEstudianteNulo() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.actualizar(null)
            );
            
            assertEquals("ESTUDIANTE_NULO", excepcion.getCodigoError());
        }

        @Test
        @Order(27)
        @DisplayName("Debe rechazar actualización con ID nulo")
        void debeRechazarActualizacionConIdNulo() {
            // Arrange
            Estudiante estudiante = crearEstudiantePrueba("Test", "User", "test@test.com");
            estudiante.setId(null);

            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.actualizar(estudiante)
            );
            
            assertEquals("ID_NULO", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas Exhaustivas de Eliminación")
    class EliminacionExhaustiva {

        @Test
        @Order(28)
        @DisplayName("Debe eliminar estudiante exitosamente (lógico)")
        void debeEliminarEstudianteExitosamenteLogico() throws RepositorioException {
            // Arrange
            Estudiante estudiante = repositorio.crear(crearEstudiantePrueba("Eliminar", "Test", "eliminar@test.com"));

            // Act
            boolean resultado = repositorio.eliminar(estudiante.getId());

            // Assert
            assertTrue(resultado);
            
            // Verificar eliminación lógica - no debe aparecer en listado
            List<Estudiante> activos = repositorio.obtenerTodos();
            assertFalse(activos.stream().anyMatch(e -> e.getId().equals(estudiante.getId())));
        }

        @Test
        @Order(29)
        @DisplayName("Debe rechazar eliminación de estudiante inexistente")
        void debeRechazarEliminacionDeEstudianteInexistente() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.eliminar(999L)
            );
            
            assertEquals("ESTUDIANTE_NO_ENCONTRADO", excepcion.getCodigoError());
        }

        @Test
        @Order(30)
        @DisplayName("Debe rechazar eliminación con ID nulo")
        void debeRechazarEliminacionConIdNulo() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.eliminar(null)
            );
            
            assertEquals("ID_NULO", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas de Métodos de Existencia")
    class MetodosExistencia {

        @Test
        @Order(31)
        @DisplayName("Debe confirmar existencia por ID")
        void debeConfirmarExistenciaPorId() throws RepositorioException {
            // Arrange
            Estudiante estudiante = repositorio.crear(crearEstudiantePrueba("Existe", "Test", "existe@test.com"));

            // Act
            boolean existe = repositorio.existe(estudiante.getId());

            // Assert
            assertTrue(existe);
        }

        @Test
        @Order(32)
        @DisplayName("Debe negar existencia para ID inexistente")
        void debeNegarExistenciaParaIdInexistente() throws RepositorioException {
            // Act
            boolean existe = repositorio.existe(999L);

            // Assert
            assertFalse(existe);
        }

        @Test
        @Order(33)
        @DisplayName("Debe rechazar verificación con ID nulo")
        void debeRechazarVerificacionConIdNulo() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.existe(null)
            );
            
            assertEquals("ID_NULO", excepcion.getCodigoError());
        }

        @Test
        @Order(34)
        @DisplayName("Debe confirmar existencia por email")
        void debeConfirmarExistenciaPorEmail() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Email", "Test", "email.test@test.com"));

            // Act
            boolean existe = repositorio.existePorEmail("email.test@test.com");

            // Assert
            assertTrue(existe);
        }

        @Test
        @Order(35)
        @DisplayName("Debe verificar email ignorando mayúsculas")
        void debeVerificarEmailIgnorandoMayusculas() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Case", "Test", "case.test@test.com"));

            // Act
            boolean existe = repositorio.existePorEmail("CASE.TEST@TEST.COM");

            // Assert
            assertTrue(existe);
        }

        @Test
        @Order(36)
        @DisplayName("Debe negar existencia para email inexistente")
        void debeNegarExistenciaParaEmailInexistente() throws RepositorioException {
            // Act
            boolean existe = repositorio.existePorEmail("noexiste@test.com");

            // Assert
            assertFalse(existe);
        }

        @Test
        @Order(37)
        @DisplayName("Debe rechazar verificación con email nulo")
        void debeRechazarVerificacionConEmailNulo() {
            // Act & Assert
            RepositorioException excepcion = assertThrows(
                RepositorioException.class,
                () -> repositorio.existePorEmail(null)
            );
            
            assertEquals("EMAIL_NULO", excepcion.getCodigoError());
        }
    }

    @Nested
    @DisplayName("Pruebas de Conteo")
    class Conteo {

        @Test
        @Order(38)
        @DisplayName("Debe contar estudiantes activos correctamente")
        void debeContarEstudiantesActivosCorrectamente() throws RepositorioException {
            // Arrange
            repositorio.crear(crearEstudiantePrueba("Activo1", "Test", "activo1@test.com"));
            repositorio.crear(crearEstudiantePrueba("Activo2", "Test", "activo2@test.com"));
            
            Estudiante inactivo = crearEstudiantePrueba("Inactivo", "Test", "inactivo@test.com");
            inactivo.setActivo(false);
            repositorio.crear(inactivo);

            // Act
            long conteo = repositorio.contarEstudiantes();

            // Assert
            assertEquals(2, conteo); // Solo los activos
        }

        @Test
        @Order(39)
        @DisplayName("Debe retornar cero sin estudiantes")
        void debeRetornarCeroSinEstudiantes() throws RepositorioException {
            // Act
            long conteo = repositorio.contarEstudiantes();

            // Assert
            assertEquals(0, conteo);
        }
    }

    @Nested
    @DisplayName("Pruebas de Inicialización")
    class Inicializacion {

        @Test
        @Order(40)
        @DisplayName("Debe inicializar base de datos correctamente")
        void debeInicializarBaseDatosCorrectamente() {
            // Act & Assert
            assertDoesNotThrow(() -> {
                EstudianteRepositorioBD repo = new EstudianteRepositorioBD(H2_URL, H2_USER, H2_PASSWORD);
                repo.inicializarBaseDatos();
            });
        }

        @Test
        @Order(41)
        @DisplayName("Debe usar constructor por defecto")
        void debeUsarConstructorPorDefecto() {
            // Act & Assert
            assertDoesNotThrow(() -> {
                EstudianteRepositorioBD repo = new EstudianteRepositorioBD();
                // No ejecutamos operaciones porque usa credenciales SQLonline que pueden fallar
            });
        }
    }

    // =================== MÉTODOS AUXILIARES ===================

    private Estudiante crearEstudiantePrueba(String nombre, String apellido, String email) {
        return crearEstudiantePrueba(nombre, apellido, email, "Carrera Test");
    }

    private Estudiante crearEstudiantePrueba(String nombre, String apellido, String email, String carrera) {
        Estudiante estudiante = new Estudiante(nombre, apellido, email, carrera);
        estudiante.setFechaIngreso(LocalDate.of(2024, 1, 15));
        return estudiante;
    }
}