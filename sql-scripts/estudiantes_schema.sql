-- ================================================
-- PROYECTO CRUD CON TDD - ROBERTO RIVAS LOPEZ
-- Esquema de Base de Datos para SQLonline (MySQL)
-- ================================================

-- Crear base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS crud_estudiantes_tdd
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE crud_estudiantes_tdd;

-- ================================================
-- TABLA PRINCIPAL: estudiantes
-- ================================================
DROP TABLE IF EXISTS estudiantes;

CREATE TABLE estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL COMMENT 'Nombre del estudiante',
    apellido VARCHAR(100) NOT NULL COMMENT 'Apellido del estudiante', 
    email VARCHAR(255) NOT NULL UNIQUE COMMENT 'Email único del estudiante',
    carrera VARCHAR(150) NOT NULL COMMENT 'Carrera que estudia',
    fecha_ingreso DATE NOT NULL COMMENT 'Fecha de ingreso al sistema',
    activo BOOLEAN NOT NULL DEFAULT true COMMENT 'Estado del estudiante (eliminación lógica)',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última modificación',
    
    -- Índices para optimizar consultas
    INDEX idx_email (email),
    INDEX idx_carrera (carrera),
    INDEX idx_activo (activo),
    INDEX idx_fecha_ingreso (fecha_ingreso),
    INDEX idx_nombre_apellido (apellido, nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Tabla de estudiantes para sistema CRUD con TDD';

-- ================================================
-- TABLA DE AUDITORÍA PARA ELIMINACIONES
-- ================================================
CREATE TABLE IF NOT EXISTS log_eliminaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tabla VARCHAR(50) NOT NULL,
    registro_id BIGINT NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    fecha_eliminacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    datos_anteriores TEXT,
    INDEX idx_tabla_registro (tabla, registro_id),
    INDEX idx_fecha (fecha_eliminacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Log de eliminaciones lógicas para auditoría';

-- ================================================
-- PROCEDIMIENTOS ALMACENADOS
-- ================================================

DELIMITER //

-- Procedimiento para obtener estadísticas por carrera
CREATE PROCEDURE IF NOT EXISTS ObtenerEstadisticasPorCarrera(IN p_carrera VARCHAR(150))
BEGIN
    SELECT 
        carrera,
        COUNT(*) as total_estudiantes,
        COUNT(CASE WHEN activo THEN 1 END) as activos,
        COUNT(CASE WHEN NOT activo THEN 1 END) as inactivos,
        AVG(DATEDIFF(CURDATE(), fecha_ingreso)) as dias_promedio_en_sistema
    FROM estudiantes 
    WHERE carrera = p_carrera OR p_carrera IS NULL
    GROUP BY carrera
    ORDER BY total_estudiantes DESC;
END//

-- Función para validar formato de email
CREATE FUNCTION IF NOT EXISTS ValidarEmail(p_email VARCHAR(255)) 
RETURNS BOOLEAN
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE es_valido BOOLEAN DEFAULT FALSE;
    
    IF p_email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$' THEN
        SET es_valido = TRUE;
    END IF;
    
    RETURN es_valido;
END//

DELIMITER ;

-- ================================================
-- TRIGGERS PARA AUDITORÍA
-- ================================================

DELIMITER //

CREATE TRIGGER IF NOT EXISTS trg_estudiante_eliminacion
    BEFORE UPDATE ON estudiantes
    FOR EACH ROW
BEGIN
    -- Si se está desactivando un estudiante, registrar en log
    IF OLD.activo = TRUE AND NEW.activo = FALSE THEN
        INSERT INTO log_eliminaciones 
        (tabla, registro_id, usuario, fecha_eliminacion, datos_anteriores)
        VALUES 
        ('estudiantes', OLD.id, USER(), NOW(), 
         CONCAT('{"nombre":"', OLD.nombre, '","apellido":"', OLD.apellido, '","email":"', OLD.email, '"}'));
    END IF;
END//

DELIMITER ;

-- ================================================
-- INSTRUCCIONES DE USO PARA SQLONLINE
-- ================================================

/*
INSTRUCCIONES PARA USAR EN SQLONLINE:

1. Ir a https://sqlonline.com/
2. Seleccionar "MySQL" como base de datos
3. Ejecutar este script para crear el esquema
4. Luego ejecutar datos_ejemplo.sql para insertar datos
5. Usar consultas_testing.sql para probar funcionalidad

CONFIGURACIÓN PARA JAVA:
- URL: jdbc:mysql://sql.freedb.tech:3306/freedb_tu_base_datos
- Usuario: freedb_tu_usuario  
- Contraseña: tu_contraseña

ALTERNATIVA CON H2 (para pruebas locales):
- URL: jdbc:h2:mem:testdb
- Usuario: sa
- Contraseña: (vacía)

NOTAS IMPORTANTES:
- Cambiar las credenciales en EstudianteRepositorioBD.java
- El script está optimizado para MySQL 5.7+ / 8.0+
- Incluye índices para mejor rendimiento
- Implementa eliminación lógica (activo = false)
- Separado en 3 archivos: esquema, datos y consultas de testing
*/