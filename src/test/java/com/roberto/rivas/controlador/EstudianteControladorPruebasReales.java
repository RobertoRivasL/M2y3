package com.roberto.rivas.controlador;

import com.roberto.rivas.repositorio.EstudianteRepositorioMemoria;
import com.roberto.rivas.servicio.EstudianteServicioImpl;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas que ejecutan código real del controlador sin loops infinitos
 * Usa reflexión para acceder a métodos privados y mejorar cobertura
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas Reales del Controlador (Cobertura)")
class EstudianteControladorPruebasReales {

    private EstudianteControlador controlador;
    private EstudianteServicioImpl servicio;
    private EstudianteRepositorioMemoria repositorio;
    private ByteArrayOutputStream salidaCapturada;
    private PrintStream salidaOriginal;

    @BeforeEach
    void setUp() {
        // Configurar captura de salida
        salidaOriginal = System.out;
        salidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salidaCapturada));
        
        // Crear componentes reales
        repositorio = new EstudianteRepositorioMemoria();
        servicio = new EstudianteServicioImpl(repositorio);
        controlador = new EstudianteControlador(false);
    }

    @Test
    @DisplayName("Debe procesar opción 1 - Registrar estudiante")
    void debeProcesarOpcion1RegistrarEstudiante() throws Exception {
        // Simular entrada para registrar estudiante
        String entrada = "Roberto\nRivas\nroberto@test.com\nDevOps\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        // Usar reflexión para acceder al método privado procesarOpcion
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        // Act - Ejecutar opción 1 (registrar estudiante)
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 1);
        });
        
        // Assert - Verificar que se mostró mensaje de registro
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("REGISTRAR") || salida.contains("estudiante"));
    }

    @Test
    @DisplayName("Debe procesar opción 2 - Buscar por ID")
    void debeProcesarOpcion2BuscarPorId() throws Exception {
        // Simular entrada para buscar por ID
        String entrada = "1\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        // Usar reflexión para acceder al método privado
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        // Act
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 2);
        });
        
        // Assert
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("BUSCAR") || salida.contains("ID"));
    }

    @Test
    @DisplayName("Debe procesar opción 3 - Buscar por email")
    void debeProcesarOpcion3BuscarPorEmail() throws Exception {
        // Simular entrada para buscar por email
        String entrada = "test@test.com\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 3);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("BUSCAR") || salida.contains("email"));
    }

    @Test
    @DisplayName("Debe procesar opción 4 - Listar todos")
    void debeProcesarOpcion4ListarTodos() throws Exception {
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 4);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("LISTA") || salida.contains("estudiantes"));
    }

    @Test
    @DisplayName("Debe procesar opción 5 - Listar por carrera")
    void debeProcesarOpcion5ListarPorCarrera() throws Exception {
        String entrada = "DevOps\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 5);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("carrera") || salida.contains("CARRERA"));
    }

    @Test
    @DisplayName("Debe procesar opción 9 - Estadísticas")
    void debeProcesarOpcion9Estadisticas() throws Exception {
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 9);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("ESTADÍSTICAS") || salida.contains("estadísticas"));
    }

    @Test
    @DisplayName("Debe procesar opción 10 - Demostración completa")
    void debeProcesarOpcion10DemostracionCompleta() throws Exception {
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 10);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("DEMOSTRACIÓN") || salida.contains("TDD"));
    }

    @Test
    @DisplayName("Debe manejar opción inválida")
    void debeManejarOpcionInvalida() throws Exception {
        Method procesarOpcion = EstudianteControlador.class.getDeclaredMethod("procesarOpcion", int.class);
        procesarOpcion.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            procesarOpcion.invoke(controlador, 999);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("válida") || salida.contains("opción"));
    }

    @Test
    @DisplayName("Debe mostrar menú principal")
    void debeMostrarMenuPrincipal() throws Exception {
        Method mostrarMenu = EstudianteControlador.class.getDeclaredMethod("mostrarMenu");
        mostrarMenu.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            mostrarMenu.invoke(controlador);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("MENÚ") || salida.contains("opción"));
    }

    @Test
    @DisplayName("Debe mostrar bienvenida")
    void debeMostrarBienvenida() throws Exception {
        Method mostrarBienvenida = EstudianteControlador.class.getDeclaredMethod("mostrarBienvenida");
        mostrarBienvenida.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            mostrarBienvenida.invoke(controlador);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("CRUD") && salida.contains("TDD"));
    }

    @Test
    @DisplayName("Debe mostrar detalles de estudiante")
    void debeMostrarDetallesDeEstudiante() throws Exception {
        // Crear estudiante de prueba
        EstudianteDTO estudiante = new EstudianteDTO(1L, "Roberto", "Rivas", 
                "roberto@test.com", "DevOps", LocalDate.now(), true);
        
        Method mostrarDetalles = EstudianteControlador.class.getDeclaredMethod("mostrarDetallesEstudiante", EstudianteDTO.class);
        mostrarDetalles.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            mostrarDetalles.invoke(controlador, estudiante);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("Roberto") && salida.contains("DevOps"));
    }

    @Test
    @DisplayName("Debe mostrar resumen de estudiante")
    void debeMostrarResumenDeEstudiante() throws Exception {
        EstudianteDTO estudiante = new EstudianteDTO(1L, "Ana", "García", 
                "ana@test.com", "Medicina", LocalDate.now(), true);
        
        Method mostrarResumen = EstudianteControlador.class.getDeclaredMethod("mostrarResumenEstudiante", EstudianteDTO.class);
        mostrarResumen.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            mostrarResumen.invoke(controlador, estudiante);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("Ana") && salida.contains("Medicina"));
    }

    @Test
    @DisplayName("Debe leer opción numérica")
    void debeLeerOpcionNumerica() throws Exception {
        String entrada = "5\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        Method leerOpcion = EstudianteControlador.class.getDeclaredMethod("leerOpcion");
        leerOpcion.setAccessible(true);
        
        int resultado = (int) leerOpcion.invoke(controlador);
        assertEquals(5, resultado);
    }

    @Test
    @DisplayName("Debe manejar entrada no numérica")
    void debeManejarEntradaNoNumerica() throws Exception {
        String entrada = "abc\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        Method leerOpcion = EstudianteControlador.class.getDeclaredMethod("leerOpcion");
        leerOpcion.setAccessible(true);
        
        int resultado = (int) leerOpcion.invoke(controlador);
        assertEquals(-1, resultado); // Valor por defecto para entrada inválida
    }

    @Test
    @DisplayName("Debe leer Long válido")
    void debeLeerLongValido() throws Exception {
        String entrada = "123\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        Method leerLong = EstudianteControlador.class.getDeclaredMethod("leerLong");
        leerLong.setAccessible(true);
        
        Long resultado = (Long) leerLong.invoke(controlador);
        assertEquals(123L, resultado);
    }

    @Test
    @DisplayName("Debe manejar Long inválido")
    void debeManejarLongInvalido() throws Exception {
        String entrada = "abc\n";
        System.setIn(new ByteArrayInputStream(entrada.getBytes()));
        
        Method leerLong = EstudianteControlador.class.getDeclaredMethod("leerLong");
        leerLong.setAccessible(true);
        
        Long resultado = (Long) leerLong.invoke(controlador);
        assertEquals(0L, resultado);
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("número válido"));
    }

    @Test
    @DisplayName("Debe manejar error de servicio")
    void debeManejarErrorDeServicio() throws Exception {
        ServicioException excepcion = new ServicioException("Error de prueba", ServicioException.TipoError.VALIDACION);
        
        Method manejarError = EstudianteControlador.class.getDeclaredMethod("manejarErrorServicio", ServicioException.class);
        manejarError.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            manejarError.invoke(controlador, excepcion);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("Error") && salida.contains("validación"));
    }

    @Test
    @DisplayName("Debe manejar error general")
    void debeManejarErrorGeneral() throws Exception {
        Exception excepcion = new RuntimeException("Error general");
        
        Method manejarError = EstudianteControlador.class.getDeclaredMethod("manejarError", String.class, Exception.class);
        manejarError.setAccessible(true);
        
        assertDoesNotThrow(() -> {
            manejarError.invoke(controlador, "Mensaje de error", excepcion);
        });
        
        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("Mensaje de error"));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(salidaOriginal);
        System.setIn(System.in);
    }
}