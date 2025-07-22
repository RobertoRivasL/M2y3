package com.roberto.rivas.servicio.excepcion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas exhaustivas para ServicioException
 * Objetivo: Aumentar cobertura de excepciones de 50% a 85%+
 * VERSIÓN CORREGIDA - Sin ambigüedades de constructor
 * 
 * @author Roberto Rivas Lopez
 */
@DisplayName("Pruebas Completas de ServicioException")
class ServicioExceptionTest {

    @Nested
    @DisplayName("Pruebas de Constructores")
    class Constructores {

        @Test
        @DisplayName("Debe crear excepción con mensaje simple")
        void debeCrearExcepcionConMensajeSimple() {
            // Arrange
            String mensaje = "Error de servicio";

            // Act
            ServicioException excepcion = new ServicioException(mensaje);

            // Assert
            assertEquals(mensaje, excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.SISTEMA, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }

        @Test
        @DisplayName("Debe crear excepción con mensaje y tipo VALIDACION")
        void debeCrearExcepcionConMensajeYTipoValidacion() {
            // Arrange
            String mensaje = "Error de validación";
            ServicioException.TipoError tipo = ServicioException.TipoError.VALIDACION;

            // Act
            ServicioException excepcion = new ServicioException(mensaje, tipo);

            // Assert
            assertEquals(mensaje, excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(tipo, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }

        @Test
        @DisplayName("Debe crear excepción con mensaje y tipo NEGOCIO")
        void debeCrearExcepcionConMensajeYTipoNegocio() {
            // Arrange
            String mensaje = "Error de negocio";
            ServicioException.TipoError tipo = ServicioException.TipoError.NEGOCIO;

            // Act
            ServicioException excepcion = new ServicioException(mensaje, tipo);

            // Assert
            assertEquals(mensaje, excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(tipo, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }

        @Test
        @DisplayName("Debe crear excepción con mensaje, código y tipo")
        void debeCrearExcepcionConMensajeCodigoYTipo() {
            // Arrange
            String mensaje = "Error específico";
            String codigo = "ERROR_ESPECIFICO";
            ServicioException.TipoError tipo = ServicioException.TipoError.NEGOCIO;

            // Act
            ServicioException excepcion = new ServicioException(mensaje, codigo, tipo);

            // Assert
            assertEquals(mensaje, excepcion.getMessage());
            assertEquals(codigo, excepcion.getCodigoError());
            assertEquals(tipo, excepcion.getTipoError());
            assertNull(excepcion.getCause());
        }

        @Test
        @DisplayName("Debe crear excepción con mensaje y causa")
        void debeCrearExcepcionConMensajeYCausa() {
            // Arrange
            String mensaje = "Error con causa";
            RuntimeException causa = new RuntimeException("Causa original");

            // Act
            ServicioException excepcion = new ServicioException(mensaje, causa);

            // Assert
            assertEquals(mensaje, excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.SISTEMA, excepcion.getTipoError());
            assertEquals(causa, excepcion.getCause());
        }

        @Test
        @DisplayName("Debe crear excepción con todos los parámetros")
        void debeCrearExcepcionConTodosLosParametros() {
            // Arrange
            String mensaje = "Error completo";
            String codigo = "ERROR_COMPLETO";
            ServicioException.TipoError tipo = ServicioException.TipoError.DATOS;
            IllegalArgumentException causa = new IllegalArgumentException("Argumento inválido");

            // Act
            ServicioException excepcion = new ServicioException(mensaje, codigo, tipo, causa);

            // Assert
            assertEquals(mensaje, excepcion.getMessage());
            assertEquals(codigo, excepcion.getCodigoError());
            assertEquals(tipo, excepcion.getTipoError());
            assertEquals(causa, excepcion.getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas de Tipos de Error")
    class TiposDeError {

        @Test
        @DisplayName("Debe identificar error de validación")
        void debeIdentificarErrorDeValidacion() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error validación", 
                                                               "VALIDACION_ERROR",
                                                               ServicioException.TipoError.VALIDACION);

            // Act & Assert
            assertTrue(excepcion.esErrorDeValidacion());
            assertFalse(excepcion.esErrorDeNegocio());
            assertFalse(excepcion.esErrorDeDatos());
            assertFalse(excepcion.esErrorDeSistema());
        }

        @Test
        @DisplayName("Debe identificar error de negocio")
        void debeIdentificarErrorDeNegocio() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error negocio", 
                                                               "NEGOCIO_ERROR",
                                                               ServicioException.TipoError.NEGOCIO);

            // Act & Assert
            assertFalse(excepcion.esErrorDeValidacion());
            assertTrue(excepcion.esErrorDeNegocio());
            assertFalse(excepcion.esErrorDeDatos());
            assertFalse(excepcion.esErrorDeSistema());
        }

        @Test
        @DisplayName("Debe identificar error de datos")
        void debeIdentificarErrorDeDatos() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error datos", 
                                                               "DATOS_ERROR",
                                                               ServicioException.TipoError.DATOS);

            // Act & Assert
            assertFalse(excepcion.esErrorDeValidacion());
            assertFalse(excepcion.esErrorDeNegocio());
            assertTrue(excepcion.esErrorDeDatos());
            assertFalse(excepcion.esErrorDeSistema());
        }

        @Test
        @DisplayName("Debe identificar error de sistema")
        void debeIdentificarErrorDeSistema() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error sistema", 
                                                               "SISTEMA_ERROR",
                                                               ServicioException.TipoError.SISTEMA);

            // Act & Assert
            assertFalse(excepcion.esErrorDeValidacion());
            assertFalse(excepcion.esErrorDeNegocio());
            assertFalse(excepcion.esErrorDeDatos());
            assertTrue(excepcion.esErrorDeSistema());
        }

