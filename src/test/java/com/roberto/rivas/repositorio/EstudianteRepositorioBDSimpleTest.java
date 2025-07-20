package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas simples para EstudianteRepositorioBD
 * Utiliza H2 en memoria para simular BD real
 * 
 * @author Roberto Rivas Lopez
 */
class EstudianteRepositorioBDSimpleTest {

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

    @Test
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
    @DisplayName("Crear y buscar estudiante debe funcionar correctamente")
    void testCrearYBuscarEstudiante() throws RepositorioException {
        // Arrange
        Estudiante estudiante = new Estudiante(
            "María", "González", "maria@test.com", "Psicología"
        );
        estudiante.setFechaIngreso(LocalDate.now());

        // Act
        Estudiante creado = repositorio.crear(estudiante);
        var encontrado = repositorio.buscarPorId(creado.getId());

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("María", encontrado.get().getNombre());
        assertEquals("maria@test.com", encontrado.get().getEmail());
    }
}
