package com.roberto.rivas.repositorio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas completas para RepositorioException
 * Cobertura de todos los constructores y métodos
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas de RepositorioException")
public class RepositorioExceptionTest {

    @Nested
    @DisplayName("Pruebas de Constructores")
    class Constructores {
        
        @Test
        @DisplayName("Constructor con solo mensaje debe usar código por defecto")
        void constructorConSoloMensajeDebeUsarCodigoPorDefecto() {
            // Arrange & Act
            RepositorioException excepcion = new RepositorioException("Mensaje de prueba");
            
            // Assert
            assertEquals("Mensaje de prueba", excepcion.getMessage());
            assertEquals("REPO_ERROR", excepcion.getCodigoError());
            assertNull(excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor con mensaje y código debe funcionar correctamente")
        void constructorConMensajeYCodigoDebeFuncionarCorrectamente() {
            // Arrange & Act
            RepositorioException excepcion = new RepositorioException("Error específico", "CODIGO_ESPECIFICO");
            
            // Assert
            assertEquals("Error específico", excepcion.getMessage());
            assertEquals("CODIGO_ESPECIFICO", excepcion.getCodigoError());
            assertNull(excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor con mensaje y causa debe funcionar correctamente")
        void constructorConMensajeYCausaDebeFuncionarCorrectamente() {
            // Arrange
            RuntimeException causa = new RuntimeException("Causa original");
            
            // Act
            RepositorioException excepcion = new RepositorioException("Error con causa", causa);
            
            // Assert
            assertEquals("Error con causa", excepcion.getMessage());
            assertEquals("REPO_ERROR", excepcion.getCodigoError());
            assertEquals(causa, excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor completo debe funcionar correctamente")
        void constructorCompletoDebeFuncionarCorrectamente() {
            // Arrange
            IllegalStateException causa = new IllegalStateException("Estado inválido");
            
            // Act
            RepositorioException excepcion = new RepositorioException("Error completo", 
                                                                    "ERROR_COMPLETO", 
                                                                    causa);
            
            // Assert
            assertEquals("Error completo", excepcion.getMessage());
            assertEquals("ERROR_COMPLETO", excepcion.getCodigoError());
            assertEquals(causa, excepcion.getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas de Getters")
    class Getters {
        
        @Test
        @DisplayName("getCodigoError debe retornar código correcto")
        void getCodigoErrorDebeRetornarCodigoCorrecto() {
            // Arrange
            RepositorioException excepcion = new RepositorioException("Test", "MI_CODIGO");
            
            // Act & Assert
            assertEquals("MI_CODIGO", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("getMessage debe heredar comportamiento de Exception")
        void getMessageDebeHeredarComportamientoDeException() {
            // Arrange
            String mensaje = "Mensaje específico de repositorio";
            RepositorioException excepcion = new RepositorioException(mensaje);
            
            // Act & Assert
            assertEquals(mensaje, excepcion.getMessage());
        }
        
        @Test
        @DisplayName("getCause debe retornar causa cuando se proporciona")
        void getCauseDebeRetornarCausaCuandoSeProporciona() {
            // Arrange
            SQLException causa = new SQLException("Error de SQL", "08001");
            RepositorioException excepcion = new RepositorioException("Error de repositorio", causa);
            
            // Act & Assert
            assertEquals(causa, excepcion.getCause());
        }
        
        @Test
        @DisplayName("getCause debe retornar null cuando no se proporciona")
        void getCauseDebeRetornarNullCuandoNoSeProporciona() {
            // Arrange
            RepositorioException excepcion = new RepositorioException("Sin causa");
            
            // Act & Assert
            assertNull(excepcion.getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas del Método toString")
    class MetodoToString {
        
        @Test
        @DisplayName("toString debe incluir código y mensaje")
        void toStringDebeIncluirCodigoYMensaje() {
            // Arrange
            RepositorioException excepcion = new RepositorioException("Mensaje test", "CODIGO_TEST");
            
            // Act
            String resultado = excepcion.toString();
            
            // Assert
            assertTrue(resultado.contains("RepositorioException"));
            assertTrue(resultado.contains("CODIGO_TEST"));
            assertTrue(resultado.contains("Mensaje test"));
        }
        
        @Test
        @DisplayName("toString debe manejar código por defecto")
        void toStringDebeManejarCodigoPorDefecto() {
            // Arrange
            RepositorioException excepcion = new RepositorioException("Solo mensaje");
            
            // Act
            String resultado = excepcion.toString();
            
            // Assert
            assertTrue(resultado.contains("RepositorioException"));
            assertTrue(resultado.contains("REPO_ERROR"));
            assertTrue(resultado.contains("Solo mensaje"));
        }
        
        @Test
        @DisplayName("toString debe formatear correctamente")
        void toStringDebeFormatearCorrectamente() {
            // Arrange
            RepositorioException excepcion = new RepositorioException("Error de formato", "FORMAT_ERROR");
            
            // Act
            String resultado = excepcion.toString();
            
            // Assert
            String esperado = "RepositorioException[codigo=FORMAT_ERROR, mensaje=Error de formato]";
            assertEquals(esperado, resultado);
        }
    }

    @Nested
    @DisplayName("Pruebas de Herencia de Exception")
    class HerenciaException {
        
        @Test
        @DisplayName("Debe ser instancia de Exception")
        void debeSerInstanciaDeException() {
            // Arrange
            RepositorioException excepcion = new RepositorioException("Test");
            
            // Act & Assert
            assertTrue(excepcion instanceof Exception);
            assertTrue(excepcion instanceof Throwable);
        }
        
        @Test
        @DisplayName("Debe mantener stack trace")
        void debeMantenerStackTrace() {
            // Arrange & Act
            RepositorioException excepcion = new RepositorioException("Test stack trace");
            
            // Assert
            assertNotNull(excepcion.getStackTrace());
            assertTrue(excepcion.getStackTrace().length > 0);
        }
        
        @Test
        @DisplayName("Debe propagarse correctamente en catch")
        void debePropaggarseCorrectamenteEnCatch() {
            // Arrange & Act & Assert
            assertThrows(RepositorioException.class, () -> {
                throw new RepositorioException("Excepción de prueba", "TEST_ERROR");
            });
        }
    }

    @Nested
    @DisplayName("Pruebas de Casos de Uso Reales")
    class CasosUsoReales {
        
        @Test
        @DisplayName("Simulación de error de conexión de BD")
        void simulacionErrorConexionBD() {
            // Arrange
            SQLException causaSQL = new SQLException("No se pudo conectar a la base de datos", "08001");
            
            // Act
            RepositorioException excepcion = new RepositorioException(
                "Error al conectar con la base de datos", 
                "DB_CONNECTION_ERROR", 
                causaSQL
            );
            
            // Assert
            assertEquals("Error al conectar con la base de datos", excepcion.getMessage());
            assertEquals("DB_CONNECTION_ERROR", excepcion.getCodigoError());
            assertEquals(causaSQL, excepcion.getCause());
            assertTrue(excepcion.getCause() instanceof SQLException);
        }
        
        @Test
        @DisplayName("Simulación de error de validación")
        void simulacionErrorValidacion() {
            // Act
            RepositorioException excepcion = new RepositorioException(
                "Los datos proporcionados no son válidos", 
                "VALIDATION_ERROR"
            );
            
            // Assert
            assertEquals("Los datos proporcionados no son válidos", excepcion.getMessage());
            assertEquals("VALIDATION_ERROR", excepcion.getCodigoError());
            assertNull(excepcion.getCause());
        }
        
        @Test
        @DisplayName("Simulación de error de duplicado")
        void simulacionErrorDuplicado() {
            // Act
            RepositorioException excepcion = new RepositorioException(
                "Ya existe un registro con los mismos datos"
            );
            
            // Assert
            assertEquals("Ya existe un registro con los mismos datos", excepcion.getMessage());
            assertEquals("REPO_ERROR", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("Simulación de error con múltiples causas anidadas")
        void simulacionErrorMultiplesCausasAnidadas() {
            // Arrange
            IllegalArgumentException causaRaiz = new IllegalArgumentException("Argumento inválido");
            RuntimeException causaIntermedia = new RuntimeException("Error intermedio", causaRaiz);
            
            // Act
            RepositorioException excepcion = new RepositorioException(
                "Error en operación del repositorio", 
                "OPERATION_ERROR", 
                causaIntermedia
            );
            
            // Assert
            assertEquals("Error en operación del repositorio", excepcion.getMessage());
            assertEquals("OPERATION_ERROR", excepcion.getCodigoError());
            assertEquals(causaIntermedia, excepcion.getCause());
            assertEquals(causaRaiz, excepcion.getCause().getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas de Códigos de Error Específicos")
    class CodigosErrorEspecificos {
        
        @Test
        @DisplayName("Debe manejar códigos de error nulos")
        void debeManejarCodigosErrorNulos() {
            // Arrange & Act
            RepositorioException excepcion = new RepositorioException("Test", (String) null);
            
            // Assert
            assertNull(excepcion.getCodigoError());
            assertEquals("Test", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe manejar códigos de error vacíos")
        void debeManejarCodigosErrorVacios() {
            // Arrange & Act
            RepositorioException excepcion = new RepositorioException("Test", "");
            
            // Assert
            assertEquals("", excepcion.getCodigoError());
            assertEquals("Test", excepcion.getMessage());
        }
        
        @Test
        @DisplayName("Debe preservar códigos de error con espacios")
        void debePreservarCodigosErrorConEspacios() {
            // Arrange & Act
            RepositorioException excepcion = new RepositorioException("Test", " CODIGO_CON_ESPACIOS ");
            
            // Assert
            assertEquals(" CODIGO_CON_ESPACIOS ", excepcion.getCodigoError());
        }
    }

    /**
     * Clase interna para simular SQLException sin depender de java.sql
     */
    private static class SQLException extends Exception {
        private final String sqlState;
        
        public SQLException(String message, String sqlState) {
            super(message);
            this.sqlState = sqlState;
        }
        
        public String getSQLState() {
            return sqlState;
        }
    }
}