        @Test
        @DisplayName("Debe usar tipo SISTEMA por defecto")
        void debeUsarTipoSistemaPorDefecto() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error sin tipo");

            // Act & Assert
            assertEquals(ServicioException.TipoError.SISTEMA, excepcion.getTipoError());
            assertTrue(excepcion.esErrorDeSistema());
        }
    }

    @Nested
    @DisplayName("Pruebas de Getters")
    class Getters {

        @Test
        @DisplayName("Debe obtener código de error personalizado")
        void debeObtenerCodigoDeErrorPersonalizado() {
            // Arrange
            String codigoPersonalizado = "VALIDACION_EMAIL";
            ServicioException excepcion = new ServicioException("Error email", 
                                                               codigoPersonalizado, 
                                                               ServicioException.TipoError.VALIDACION);

            // Act & Assert
            assertEquals(codigoPersonalizado, excepcion.getCodigoError());
        }

        @Test
        @DisplayName("Debe obtener código de error por defecto")
        void debeObtenerCodigoDeErrorPorDefecto() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error genérico");

            // Act & Assert
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
        }

        @Test
        @DisplayName("Debe obtener tipo de error específico")
        void debeObtenerTipoDeErrorEspecifico() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error negocio", 
                                                               "NEGOCIO_ERROR", 
                                                               ServicioException.TipoError.NEGOCIO);

            // Act & Assert
            assertEquals(ServicioException.TipoError.NEGOCIO, excepcion.getTipoError());
        }
    }

    @Nested
    @DisplayName("Pruebas de toString")
    class ToString {

        @Test
        @DisplayName("Debe generar toString con información completa")
        void debeGenerarToStringConInformacionCompleta() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error completo", 
                                                               "ERROR_COMPLETO", 
                                                               ServicioException.TipoError.VALIDACION);

            // Act
            String resultado = excepcion.toString();

            // Assert
            assertTrue(resultado.contains("ServicioException"));
            assertTrue(resultado.contains("tipo=VALIDACION"));
            assertTrue(resultado.contains("codigo=ERROR_COMPLETO"));
            assertTrue(resultado.contains("mensaje=Error completo"));
        }

        @Test
        @DisplayName("Debe generar toString con valores por defecto")
        void debeGenerarToStringConValoresPorDefecto() {
            // Arrange
            ServicioException excepcion = new ServicioException("Error simple");

            // Act
            String resultado = excepcion.toString();

            // Assert
            assertTrue(resultado.contains("ServicioException"));
            assertTrue(resultado.contains("tipo=SISTEMA"));
            assertTrue(resultado.contains("codigo=SERVICIO_ERROR"));
            assertTrue(resultado.contains("mensaje=Error simple"));
        }

        @Test
        @DisplayName("Debe incluir todos los tipos en toString")
        void debeIncluirTodosLosTiposEnToString() {
            // Arrange & Act
            String toStringValidacion = new ServicioException("", "VAL", ServicioException.TipoError.VALIDACION).toString();
            String toStringNegocio = new ServicioException("", "NEG", ServicioException.TipoError.NEGOCIO).toString();
            String toStringDatos = new ServicioException("", "DAT", ServicioException.TipoError.DATOS).toString();
            String toStringSistema = new ServicioException("", "SIS", ServicioException.TipoError.SISTEMA).toString();

            // Assert
            assertTrue(toStringValidacion.contains("tipo=VALIDACION"));
            assertTrue(toStringNegocio.contains("tipo=NEGOCIO"));
            assertTrue(toStringDatos.contains("tipo=DATOS"));
            assertTrue(toStringSistema.contains("tipo=SISTEMA"));
        }
    }

    @Nested
    @DisplayName("Pruebas de Casos Específicos")
    class CasosEspecificos {

        @Test
        @DisplayName("Debe manejar mensaje nulo")
        void debeManejarMensajeNulo() {
            // Act
            ServicioException excepcion = new ServicioException(null);

            // Assert
            assertNull(excepcion.getMessage());
            assertEquals("SERVICIO_ERROR", excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.SISTEMA, excepcion.getTipoError());
        }

        @Test
        @DisplayName("Debe manejar código nulo")
        void debeManejarCodigoNulo() {
            // Act
            ServicioException excepcion = new ServicioException("Mensaje", 
                                                               null, 
                                                               ServicioException.TipoError.VALIDACION);

            // Assert
            assertEquals("Mensaje", excepcion.getMessage());
            assertNull(excepcion.getCodigoError());
            assertEquals(ServicioException.TipoError.VALIDACION, excepcion.getTipoError());
        }

        @Test
        @DisplayName("Debe manejar tipo nulo")
        void debeManejarTipoNulo() {
            // Act
            ServicioException excepcion = new ServicioException("Mensaje", 
                                                               "CODIGO", 
                                                               null);

            // Assert
            assertEquals("Mensaje", excepcion.getMessage());
            assertEquals("CODIGO", excepcion.getCodigoError());
            assertNull(excepcion.getTipoError());
        }

        @Test
        @DisplayName("Debe manejar causa nula")
        void debeManejarCausaNula() {
            // Act
            ServicioException excepcion = new ServicioException("Mensaje", (Throwable) null);

            // Assert
            assertEquals("Mensaje", excepcion.getMessage());
            assertNull(excepcion.getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas de Herencia de Exception")
    class HerenciaException {

        @Test
        @DisplayName("Debe ser instancia de Exception")
        void debeSerInstanciaDeException() {
            // Arrange
            ServicioException excepcion = new ServicioException("Test");

            // Act & Assert
            assertTrue(excepcion instanceof Exception);
            assertTrue(excepcion instanceof Throwable);
        }

        @Test
        @DisplayName("Debe preservar mensaje en herencia")
        void debePreservarMensajeEnHerencia() {
            // Arrange
            String mensaje = "Mensaje de prueba";
            ServicioException excepcion = new ServicioException(mensaje);

            // Act
            Exception exception = excepcion;

            // Assert
            assertEquals(mensaje, exception.getMessage());
        }

        @Test
        @DisplayName("Debe preservar causa en herencia")
        void debePreservarCausaEnHerencia() {
            // Arrange
            RuntimeException causa = new RuntimeException("Causa original");
            ServicioException excepcion = new ServicioException("Mensaje", causa);

            // Act
            Exception exception = excepcion;

            // Assert
            assertEquals(causa, exception.getCause());
        }
    }

    @Nested
    @DisplayName("Pruebas de Enumeración TipoError")
    class EnumeracionTipoError {

        @Test
        @DisplayName("Debe tener todos los tipos definidos")
        void debeTenerTodosLosTiposDefinidos() {
            // Act
            ServicioException.TipoError[] tipos = ServicioException.TipoError.values();

            // Assert
            assertEquals(4, tipos.length);
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.VALIDACION));
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.NEGOCIO));
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.DATOS));
            assertTrue(java.util.Arrays.asList(tipos).contains(ServicioException.TipoError.SISTEMA));
        }

        @Test
        @DisplayName("Debe obtener tipo por nombre")
        void debeObtenerTipoPorNombre() {
            // Act & Assert
            assertEquals(ServicioException.TipoError.VALIDACION, 
                        ServicioException.TipoError.valueOf("VALIDACION"));
            assertEquals(ServicioException.TipoError.NEGOCIO, 
                        ServicioException.TipoError.valueOf("NEGOCIO"));
            assertEquals(ServicioException.TipoError.DATOS, 
                        ServicioException.TipoError.valueOf("DATOS"));
            assertEquals(ServicioException.TipoError.SISTEMA, 
                        ServicioException.TipoError.valueOf("SISTEMA"));
        }
    }

    @Nested
    @DisplayName("Casos de Uso Reales")
    class CasosDeUsoReales {

        @Test
        @DisplayName("Debe crear excepción típica de validación")
        void debeCrearExcepcionTipicaDeValidacion() {
            // Act
            ServicioException excepcion = new ServicioException(
                "El email no puede estar vacío", 
                "EMAIL_VACIO", 
                ServicioException.TipoError.VALIDACION
            );

            // Assert
            assertTrue(excepcion.esErrorDeValidacion());
            assertEquals("EMAIL_VACIO", excepcion.getCodigoError());
            assertTrue(excepcion.toString().contains("VALIDACION"));
        }

        @Test
        @DisplayName("Debe crear excepción típica de negocio")
        void debeCrearExcepcionTipicaDeNegocio() {
            // Act
            ServicioException excepcion = new ServicioException(
                "Ya existe un estudiante con este email", 
                "EMAIL_DUPLICADO", 
                ServicioException.TipoError.NEGOCIO
            );

            // Assert
            assertTrue(excepcion.esErrorDeNegocio());
            assertEquals("EMAIL_DUPLICADO", excepcion.getCodigoError());
        }

        @Test
        @DisplayName("Debe crear excepción típica de datos con causa")
        void debeCrearExcepcionTipicaDeDatosConCausa() {
            // Arrange
            RuntimeException causaConexion = new RuntimeException("Connection timeout");

            // Act
            ServicioException excepcion = new ServicioException(
                "Error al acceder a la base de datos", 
                "BD_ERROR", 
                ServicioException.TipoError.DATOS,
                causaConexion
            );

            // Assert
            assertTrue(excepcion.esErrorDeDatos());
            assertEquals("BD_ERROR", excepcion.getCodigoError());
            assertEquals(causaConexion, excepcion.getCause());
        }
    }
}