package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de Estudiantes
 * Aplica Principio de Inversión de Dependencias (SOLID)
 * Principio de Separación de Intereses
 * 
 * @author Roberto Rivas Lopez
 */
public interface IEstudianteRepositorio {
    
    /**
     * Crear un nuevo estudiante
     * @param estudiante El estudiante a crear
     * @return El estudiante creado con ID asignado
     * @throws RepositorioException Si ocurre un error durante la creación
     */
    Estudiante crear(Estudiante estudiante) throws RepositorioException;
    
    /**
     * Buscar estudiante por ID
     * @param id ID del estudiante
     * @return Optional con el estudiante si existe
     * @throws RepositorioException Si ocurre un error durante la búsqueda
     */
    Optional<Estudiante> buscarPorId(Long id) throws RepositorioException;
    
    /**
     * Buscar estudiante por email
     * @param email Email del estudiante
     * @return Optional con el estudiante si existe
     * @throws RepositorioException Si ocurre un error durante la búsqueda
     */
    Optional<Estudiante> buscarPorEmail(String email) throws RepositorioException;
    
    /**
     * Obtener todos los estudiantes activos
     * @return Lista de estudiantes activos
     * @throws RepositorioException Si ocurre un error durante la consulta
     */
    List<Estudiante> obtenerTodos() throws RepositorioException;
    
    /**
     * Obtener estudiantes por carrera
     * @param carrera Nombre de la carrera
     * @return Lista de estudiantes de la carrera especificada
     * @throws RepositorioException Si ocurre un error durante la consulta
     */
    List<Estudiante> obtenerPorCarrera(String carrera) throws RepositorioException;
    
    /**
     * Actualizar un estudiante existente
     * @param estudiante El estudiante con datos actualizados
     * @return El estudiante actualizado
     * @throws RepositorioException Si el estudiante no existe o error durante actualización
     */
    Estudiante actualizar(Estudiante estudiante) throws RepositorioException;
    
    /**
     * Eliminar un estudiante por ID (eliminación lógica)
     * @param id ID del estudiante a eliminar
     * @return true si se eliminó correctamente
     * @throws RepositorioException Si el estudiante no existe o error durante eliminación
     */
    boolean eliminar(Long id) throws RepositorioException;
    
    /**
     * Verificar si existe un estudiante por ID
     * @param id ID del estudiante
     * @return true si existe el estudiante
     * @throws RepositorioException Si ocurre un error durante la verificación
     */
    boolean existe(Long id) throws RepositorioException;
    
    /**
     * Verificar si existe un estudiante por email
     * @param email Email del estudiante
     * @return true si existe el estudiante con ese email
     * @throws RepositorioException Si ocurre un error durante la verificación
     */
    boolean existePorEmail(String email) throws RepositorioException;
    
    /**
     * Contar total de estudiantes activos
     * @return Número total de estudiantes activos
     * @throws RepositorioException Si ocurre un error durante el conteo
     */
    long contarEstudiantes() throws RepositorioException;
}