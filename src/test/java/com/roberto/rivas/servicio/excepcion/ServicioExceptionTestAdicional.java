package com.roberto.rivas.servicio.excepcion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas adicionales para ServicioException
 * Cobertura completa de todos los constructores y métodos
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas de ServicioException")
public class ServicioExceptionTestAdicional {

    @Nested
    @DisplayName("Pruebas de Constructores")
    class Constructores {
        
        @Test
        @DisplayName("Constructor con solo mensaje debe usar valores por defecto")
        void constructorConSoloMensajeDebeUsarValoresPorDefecto() {
            // Arrange & Act
            ServicioException excepcion = new ServicioException("Mensaje de prueba");
            
            // Assert
            assertEquals("Mensaje de prueba", excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.SISTEMA, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor con mensaje y tipo debe funcionar correctamente")
        void constructorConMensajeYTipoDebeFuncionarCorrectamente() {
            // Arrange & Act
            ServicioException excepcion = new ServicioException("Error de validación", 
                                                               ServicioException.TipoError.VALIDACION);
            
            // Assert
            assertEquals("Error de validación", excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.VALIDACION, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor con mensaje, código y tipo debe funcionar correctamente")
        void constructorConMensajeCodigoYTipoDebeFuncionarCorrectamente() {
            // Arrange & Act
            ServicioException excepcion = new ServicioException("Error de negocio", 
                                                               "NEGOCIO_001", 
                                                               ServicioException.TipoError.NEGOCIO);
            
            // Assert
            assertEquals("Error de negocio", excepcion.getMessage());
            assertEquals("NEGOCIO_001", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.NEGOCIO, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor con mensaje y causa debe funcionar correctamente")
        void constructorConMensajeYCausaDebeFuncionarCorrectamente() {
            // Arrange
            RuntimeException causa = new RuntimeException("Causa original");
            
            // Act
            ServicioException excepcion = new ServicioException("Error con causa", causa);
            
            // Assert
            assertEquals("Error con causa", excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.SISTEMA, excepcion.getTipoError());
            assertEquals(causa, excepcion.getCause());
        }
        
        @Test
        @DisplayName("Constructor completo debe funcionar correctamente")
        void constructorCompletoDebeFuncionarCorrectamente() {
            // Arrange
            IllegalArgumentException causa = new IllegalArgumentException("Argumento inválido");
            
            // Act
            ServicioException excepcion = new ServicioException("Error completo", 
                                                               "ERROR_COMPLETO", 
                                                               ServicioException.TipoError.DATOS, 
                                                               causa);
            
            // Assert
            assertEquals("Error completo", excepcion.getMessage());
            assertEquals("ERROR_COMPLETO", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.DATOS, excepcion.getTipoError());
            assertEquals(causa, excepcion.getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas de Métodos de Tipo de Error")
    class MetodosTipoError {
        
        @Test
        @DisplayName("esErrorDeValidacion debe retornar true para tipo VALIDACION")
        void esErrorDeValidacionDebeRetornarTrueParaTipoValidacion() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error validación", 
                                                               ServicioException.TipoError.VALIDACION);
            
            // Act & Assert
            assertTrue(excepcion.esErrorDeValidacion());
            assertFalse(excepcion.esErrorDeNegocio());
            assertFalse(excepcion.esErrorDeDatos());
            assertFalse(excepcion.esErrorDeSistema());
        }
        
        @Test
        @DisplayName("esErrorDeNegocio debe retornar true para tipo NEGOCIO")
        void esErrorDeNegocioDebeRetornarTrueParaTipoNegocio() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error negocio", 
                                                               ServicioException.TipoError.NEGOCIO);
            
            // Act & Assert
            assertFalse(excepcion.esErrorDeValidacion());
            assertTrue(excepcion.esErrorDeNegocio());
            assertFalse(excepcion.esErrorDeDatos());
            assertFalse(excepcion.esErrorDeSistema());
        }
        
        @Test
        @DisplayName("esErrorDeDatos debe retornar true para tipo DATOS")
        void esErrorDeDatosDebeRetornarTrueParaTipoDatos() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error datos", 
                                                               ServicioException.TipoError.DATOS);
            
            // Act & Assert
            assertFalse(excepcion.esErrorDeValidacion());
            assertFalse(excepcion.esErrorDeNegocio());
            assertTrue(excepcion.esErrorDeDatos());
            assertFalse(excepcion.esErrorDeSistema());
        }
        
        @Test
        @DisplayName("esErrorDeSistema debe retornar true para tipo SISTEMA")
        void esErrorDeSistemaDebeRetornarTrueParaTipoDatos() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error sistema", 
                                                               ServicioException.TipoError.SISTEMA);
            
            // Act & Assert
            assertFalse(excepcion.esErrorDeValidacion());
            assertFalse(excepcion.esErrorDeNegocio());
            assertFalse(excepcion.esErrorDeDatos());
            assertTrue(excepcion.esErrorDeSistema());
        }
    }

