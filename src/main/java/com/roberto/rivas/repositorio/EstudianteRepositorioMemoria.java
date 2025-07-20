package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de Estudiantes
 * Aplica principios SOLID y patrones de diseño
 * Implementación thread-safe para entornos concurrentes
 * 
 * @author Roberto Rivas Lopez
 */
public class EstudianteRepositorioMemoria implements IEstudianteRepositorio {

    private final Map<Long, Estudiante> estudiantes;
    private final AtomicLong contadorId;
    
    public EstudianteRepositorioMemoria() {
        this.estudiantes = new ConcurrentHashMap<>();
        this.contadorId = new AtomicLong(1);
    }

    @Override
    public Estudiante crear(Estudiante estudiante) throws RepositorioException {
        validarEstudianteNoNulo(estudiante);
        validarEmailUnico(estudiante.getEmail(), null);
        
        // Clonar estudiante para evitar modificaciones externas (Principio de Encapsulación)
        Estudiante nuevoEstudiante = clonarEstudiante(estudiante);
        nuevoEstudiante.setId(contadorId.getAndIncrement());
        
        estudiantes.put(nuevoEstudiante.getId(), nuevoEstudiante);
        
        return clonarEstudiante(nuevoEstudiante);
    }

    @Override
    public Optional<Estudiante> buscarPorId(Long id) throws RepositorioException {
        validarIdNoNulo(id);
        
        Estudiante estudiante = estudiantes.get(id);
        return estudiante != null ? Optional.of(clonarEstudiante(estudiante)) : Optional.empty();
    }

    @Override
    public Optional<Estudiante> buscarPorEmail(String email) throws RepositorioException {
        validarEmailNoNulo(email);
        
        return estudiantes.values().stream()
                .filter(e -> email.equalsIgnoreCase(e.getEmail()))
                .map(this::clonarEstudiante)
                .findFirst();
    }

    @Override
    public List<Estudiante> obtenerTodos() throws RepositorioException {
        return estudiantes.values().stream()
                .filter(Estudiante::isActivo)
                .map(this::clonarEstudiante)
                .collect(Collectors.toList());
    }

    @Override
    public List<Estudiante> obtenerPorCarrera(String carrera) throws RepositorioException {
        validarCarreraNoNula(carrera);
        
        return estudiantes.values().stream()
                .filter(Estudiante::isActivo)
                .filter(e -> carrera.equalsIgnoreCase(e.getCarrera()))
                .map(this::clonarEstudiante)
                .collect(Collectors.toList());
    }

    @Override
    public Estudiante actualizar(Estudiante estudiante) throws RepositorioException {
        validarEstudianteNoNulo(estudiante);
        validarIdNoNulo(estudiante.getId());
        
        if (!existe(estudiante.getId())) {
            throw new RepositorioException("El estudiante no existe", "ESTUDIANTE_NO_ENCONTRADO");
        }
        
        // Validar que el email no esté en uso por otro estudiante
        validarEmailUnico(estudiante.getEmail(), estudiante.getId());
        
        Estudiante estudianteActualizado = clonarEstudiante(estudiante);
        estudiantes.put(estudianteActualizado.getId(), estudianteActualizado);
        
        return clonarEstudiante(estudianteActualizado);
    }

    @Override
    public boolean eliminar(Long id) throws RepositorioException {
        validarIdNoNulo(id);
        
        Estudiante estudiante = estudiantes.get(id);
        if (estudiante == null) {
            throw new RepositorioException("El estudiante no existe", "ESTUDIANTE_NO_ENCONTRADO");
        }
        
        // Eliminación lógica (Principio de Conservación de Datos)
        estudiante.desactivar();
        return true;
    }

    @Override
    public boolean existe(Long id) throws RepositorioException {
        validarIdNoNulo(id);
        return estudiantes.containsKey(id);
    }

    @Override
    public boolean existePorEmail(String email) throws RepositorioException {
        validarEmailNoNulo(email);
        
        return estudiantes.values().stream()
                .anyMatch(e -> email.equalsIgnoreCase(e.getEmail()));
    }

    @Override
    public long contarEstudiantes() throws RepositorioException {
        return estudiantes.values().stream()
                .filter(Estudiante::isActivo)
                .count();
    }

    // Métodos privados de validación (Principio de Separación de Intereses)
    private void validarEstudianteNoNulo(Estudiante estudiante) throws RepositorioException {
        if (estudiante == null) {
            throw new RepositorioException("No se puede procesar un estudiante nulo", "ESTUDIANTE_NULO");
        }
    }

    private void validarIdNoNulo(Long id) throws RepositorioException {
        if (id == null) {
            throw new RepositorioException("El ID no puede ser nulo", "ID_NULO");
        }
    }

    private void validarEmailNoNulo(String email) throws RepositorioException {
        if (email == null || email.trim().isEmpty()) {
            throw new RepositorioException("El email no puede ser nulo o vacío", "EMAIL_NULO");
        }
    }

    private void validarCarreraNoNula(String carrera) throws RepositorioException {
        if (carrera == null || carrera.trim().isEmpty()) {
            throw new RepositorioException("La carrera no puede ser nula o vacía", "CARRERA_NULA");
        }
    }

    private void validarEmailUnico(String email, Long idExcluir) throws RepositorioException {
        boolean emailExiste = estudiantes.values().stream()
                .anyMatch(e -> email.equalsIgnoreCase(e.getEmail()) && 
                              !Objects.equals(e.getId(), idExcluir));
        
        if (emailExiste) {
            throw new RepositorioException("Ya existe un estudiante con este email", "EMAIL_DUPLICADO");
        }
    }

    // Método para clonar estudiantes (Principio de Inmutabilidad)
    private Estudiante clonarEstudiante(Estudiante original) {
        Estudiante clon = new Estudiante(original.getId(), 
                                       original.getNombre(), 
                                       original.getApellido(),
                                       original.getEmail(), 
                                       original.getCarrera());
        clon.setFechaIngreso(original.getFechaIngreso());
        clon.setActivo(original.isActivo());
        return clon;
    }

    // Método para limpiar repositorio (útil para pruebas)
    public void limpiar() {
        estudiantes.clear();
        contadorId.set(1);
    }

    // Método para obtener estadísticas (funcionalidad adicional)
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalEstudiantes = estudiantes.size();
        long estudiantesActivos = estudiantes.values().stream()
                .filter(Estudiante::isActivo)
                .count();
        long estudiantesInactivos = totalEstudiantes - estudiantesActivos;
        
        Map<String, Long> estudiantesPorCarrera = estudiantes.values().stream()
                .filter(Estudiante::isActivo)
                .collect(Collectors.groupingBy(
                    Estudiante::getCarrera,
                    Collectors.counting()
                ));
        
        stats.put("totalEstudiantes", totalEstudiantes);
        stats.put("estudiantesActivos", estudiantesActivos);
        stats.put("estudiantesInactivos", estudiantesInactivos);
        stats.put("estudiantesPorCarrera", estudiantesPorCarrera);
        stats.put("proximoId", contadorId.get());
        
        return stats;
    }
}