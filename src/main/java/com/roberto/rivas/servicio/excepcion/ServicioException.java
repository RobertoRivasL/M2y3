package com.roberto.rivas.servicio.excepcion;

/**
 * Excepción personalizada para operaciones del servicio
 * Aplica principio de Abstracción y manejo de errores de negocio
 * 
 * @author Roberto Rivas Lopez
 */
public class ServicioException extends Exception {
    
    private final String codigoError;
    private final TipoError tipoError;
    
    public enum TipoError {
        VALIDACION,
        NEGOCIO,
        DATOS,
        SISTEMA
    }
    
    public ServicioException(String mensaje) {
        super(mensaje);
        this.codigoError = "SERVICIO_ERROR";
        this.tipoError = TipoError.SISTEMA;
    }
    
    public ServicioException(String mensaje, TipoError tipoError) {
        super(mensaje);
        this.codigoError = "SERVICIO_ERROR";
        this.tipoError = tipoError;
    }
    
    public ServicioException(String mensaje, String codigoError, TipoError tipoError) {
        super(mensaje);
        this.codigoError = codigoError;
        this.tipoError = tipoError;
    }
    
    public ServicioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = "SERVICIO_ERROR";
        this.tipoError = TipoError.SISTEMA;
    }
    
    public ServicioException(String mensaje, String codigoError, TipoError tipoError, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
        this.tipoError = tipoError;
    }
    
    public String getCodigoError() {
        return codigoError;
    }
    
    public TipoError getTipoError() {
        return tipoError;
    }
    
    public boolean esErrorDeValidacion() {
        return tipoError == TipoError.VALIDACION;
    }
    
    public boolean esErrorDeNegocio() {
        return tipoError == TipoError.NEGOCIO;
    }
    
    public boolean esErrorDeDatos() {
        return tipoError == TipoError.DATOS;
    }
    
    public boolean esErrorDeSistema() {
        return tipoError == TipoError.SISTEMA;
    }
    
    @Override
    public String toString() {
        return String.format("ServicioException[tipo=%s, codigo=%s, mensaje=%s]", 
                           tipoError, codigoError, getMessage());
    }
}