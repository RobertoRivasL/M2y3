package com.roberto.rivas.modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad Estudiante para el CRUD con TDD
 * Aplica principios de Encapsulación y Abstracción
 * 
 * @author Roberto Rivas Lopez
 */
public class Estudiante {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String carrera;
    private LocalDate fechaIngreso;
    private boolean activo;

    // Constructor por defecto
    public Estudiante() {
        this.activo = true;
        this.fechaIngreso = LocalDate.now();
    }

    // Constructor completo
    public Estudiante(Long id, String nombre, String apellido, String email, String carrera) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.carrera = carrera;
    }

    // Constructor sin ID (para creación)
    public Estudiante(String nombre, String apellido, String email, String carrera) {
        this(null, nombre, apellido, email, carrera);
    }

    // Getters y Setters con validaciones (Principio de Encapsulación)
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
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío");
        }
        this.apellido = apellido.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !esEmailValido(email)) {
            throw new IllegalArgumentException("El email debe tener un formato válido");
        }
        this.email = email.toLowerCase().trim();
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        if (carrera == null || carrera.trim().isEmpty()) {
            throw new IllegalArgumentException("La carrera no puede ser nula o vacía");
        }
        this.carrera = carrera.trim();
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser nula");
        }
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
        }
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Métodos de negocio (Principio de Abstracción)
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    // Validación privada (Principio de Encapsulación)
    private boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        email = email.trim();
        
        // Verificar formato básico
        if (!email.contains("@") || !email.contains(".")) {
            return false;
        }
        
        // Verificar que @ esté antes del último punto
        int atIndex = email.indexOf("@");
        int lastDotIndex = email.lastIndexOf(".");
        
        if (atIndex <= 0 || atIndex >= lastDotIndex - 1 || lastDotIndex >= email.length() - 1) {
            return false;
        }
        
        // Verificar que no haya @ múltiples
        if (email.indexOf("@") != email.lastIndexOf("@")) {
            return false;
        }
        
        // Verificar que no empiece o termine con @ o .
        if (email.startsWith("@") || email.endsWith("@") || 
            email.startsWith(".") || email.endsWith(".")) {
            return false;
        }
        
        // Verificar que tenga parte local y dominio
        String[] parts = email.split("@");
        if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
            return false;
        }
        
        // Verificar que el dominio tenga al menos un punto
        String domain = parts[1];
        if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }
        
        return true;
    }

    // Métodos equals y hashCode para comparaciones
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estudiante that = (Estudiante) obj;
        return Objects.equals(id, that.id) && 
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return String.format("Estudiante{id=%d, nombre='%s', apellido='%s', email='%s', carrera='%s', activo=%s}",
                id, nombre, apellido, email, carrera, activo);
    }
}