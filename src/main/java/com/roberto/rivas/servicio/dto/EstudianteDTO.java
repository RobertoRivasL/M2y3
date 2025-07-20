package com.roberto.rivas.servicio.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Data Transfer Object para Estudiante
 * Aplica principio de Separación de Intereses
 * Representa datos para transferencia entre capas
 * 
 * @author Roberto Rivas Lopez
 */
public class EstudianteDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String carrera;
    private LocalDate fechaIngreso;
    private boolean activo;
    private String nombreCompleto;

    // Constructor por defecto
    public EstudianteDTO() {
    }

    // Constructor completo
    public EstudianteDTO(Long id, String nombre, String apellido, String email, 
                        String carrera, LocalDate fechaIngreso, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.carrera = carrera;
        this.fechaIngreso = fechaIngreso;
        this.activo = activo;
        this.nombreCompleto = generarNombreCompleto();
    }

    // Constructor para creación (sin ID)
    public EstudianteDTO(String nombre, String apellido, String email, String carrera) {
        this(null, nombre, apellido, email, carrera, LocalDate.now(), true);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        this.nombreCompleto = generarNombreCompleto();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
        this.nombreCompleto = generarNombreCompleto();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    // Método privado para generar nombre completo
    private String generarNombreCompleto() {
        if (nombre != null && apellido != null) {
            return nombre + " " + apellido;
        }
        return "";
    }

    // Métodos de validación
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               email != null && esEmailValido(email) &&
               carrera != null && !carrera.trim().isEmpty();
    }

    private boolean esEmailValido(String email) {
        return email != null && 
               email.contains("@") && 
               email.contains(".") &&
               email.indexOf("@") < email.lastIndexOf(".");
    }

    // Métodos equals y hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EstudianteDTO that = (EstudianteDTO) obj;
        return Objects.equals(id, that.id) && 
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return String.format("EstudianteDTO{id=%d, nombreCompleto='%s', email='%s', carrera='%s', activo=%s}",
                id, nombreCompleto, email, carrera, activo);
    }
}