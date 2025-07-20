package com.roberto.rivas.servicio;

import com.roberto.rivas.modelo.Estudiante;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interfaz del servicio de Estudiantes
 * Aplica Principio de Inversión de Dependencias y Separación de Intereses
 * Define la lógica de negocio para gestión de estudiantes
 * 
 * @author Roberto Rivas Lopez
 */
public interface IEstudianteServicio {
    
    /**
     * Registrar un nuevo estudiante con validaciones de negocio
     * @param estudianteDTO Datos del estudiante a registrar
     * @return DTO del estudiante registrado
     * @throws ServicioException Si no cumple las reglas de negocio
     */
    EstudianteDTO registrarEstudiante(EstudianteDTO estudianteDTO) throws ServicioException;
    
    /**
     * Buscar estudiante por ID
     * @param id ID del estudiante
     * @return Optional con el DTO del estudiante si existe
     * @throws ServicioException Si ocurre error en la búsqueda
     */
    Optional<EstudianteDTO> buscarEstudiantePorId(Long id) throws ServicioException;
    
    /**
     * Buscar estudiante por email
     * @param email Email del estudiante
     * @return Optional con el DTO del estudiante si existe
     * @throws ServicioException Si ocurre error en la búsqueda
     */
    Optional<EstudianteDTO> buscarEstudiantePorEmail(String email) throws ServicioException;
    
    /**
     * Obtener lista de todos los estudiantes activos
     * @return Lista de DTOs de estudiantes activos
     * @throws ServicioException Si ocurre error en la consulta
     */
    List<EstudianteDTO> listarEstudiantesActivos() throws ServicioException;
    
    /**
     * Obtener estudiantes por carrera
     * @param carrera Nombre de la carrera
     * @return Lista de DTOs de estudiantes de la carrera
     * @throws ServicioException Si ocurre error en la consulta
     */
    List<EstudianteDTO> listarEstudiantesPorCarrera(String carrera) throws ServicioException;
    
    /**
     * Actualizar información de un estudiante
     * @param estudianteDTO Datos actualizados del estudiante
     * @return DTO del estudiante actualizado
     * @throws ServicioException Si no existe o no cumple reglas de negocio
     */
    EstudianteDTO actualizarEstudiante(EstudianteDTO estudianteDTO) throws ServicioException;
    
    /**
     * Eliminar estudiante (eliminación lógica)
     * @param id ID del estudiante a eliminar
     * @return true si se eliminó correctamente
     * @throws ServicioException Si el estudiante no existe
     */
    boolean eliminarEstudiante(Long id) throws ServicioException;
    
    /**
     * Reactivar estudiante previamente eliminado
     * @param id ID del estudiante a reactivar
     * @return DTO del estudiante reactivado
     * @throws ServicioException Si el estudiante no existe
     */
    EstudianteDTO reactivarEstudiante(Long id) throws ServicioException;
    
    /**
     * Obtener estadísticas del sistema
     * @return Mapa con estadísticas de estudiantes
     * @throws ServicioException Si ocurre error al generar estadísticas
     */
    Map<String, Object> obtenerEstadisticas() throws ServicioException;
    
    /**
     * Validar si un email es único en el sistema
     * @param email Email a validar
     * @param idExcluir ID a excluir de la validación (para actualizaciones)
     * @return true si el email es único
     * @throws ServicioException Si ocurre error en la validación
     */
    boolean validarEmailUnico(String email, Long idExcluir) throws ServicioException;
    
    /**
     * Obtener total de estudiantes activos
     * @return Número total de estudiantes activos
     * @throws ServicioException Si ocurre error en el conteo
     */
    long contarEstudiantesActivos() throws ServicioException;
}