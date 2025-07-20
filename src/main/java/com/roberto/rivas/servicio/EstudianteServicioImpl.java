package com.roberto.rivas.servicio;

import com.roberto.rivas.modelo.Estudiante;
import com.roberto.rivas.repositorio.IEstudianteRepositorio;
import com.roberto.rivas.repositorio.RepositorioException;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Estudiantes
 * Aplica principios SOLID y manejo de excepciones
 * Implementa lógica de negocio con validaciones
 * 
 * @author Roberto Rivas Lopez
 */
public class EstudianteServicioImpl implements IEstudianteServicio {

    private final IEstudianteRepositorio repositorio;

    // Constructor que implementa Inyección de Dependencias (Principio SOLID)
    public EstudianteServicioImpl(IEstudianteRepositorio repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio es requerido");
        }
        this.repositorio = repositorio;
    }

    @Override
    public EstudianteDTO registrarEstudiante(EstudianteDTO estudianteDTO) throws ServicioException {
        validarDTONoNulo(estudianteDTO, "Los datos del estudiante son requeridos");
        validarDatosCompletos(estudianteDTO);
        
        try {
            // Validar email único (Regla de negocio)
            if (repositorio.existePorEmail(estudianteDTO.getEmail())) {
                throw new ServicioException("Ya existe un estudiante con este email", 
                                          "EMAIL_DUPLICADO", ServicioException.TipoError.NEGOCIO);
            }

            // Convertir DTO a entidad
            Estudiante estudiante = convertirDTOAEntidad(estudianteDTO);
            
            // Crear en repositorio
            Estudiante estudianteCreado = repositorio.crear(estudiante);
            
            // Convertir entidad a DTO y retornar
            return convertirEntidadADTO(estudianteCreado);
            
        } catch (RepositorioException e) {
            throw new ServicioException("Error al registrar el estudiante: " + e.getMessage(), 
                                      "REGISTRO_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public Optional<EstudianteDTO> buscarEstudiantePorId(Long id) throws ServicioException {
        validarIdNoNulo(id);
        
        try {
            return repositorio.buscarPorId(id)
                             .map(this::convertirEntidadADTO);
        } catch (RepositorioException e) {
            throw new ServicioException("Error al buscar el estudiante: " + e.getMessage(), 
                                      "BUSQUEDA_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public Optional<EstudianteDTO> buscarEstudiantePorEmail(String email) throws ServicioException {
        validarEmailNoVacio(email);
        
        try {
            return repositorio.buscarPorEmail(email)
                             .map(this::convertirEntidadADTO);
        } catch (RepositorioException e) {
            throw new ServicioException("Error al buscar el estudiante por email: " + e.getMessage(), 
                                      "BUSQUEDA_EMAIL_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public List<EstudianteDTO> listarEstudiantesActivos() throws ServicioException {
        try {
            return repositorio.obtenerTodos()
                             .stream()
                             .map(this::convertirEntidadADTO)
                             .collect(Collectors.toList());
        } catch (RepositorioException e) {
            throw new ServicioException("Error al listar estudiantes: " + e.getMessage(), 
                                      "LISTADO_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public List<EstudianteDTO> listarEstudiantesPorCarrera(String carrera) throws ServicioException {
        validarCarreraNoVacia(carrera);
        
        try {
            return repositorio.obtenerPorCarrera(carrera)
                             .stream()
                             .map(this::convertirEntidadADTO)
                             .collect(Collectors.toList());
        } catch (RepositorioException e) {
            throw new ServicioException("Error al listar estudiantes por carrera: " + e.getMessage(), 
                                      "LISTADO_CARRERA_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public EstudianteDTO actualizarEstudiante(EstudianteDTO estudianteDTO) throws ServicioException {
        validarDTONoNulo(estudianteDTO, "Los datos del estudiante son requeridos");
        validarIdNoNulo(estudianteDTO.getId());
        validarDatosCompletos(estudianteDTO);
        
        try {
            // Verificar que el estudiante existe
            if (!repositorio.existe(estudianteDTO.getId())) {
                throw new ServicioException("El estudiante no existe", 
                                          "ESTUDIANTE_NO_ENCONTRADO", ServicioException.TipoError.NEGOCIO);
            }

            // Validar email único (excluyendo el propio estudiante)
            if (!validarEmailUnico(estudianteDTO.getEmail(), estudianteDTO.getId())) {
                throw new ServicioException("Ya existe otro estudiante con este email", 
                                          "EMAIL_DUPLICADO", ServicioException.TipoError.NEGOCIO);
            }

            // Convertir y actualizar
            Estudiante estudiante = convertirDTOAEntidad(estudianteDTO);
            Estudiante estudianteActualizado = repositorio.actualizar(estudiante);
            
            return convertirEntidadADTO(estudianteActualizado);
            
        } catch (RepositorioException e) {
            throw new ServicioException("Error al actualizar el estudiante: " + e.getMessage(), 
                                      "ACTUALIZACION_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public boolean eliminarEstudiante(Long id) throws ServicioException {
        validarIdNoNulo(id);
        
        try {
            if (!repositorio.existe(id)) {
                throw new ServicioException("El estudiante no existe", 
                                          "ESTUDIANTE_NO_ENCONTRADO", ServicioException.TipoError.NEGOCIO);
            }

            return repositorio.eliminar(id);
            
        } catch (RepositorioException e) {
            throw new ServicioException("Error al eliminar el estudiante: " + e.getMessage(), 
                                      "ELIMINACION_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public EstudianteDTO reactivarEstudiante(Long id) throws ServicioException {
        validarIdNoNulo(id);
        
        try {
            Optional<Estudiante> estudianteOpt = repositorio.buscarPorId(id);
            
            if (!estudianteOpt.isPresent()) {
                throw new ServicioException("El estudiante no existe", 
                                          "ESTUDIANTE_NO_ENCONTRADO", ServicioException.TipoError.NEGOCIO);
            }

            Estudiante estudiante = estudianteOpt.get();
            estudiante.activar();
            
            Estudiante estudianteReactivado = repositorio.actualizar(estudiante);
            return convertirEntidadADTO(estudianteReactivado);
            
        } catch (RepositorioException e) {
            throw new ServicioException("Error al reactivar el estudiante: " + e.getMessage(), 
                                      "REACTIVACION_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public Map<String, Object> obtenerEstadisticas() throws ServicioException {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            
            long totalActivos = repositorio.contarEstudiantes();
            estadisticas.put("totalEstudiantesActivos", totalActivos);
            
            // Estadísticas por carrera
            List<Estudiante> todosEstudiantes = repositorio.obtenerTodos();
            Map<String, Long> estudiantesPorCarrera = todosEstudiantes.stream()
                    .collect(Collectors.groupingBy(
                        Estudiante::getCarrera,
                        Collectors.counting()
                    ));
            
            estadisticas.put("estudiantesPorCarrera", estudiantesPorCarrera);
            estadisticas.put("fechaGeneracion", new Date());
            
            return estadisticas;
            
        } catch (RepositorioException e) {
            throw new ServicioException("Error al generar estadísticas: " + e.getMessage(), 
                                      "ESTADISTICAS_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public boolean validarEmailUnico(String email, Long idExcluir) throws ServicioException {
        validarEmailNoVacio(email);
        
        try {
            Optional<Estudiante> estudianteExistente = repositorio.buscarPorEmail(email);
            
            if (!estudianteExistente.isPresent()) {
                return true; // Email no existe, es único
            }
            
            // Si existe, verificar si es el mismo estudiante que estamos actualizando
            return Objects.equals(estudianteExistente.get().getId(), idExcluir);
            
        } catch (RepositorioException e) {
            throw new ServicioException("Error al validar email único: " + e.getMessage(), 
                                      "VALIDACION_EMAIL_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    @Override
    public long contarEstudiantesActivos() throws ServicioException {
        try {
            return repositorio.contarEstudiantes();
        } catch (RepositorioException e) {
            throw new ServicioException("Error al contar estudiantes: " + e.getMessage(), 
                                      "CONTEO_ERROR", ServicioException.TipoError.DATOS, e);
        }
    }

    // Métodos privados de validación (Principio de Separación de Intereses)
    private void validarDTONoNulo(EstudianteDTO dto, String mensaje) throws ServicioException {
        if (dto == null) {
            throw new ServicioException(mensaje, "DTO_NULO", ServicioException.TipoError.VALIDACION);
        }
    }

    private void validarIdNoNulo(Long id) throws ServicioException {
        if (id == null) {
            throw new ServicioException("El ID es requerido", "ID_NULO", ServicioException.TipoError.VALIDACION);
        }
    }

    private void validarEmailNoVacio(String email) throws ServicioException {
        if (email == null || email.trim().isEmpty()) {
            throw new ServicioException("El email es requerido", "EMAIL_VACIO", ServicioException.TipoError.VALIDACION);
        }
    }

    private void validarCarreraNoVacia(String carrera) throws ServicioException {
        if (carrera == null || carrera.trim().isEmpty()) {
            throw new ServicioException("La carrera es requerida", "CARRERA_VACIA", ServicioException.TipoError.VALIDACION);
        }
    }

    private void validarDatosCompletos(EstudianteDTO dto) throws ServicioException {
        if (!dto.esValido()) {
            throw new ServicioException("Los datos del estudiante no son válidos", 
                                      "DATOS_INVALIDOS", ServicioException.TipoError.VALIDACION);
        }
    }

    // Métodos de conversión (Principio de Separación de Intereses)
    private Estudiante convertirDTOAEntidad(EstudianteDTO dto) {
        Estudiante estudiante = new Estudiante(dto.getId(), dto.getNombre(), dto.getApellido(),
                                             dto.getEmail(), dto.getCarrera());
        
        if (dto.getFechaIngreso() != null) {
            estudiante.setFechaIngreso(dto.getFechaIngreso());
        }
        
        estudiante.setActivo(dto.isActivo());
        return estudiante;
    }

    private EstudianteDTO convertirEntidadADTO(Estudiante entidad) {
        return new EstudianteDTO(entidad.getId(), entidad.getNombre(), entidad.getApellido(),
                               entidad.getEmail(), entidad.getCarrera(), 
                               entidad.getFechaIngreso(), entidad.isActivo());
    }
}