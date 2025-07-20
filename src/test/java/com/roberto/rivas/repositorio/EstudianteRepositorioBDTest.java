package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.j        // Test con nombre vacío
        Estudiante sinNombre = new Estudiante("", "Apellido", "test@email.com", "Carrera");
        sinNombre.setFechaIngreso(LocalDate.now());
        assertThrows(RepositorioException.class, () -> repositorio.crear(sinNombre));

        // Test con email inválido
        Estudiante emailInvalido = new Estudiante("Nombre", "Apellido", "email-invalido", "Carrera");
        emailInvalido.setFechaIngreso(LocalDate.now());
        assertThrows(RepositorioException.class, () -> repositorio.crear(emailInvalido));

        // Test con datos válidos pero con algún problema de edad (esto se maneja en el modelo)
        Estudiante estudianteValido = new Estudiante("Nombre", "Apellido", "test@email.com", "Carrera");
        estudianteValido.setFechaIngreso(LocalDate.now());
        // En lugar de probar edad inválida, probar otro caso de validación
        assertDoesNotThrow(() -> repositorio.crear(estudianteValido));pi.Assertions.*;

/**
 * Pruebas para EstudianteRepositorioBD
 * Utiliza H2 en memoria para simular BD real
 * 
 * @author Roberto Rivas Lopez
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EstudianteRepositorioBDTest {

    private EstudianteRepositorioBD repositorio;
    private static final String H2_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL";
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

    // =================== PRUEBAS DE CREACIÓN ===================

    @Test
    @Order(1)
    @DisplayName("Crear estudiante válido debe retornar estudiante con ID")
    void testCrearEstudianteValido() throws RepositorioException {
        // Arrange
        Estudiante estudiante = new Estudiante(
            "Juan Carlos", 
            "Pérez López", 
            "juan.perez@test.com", 
            "Ingeniería Informática"
        );
        estudiante.setFechaIngreso(LocalDate.of(2024, 1, 15));

        // Act
        Estudiante resultado = repositorio.crear(estudiante);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getId() > 0);
        assertEquals("Juan Carlos", resultado.getNombre());
        assertEquals("juan.perez@test.com", resultado.getEmail());
        assertEquals("Ingeniería Informática", resultado.getCarrera());
        assertTrue(resultado.isActivo());
    }

    @Test
    @Order(2)
    @DisplayName("Crear estudiante con email duplicado debe lanzar excepción")
    void testCrearEstudianteEmailDuplicado() throws RepositorioException {
        // Arrange
        Estudiante estudiante1 = new Estudiante(
            "Juan", "Pérez", "test@duplicate.com", "Informática"
        );
        estudiante1.setFechaIngreso(LocalDate.now());
        
        Estudiante estudiante2 = new Estudiante(
            "María", "González", "test@duplicate.com", "Psicología"
        );
        estudiante2.setFechaIngreso(LocalDate.now());

        // Act & Assert
        assertDoesNotThrow(() -> repositorio.crear(estudiante1));
        
        RepositorioException exception = assertThrows(
            RepositorioException.class,
            () -> repositorio.crear(estudiante2)
        );
        
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    @Order(3)
    @DisplayName("Crear estudiante nulo debe lanzar excepción")
    void testCrearEstudianteNulo() {
        // Act & Assert
        assertThrows(RepositorioException.class, () -> repositorio.crear(null));
    }

    // =================== PRUEBAS DE BÚSQUEDA ===================

    @Test
    @Order(4)
    @DisplayName("Buscar por ID existente debe retornar estudiante")
    void testBuscarPorIdExistente() throws RepositorioException {
        // Arrange
        Estudiante estudiante = crearEstudiantePrueba("María", "González", "maria@test.com");
        Estudiante creado = repositorio.crear(estudiante);

        // Act
        Optional<Estudiante> resultado = repositorio.buscarPorId(creado.getId());

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("María", resultado.get().getNombre());
        assertEquals("maria@test.com", resultado.get().getEmail());
    }

    @Test
    @Order(5)
    @DisplayName("Buscar por ID inexistente debe retornar Optional vacío")
    void testBuscarPorIdInexistente() throws RepositorioException {
        // Act
        Optional<Estudiante> resultado = repositorio.buscarPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @Order(6)
    @DisplayName("Buscar por email existente debe retornar estudiante")
    void testBuscarPorEmailExistente() throws RepositorioException {
        // Arrange
        Estudiante estudiante = crearEstudiantePrueba("Carlos", "López", "carlos@test.com");
        repositorio.crear(estudiante);

        // Act
        Optional<Estudiante> resultado = repositorio.buscarPorEmail("carlos@test.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Carlos", resultado.get().getNombre());
    }

    @Test
    @Order(7)
    @DisplayName("Buscar por email inexistente debe retornar Optional vacío")
    void testBuscarPorEmailInexistente() throws RepositorioException {
        // Act
        Optional<Estudiante> resultado = repositorio.buscarPorEmail("noexiste@test.com");

        // Assert
        assertFalse(resultado.isPresent());
    }

    // =================== PRUEBAS DE LISTADO ===================

    @Test
    @Order(8)
    @DisplayName("Listar todos con estudiantes debe retornar lista no vacía")
    void testListarTodosConEstudiantes() throws RepositorioException {
        // Arrange
        repositorio.crear(crearEstudiantePrueba("Ana", "Martínez", "ana@test.com"));
        repositorio.crear(crearEstudiantePrueba("Luis", "Rodríguez", "luis@test.com"));

        // Act
        List<Estudiante> resultado = repositorio.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    @Order(9)
    @DisplayName("Listar todos sin estudiantes debe retornar lista vacía")
    void testListarTodosSinEstudiantes() throws RepositorioException {
        // Act
        List<Estudiante> resultado = repositorio.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("Obtener todos debe retornar solo estudiantes activos")
    void testObtenerTodosActivos() throws RepositorioException {
        // Arrange
        Estudiante activo = crearEstudiantePrueba("Pedro", "Activo", "activo@test.com");
        Estudiante inactivo = crearEstudiantePrueba("Pablo", "Inactivo", "inactivo@test.com");
        inactivo.setActivo(false);

        repositorio.crear(activo);
        repositorio.crear(inactivo);

        // Act
        List<Estudiante> resultado = repositorio.obtenerTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
        assertEquals("Pedro", resultado.get(0).getNombre());
    }

    // =================== PRUEBAS DE ACTUALIZACIÓN ===================

    @Test
    @Order(11)
    @DisplayName("Actualizar estudiante existente debe modificar datos")
    void testActualizarEstudianteExistente() throws RepositorioException {
        // Arrange
        Estudiante original = crearEstudiantePrueba("Roberto", "Original", "original@test.com");
        Estudiante creado = repositorio.crear(original);

        creado.setNombre("Roberto Actualizado");
        creado.setCarrera("Nueva Carrera");
        creado.setEdad(25);

        // Act
        Estudiante actualizado = repositorio.actualizar(creado);

        // Assert
        assertNotNull(actualizado);
        assertEquals("Roberto Actualizado", actualizado.getNombre());
        assertEquals("Nueva Carrera", actualizado.getCarrera());
        assertEquals(25, actualizado.getEdad());
        assertEquals(creado.getId(), actualizado.getId());
    }

    @Test
    @Order(12)
    @DisplayName("Actualizar estudiante inexistente debe lanzar excepción")
    void testActualizarEstudianteInexistente() {
        // Arrange
        Estudiante estudiante = crearEstudiantePrueba("No", "Existe", "noexiste@test.com");
        estudiante.setId(999L);

        // Act & Assert
        assertThrows(RepositorioException.class, () -> repositorio.actualizar(estudiante));
    }

    // =================== PRUEBAS DE ELIMINACIÓN ===================

    @Test
    @Order(13)
    @DisplayName("Eliminar estudiante existente debe retornar true")
    void testEliminarEstudianteExistente() throws RepositorioException {
        // Arrange
        Estudiante estudiante = crearEstudiantePrueba("Eliminar", "Test", "eliminar@test.com");
        Estudiante creado = repositorio.crear(estudiante);

        // Act
        boolean resultado = repositorio.eliminar(creado.getId());

        // Assert
        assertTrue(resultado);
        
        // Verificar que ya no existe
        Optional<Estudiante> verificacion = repositorio.buscarPorId(creado.getId());
        assertFalse(verificacion.isPresent());
    }

    @Test
    @Order(14)
    @DisplayName("Eliminar estudiante inexistente debe retornar false")
    void testEliminarEstudianteInexistente() throws RepositorioException {
        // Act
        boolean resultado = repositorio.eliminar(999L);

        // Assert
        assertFalse(resultado);
    }

    // =================== PRUEBAS DE BÚSQUEDA POR CARRERA ===================

    @Test
    @Order(15)
    @DisplayName("Buscar por carrera existente debe retornar lista con estudiantes")
    void testBuscarPorCarreraExistente() throws RepositorioException {
        // Arrange
        repositorio.crear(crearEstudiantePrueba("Est1", "Inf1", "est1@test.com", "Informática"));
        repositorio.crear(crearEstudiantePrueba("Est2", "Inf2", "est2@test.com", "Informática"));
        repositorio.crear(crearEstudiantePrueba("Est3", "Psi1", "est3@test.com", "Psicología"));

        // Act
        List<Estudiante> resultado = repositorio.obtenerPorCarrera("Informática");

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(est -> assertEquals("Informática", est.getCarrera()));
    }

    @Test
    @Order(16)
    @DisplayName("Buscar por carrera inexistente debe retornar lista vacía")
    void testBuscarPorCarreraInexistente() throws RepositorioException {
        // Act
        List<Estudiante> resultado = repositorio.obtenerPorCarrera("CarreraInexistente");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // =================== PRUEBAS DE VALIDACIÓN ===================

    @Test
    @Order(17)
    @DisplayName("Crear estudiante con datos inválidos debe lanzar excepción")
    void testCrearEstudianteDatosInvalidos() {
        // Test con nombre vacío
        Estudiante sinNombre = new Estudiante("", "Apellido", "test@email.com", "Carrera");
        sinNombre.setFechaIngreso(LocalDate.now());
        assertThrows(RepositorioException.class, () -> repositorio.crear(sinNombre));

        // Test con email inválido
        Estudiante emailInvalido = new Estudiante("Nombre", "Apellido", "email-invalido", "Carrera");
        emailInvalido.setFechaIngreso(LocalDate.now());
        assertThrows(RepositorioException.class, () -> repositorio.crear(emailInvalido));

        // Test con datos válidos
        Estudiante estudianteValido = new Estudiante("Nombre", "Apellido", "test@email.com", "Carrera");
        estudianteValido.setFechaIngreso(LocalDate.now());
        assertDoesNotThrow(() -> repositorio.crear(estudianteValido));
    }

    // =================== PRUEBAS DE EXISTENCIA ===================

    @Test
    @Order(18)
    @DisplayName("Existe por ID debe retornar true para estudiante existente")
    void testExistePorIdExistente() throws RepositorioException {
        // Arrange
        Estudiante estudiante = crearEstudiantePrueba("Existe", "Test", "existe@test.com");
        Estudiante creado = repositorio.crear(estudiante);

        // Act
        boolean resultado = repositorio.existe(creado.getId());

        // Assert
        assertTrue(resultado);
    }

    @Test
    @Order(19)
    @DisplayName("Existe por ID debe retornar false para estudiante inexistente")
    void testExistePorIdInexistente() throws RepositorioException {
        // Act
        boolean resultado = repositorio.existe(999L);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @Order(20)
    @DisplayName("Existe por email debe retornar true para email existente")
    void testExistePorEmailExistente() throws RepositorioException {
        // Arrange
        Estudiante estudiante = crearEstudiantePrueba("Email", "Existe", "email.existe@test.com");
        repositorio.crear(estudiante);

        // Act
        boolean resultado = repositorio.existePorEmail("email.existe@test.com");

        // Assert
        assertTrue(resultado);
    }

    @Test
    @Order(21)
    @DisplayName("Existe por email debe retornar false para email inexistente")
    void testExistePorEmailInexistente() throws RepositorioException {
        // Act
        boolean resultado = repositorio.existePorEmail("noexiste@test.com");

        // Assert
        assertFalse(resultado);
    }

    // =================== PRUEBAS DE CONTEO ===================

    @Test
    @Order(22)
    @DisplayName("Contar estudiantes con datos debe retornar número correcto")
    void testContarEstudiantesConDatos() throws RepositorioException {
        // Arrange
        repositorio.crear(crearEstudiantePrueba("Uno", "Test", "uno@test.com"));
        repositorio.crear(crearEstudiantePrueba("Dos", "Test", "dos@test.com"));
        repositorio.crear(crearEstudiantePrueba("Tres", "Test", "tres@test.com"));

        // Act
        long resultado = repositorio.contarEstudiantes();

        // Assert
        assertEquals(3, resultado);
    }

    @Test
    @Order(23)
    @DisplayName("Contar estudiantes sin datos debe retornar cero")
    void testContarEstudiantesSinDatos() throws RepositorioException {
        // Act
        long resultado = repositorio.contarEstudiantes();

        // Assert
        assertEquals(0, resultado);
    }

    // =================== MÉTODOS AUXILIARES ===================

    private Estudiante crearEstudiantePrueba(String nombre, String apellido, String email) {
        return crearEstudiantePrueba(nombre, apellido, email, "Ingeniería Informática");
    }

    private Estudiante crearEstudiantePrueba(String nombre, String apellido, String email, String carrera) {
        Estudiante estudiante = new Estudiante(nombre, apellido, email, carrera);
        estudiante.setFechaIngreso(LocalDate.of(2024, 1, 15));
        return estudiante;
    }
}