    @Nested
    @DisplayName("Pruebas del Método toString")
    class MetodoToString {
        
        @Test
        @DisplayName("toString debe incluir tipo, código y mensaje")
        void toStringDebeIncluirTipoCodigoYMensaje() {
            // Arrange
            ServicioException excepcion = new ServicioException("Mensaje test", 
                                                               "CODIGO_TEST", 
                                                               ServicioException.TipoError.VALIDACION);
            
            // Act
            String resultado = excepcion.toString();
            
            // Assert
            assertTrue(resultado.contains("ServicioException"));
            assertTrue(resultado.contains("VALIDACION"));
            assertTrue(resultado.contains("CODIGO_TEST"));
            assertTrue(resultado.contains("Mensaje test"));
        }
        
        @Test
        @DisplayName("toString debe manejar valores por defecto")
        void toStringDebeManejarValoresPorDefecto() {
            // Arrange
            ServicioException excepcion = new ServicioException("Solo mensaje");
            
            // Act
            String resultado = excepcion.toString();
            
            // Assert
            assertTrue(resultado.contains("ServicioException"));
            assertTrue(resultado.contains("SISTEMA"));
            assertTrue(resultado.contains("SERVICIO_ERROR"));
            assertTrue(resultado.contains("Solo mensaje"));
        }
    }

    @Nested
    @DisplayName("Pruebas de Todos los Tipos de Error")
    class TodosTiposError {
        
        @Test
        @DisplayName("Debe verificar todos los valores del enum TipoError")
        void debeVerificarTodosValoresDelEnumTipoError() {
            // Arrange & Act
            ServicioException.TipoError[] tipos = ServicioException.TipoError.values();
            
            // Assert
            assertEquals(4, tipos.length);
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.VALIDACION));
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.NEGOCIO));
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.DATOS));
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.SISTEMA));
        }
        
        @Test
        @DisplayName("Debe crear excepción para cada tipo de error")
        void debeCrearExcepcionParaCadaTipoError() {
            // Act & Assert
            for (ServicioException.TipoError tipo : ServicioException.TipoError.values()) {
                ServicioException excepcion = new ServicioException("Mensaje " + tipo, tipo);
                assertEquals(tipo, excepcion.getTipoError());
                assertTrue(excepcion.getMessage().contains(tipo.toString()));
            }
        }
    }

    @Nested
    @DisplayName("Pruebas de Getters")
    class Getters {
        
        @Test
        @DisplayName("getCodigoError debe retornar código correcto")
        void getCodigoErrorDebeRetornarCodigoCorrecto() {
            // Arrange
            ServicioException excepcion = new ServicioException("Test", "MI_CODIGO", 
                                                               ServicioException.TipoError.NEGOCIO);
            
            // Act & Assert
            assertEquals("MI_CODIGO", excepcion.getCodigoError());
        }
        
        @Test
        @DisplayName("getTipoError debe retornar tipo correcto")
        void getTipoErrorDebeRetornarTipoCorrecto() {
            // Arrange
            ServicioException excepcion = new ServicioException("Test", 
                                                               ServicioException.TipoError.DATOS);
            
            // Act & Assert
            assertEquals(ServicioException.TipoError.DATOS, excepcion.getTipoError());
        }
        
        @Test
        @DisplayName("getMessage debe heredar comportamiento de Exception")
        void getMessageDebeHeredarComportamientoDeException() {
            // Arrange
            String mensaje = "Mensaje específico de prueba";
            ServicioException excepcion = new ServicioException(mensaje);
            
            // Act & Assert
            assertEquals(mensaje, excepcion.getMessage());
        }
        
        @Test
        @DisplayName("getCause debe retornar causa cuando se proporciona")
        void getCauseDebeRetornarCausaCuandoSeProporciona() {
            // Arrange
            IllegalStateException causa = new IllegalStateException("Estado inválido");
            ServicioException excepcion = new ServicioException("Error con causa", causa);
            
            // Act & Assert
            assertEquals(causa, excepcion.getCause());
        }
    }
}