package com.roberto.rivas.repositorio;

/**
 * Excepción personalizada para operaciones del repositorio
 * Aplica principio de Abstracción y manejo de errores
 * 
 * @author Roberto Rivas Lopez
 */
public class RepositorioException extends Exception {
    
    private final String codigoError;
    
    public RepositorioException(String mensaje) {
        super(mensaje);
        this.codigoError = "REPO_ERROR";
    }
    
    public RepositorioException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
    }
    
    public RepositorioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = "REPO_ERROR";
    }
    
    public RepositorioException(String mensaje, String codigoError, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
    }
    
    public String getCodigoError() {
        return codigoError;
    }
    
    @Override
    public String toString() {
        return String.format("RepositorioException[codigo=%s, mensaje=%s]", 
                           codigoError, getMessage());
    }
}