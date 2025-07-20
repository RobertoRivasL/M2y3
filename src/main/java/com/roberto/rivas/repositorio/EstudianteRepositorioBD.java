package com.roberto.rivas.repositorio;

import com.roberto.rivas.modelo.Estudiante;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación con base de datos del repositorio de Estudiantes
 * Aplica principios SOLID y manejo de recursos
 * Compatible con SQLonline y MySQL
 * 
 * @author Roberto Rivas Lopez
 */
public class EstudianteRepositorioBD implements IEstudianteRepositorio {

    private final String url;
    private final String usuario;
    private final String contrasena;

    // Constructor para SQLonline (Principio de Inyección de Dependencias)
    public EstudianteRepositorioBD(String url, String usuario, String contrasena) {
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Constructor por defecto para SQLonline gratuito
    public EstudianteRepositorioBD() {
        // Configuración para SQLonline - estos valores deben ser reemplazados
        this.url = "jdbc:mysql://sql.freedb.tech:3306/freedb_db_estudiantes_tdd";
        this.usuario = "freedb_roberto_rivas";
        this.contrasena = "tu_password_aqui";
    }

    @Override
    public Estudiante crear(Estudiante estudiante) throws RepositorioException {
        validarEstudianteParaCreacion(estudiante);
        
        String sql = """
            INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso, activo) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Validar email único antes de insertar
            if (existePorEmail(estudiante.getEmail())) {
                throw new RepositorioException("Ya existe un estudiante con este email", "EMAIL_DUPLICADO");
            }

            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getApellido());
            stmt.setString(3, estudiante.getEmail());
            stmt.setString(4, estudiante.getCarrera());
            stmt.setDate(5, java.sql.Date.valueOf(estudiante.getFechaIngreso()));
            stmt.setBoolean(6, estudiante.isActivo());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new RepositorioException("No se pudo crear el estudiante", "CREACION_FALLIDA");
            }

            // Obtener ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    estudiante.setId(generatedKeys.getLong(1));
                    return clonarEstudiante(estudiante);
                } else {
                    throw new RepositorioException("No se pudo obtener el ID generado", "ID_NO_GENERADO");
                }
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al crear estudiante en base de datos: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public Optional<Estudiante> buscarPorId(Long id) throws RepositorioException {
        validarIdNoNulo(id);
        
        String sql = "SELECT * FROM estudiantes WHERE id = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSetAEstudiante(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al buscar estudiante por ID: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public Optional<Estudiante> buscarPorEmail(String email) throws RepositorioException {
        validarEmailNoNulo(email);
        
        String sql = "SELECT * FROM estudiantes WHERE LOWER(email) = LOWER(?)";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSetAEstudiante(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al buscar estudiante por email: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public List<Estudiante> obtenerTodos() throws RepositorioException {
        String sql = "SELECT * FROM estudiantes WHERE activo = true ORDER BY apellido, nombre";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Estudiante> estudiantes = new ArrayList<>();
            while (rs.next()) {
                estudiantes.add(mapearResultSetAEstudiante(rs));
            }
            return estudiantes;

        } catch (SQLException e) {
            throw new RepositorioException("Error al obtener todos los estudiantes: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public List<Estudiante> obtenerPorCarrera(String carrera) throws RepositorioException {
        validarCarreraNoNula(carrera);
        
        String sql = "SELECT * FROM estudiantes WHERE LOWER(carrera) = LOWER(?) AND activo = true ORDER BY apellido, nombre";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, carrera);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Estudiante> estudiantes = new ArrayList<>();
                while (rs.next()) {
                    estudiantes.add(mapearResultSetAEstudiante(rs));
                }
                return estudiantes;
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al obtener estudiantes por carrera: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public Estudiante actualizar(Estudiante estudiante) throws RepositorioException {
        validarEstudianteParaActualizacion(estudiante);
        
        String sql = """
            UPDATE estudiantes 
            SET nombre = ?, apellido = ?, email = ?, carrera = ?, fecha_ingreso = ?, activo = ?
            WHERE id = ?
            """;

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            // Validar que existe el estudiante
            if (!existe(estudiante.getId())) {
                throw new RepositorioException("El estudiante no existe", "ESTUDIANTE_NO_ENCONTRADO");
            }

            // Validar email único (excluyendo el propio estudiante)
            validarEmailUnicoParaActualizacion(estudiante.getEmail(), estudiante.getId());

            stmt.setString(1, estudiante.getNombre());
            stmt.setString(2, estudiante.getApellido());
            stmt.setString(3, estudiante.getEmail());
            stmt.setString(4, estudiante.getCarrera());
            stmt.setDate(5, java.sql.Date.valueOf(estudiante.getFechaIngreso()));
            stmt.setBoolean(6, estudiante.isActivo());
            stmt.setLong(7, estudiante.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new RepositorioException("No se pudo actualizar el estudiante", "ACTUALIZACION_FALLIDA");
            }

            return clonarEstudiante(estudiante);

        } catch (SQLException e) {
            throw new RepositorioException("Error al actualizar estudiante: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public boolean eliminar(Long id) throws RepositorioException {
        validarIdNoNulo(id);
        
        // Eliminación lógica - cambiar activo a false
        String sql = "UPDATE estudiantes SET activo = false WHERE id = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            if (!existe(id)) {
                throw new RepositorioException("El estudiante no existe", "ESTUDIANTE_NO_ENCONTRADO");
            }

            stmt.setLong(1, id);
            int filasAfectadas = stmt.executeUpdate();
            
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new RepositorioException("Error al eliminar estudiante: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public boolean existe(Long id) throws RepositorioException {
        validarIdNoNulo(id);
        
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE id = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al verificar existencia del estudiante: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public boolean existePorEmail(String email) throws RepositorioException {
        validarEmailNoNulo(email);
        
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE LOWER(email) = LOWER(?)";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al verificar existencia por email: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    @Override
    public long contarEstudiantes() throws RepositorioException {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE activo = true";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getLong(1) : 0;

        } catch (SQLException e) {
            throw new RepositorioException("Error al contar estudiantes: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    // Métodos privados de utilidad (Principio de Separación de Intereses)
    private Connection obtenerConexion() throws SQLException {
        try {
            // Cargar driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
        
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    private Estudiante mapearResultSetAEstudiante(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante(
            rs.getLong("id"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("email"),
            rs.getString("carrera")
        );
        
        java.sql.Date fechaIngreso = rs.getDate("fecha_ingreso");
        if (fechaIngreso != null) {
            estudiante.setFechaIngreso(fechaIngreso.toLocalDate());
        }
        
        estudiante.setActivo(rs.getBoolean("activo"));
        return estudiante;
    }

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

    // Métodos de validación (Principio de Encapsulación)
    private void validarEstudianteParaCreacion(Estudiante estudiante) throws RepositorioException {
        if (estudiante == null) {
            throw new RepositorioException("No se puede crear un estudiante nulo", "ESTUDIANTE_NULO");
        }
        validarDatosComunes(estudiante);
    }

    private void validarEstudianteParaActualizacion(Estudiante estudiante) throws RepositorioException {
        if (estudiante == null) {
            throw new RepositorioException("No se puede actualizar un estudiante nulo", "ESTUDIANTE_NULO");
        }
        validarIdNoNulo(estudiante.getId());
        validarDatosComunes(estudiante);
    }

    private void validarDatosComunes(Estudiante estudiante) throws RepositorioException {
        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty()) {
            throw new RepositorioException("El nombre es requerido", "NOMBRE_REQUERIDO");
        }
        if (estudiante.getApellido() == null || estudiante.getApellido().trim().isEmpty()) {
            throw new RepositorioException("El apellido es requerido", "APELLIDO_REQUERIDO");
        }
        if (estudiante.getEmail() == null || estudiante.getEmail().trim().isEmpty()) {
            throw new RepositorioException("El email es requerido", "EMAIL_REQUERIDO");
        }
        if (estudiante.getCarrera() == null || estudiante.getCarrera().trim().isEmpty()) {
            throw new RepositorioException("La carrera es requerida", "CARRERA_REQUERIDA");
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

    private void validarEmailUnicoParaActualizacion(String email, Long idExcluir) throws RepositorioException {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE LOWER(email) = LOWER(?) AND id != ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setLong(2, idExcluir);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new RepositorioException("Ya existe otro estudiante con este email", "EMAIL_DUPLICADO");
                }
            }

        } catch (SQLException e) {
            throw new RepositorioException("Error al validar email único: " + e.getMessage(), 
                                         "SQL_ERROR", e);
        }
    }

    // Método para crear las tablas (útil para inicialización)
    public void inicializarBaseDatos() throws RepositorioException {
        String sqlCrearTabla = """
            CREATE TABLE IF NOT EXISTS estudiantes (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                apellido VARCHAR(100) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                carrera VARCHAR(150) NOT NULL,
                fecha_ingreso DATE NOT NULL,
                activo BOOLEAN NOT NULL DEFAULT true,
                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_email (email),
                INDEX idx_carrera (carrera),
                INDEX idx_activo (activo)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
            """;

        try (Connection conexion = obtenerConexion();
             PreparedStatement stmt = conexion.prepareStatement(sqlCrearTabla)) {
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RepositorioException("Error al inicializar base de datos: " + e.getMessage(), 
                                         "INIT_BD_ERROR", e);
        }
    }
}