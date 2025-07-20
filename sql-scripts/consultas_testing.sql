-- ================================================
-- PROYECTO CRUD CON TDD - ROBERTO RIVAS LOPEZ
-- Consultas para Testing y Validación
-- ================================================

-- NOTA: Ejecutar después de estudiantes_schema.sql y datos_ejemplo.sql
USE crud_estudiantes_tdd;

-- ================================================
-- CONSULTAS BÁSICAS DE VERIFICACIÓN
-- ================================================

-- Verificar todos los estudiantes activos
SELECT 
    id,
    CONCAT(nombre, ' ', apellido) AS nombre_completo,
    email,
    carrera,
    fecha_ingreso,
    CASE 
        WHEN activo THEN 'Activo' 
        ELSE 'Inactivo' 
    END AS estado
FROM estudiantes 
ORDER BY apellido, nombre;

-- Contar estudiantes por carrera
SELECT 
    carrera,
    COUNT(*) as total_estudiantes,
    COUNT(CASE WHEN activo THEN 1 END) as estudiantes_activos,
    COUNT(CASE WHEN NOT activo THEN 1 END) as estudiantes_inactivos
FROM estudiantes 
GROUP BY carrera 
ORDER BY total_estudiantes DESC;

-- Estadísticas generales
SELECT 
    COUNT(*) as total_registros,
    COUNT(CASE WHEN activo THEN 1 END) as estudiantes_activos,
    COUNT(CASE WHEN NOT activo THEN 1 END) as estudiantes_inactivos,
    COUNT(DISTINCT carrera) as carreras_diferentes,
    MIN(fecha_ingreso) as primer_ingreso,
    MAX(fecha_ingreso) as ultimo_ingreso
FROM estudiantes;

-- ================================================
-- CONSULTAS PARA PROBAR OPERACIONES CRUD
-- ================================================

-- 1. CREATE - Insertar nuevo estudiante
INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso) 
VALUES ('Test', 'Usuario', 'test.usuario@testing.edu', 'Testing', CURDATE());

-- Verificar inserción
SELECT * FROM estudiantes WHERE email = 'test.usuario@testing.edu';

-- 2. READ - Búsquedas diversas
-- Buscar por ID específico
SELECT * FROM estudiantes WHERE id = 1;

-- Buscar por email
SELECT * FROM estudiantes WHERE email = 'roberto.rivas@devops.edu';

-- Buscar por carrera
SELECT * FROM estudiantes WHERE carrera = 'Ingeniería DevOps' AND activo = true;

-- Buscar por nombre (parcial)
SELECT * FROM estudiantes WHERE nombre LIKE '%Roberto%' AND activo = true;

-- 3. UPDATE - Actualizar estudiante
UPDATE estudiantes 
SET carrera = 'Ingeniería DevOps Avanzada',
    fecha_modificacion = CURRENT_TIMESTAMP
WHERE email = 'test.usuario@testing.edu';

-- Verificar actualización
SELECT * FROM estudiantes WHERE email = 'test.usuario@testing.edu';

-- 4. DELETE - Eliminación lógica
UPDATE estudiantes 
SET activo = false,
    fecha_modificacion = CURRENT_TIMESTAMP
WHERE email = 'test.usuario@testing.edu';

-- Verificar eliminación lógica
SELECT * FROM estudiantes WHERE email = 'test.usuario@testing.edu';

-- ================================================
-- CONSULTAS PARA PROBAR VALIDACIONES
-- ================================================

-- Intentar insertar email duplicado (debe fallar)
-- INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso) 
-- VALUES ('Duplicado', 'Test', 'roberto.rivas@devops.edu', 'Test', CURDATE());

-- Verificar restricción de email único
SELECT email, COUNT(*) as cantidad 
FROM estudiantes 
GROUP BY email 
HAVING COUNT(*) > 1;

-- ================================================
-- CONSULTAS PARA PROBAR ORDENAMIENTO Y FILTROS
-- ================================================

-- Ordenamiento por fecha de ingreso (más recientes primero)
SELECT nombre, apellido, carrera, fecha_ingreso 
FROM estudiantes 
WHERE activo = true 
ORDER BY fecha_ingreso DESC, apellido ASC
LIMIT 10;

-- Estudiantes activos por carrera específica
SELECT nombre, apellido, email, fecha_ingreso
FROM estudiantes 
WHERE carrera = 'Ingeniería DevOps' AND activo = true
ORDER BY fecha_ingreso;

-- Búsqueda de estudiantes por apellido
SELECT * FROM estudiantes 
WHERE apellido LIKE 'García%' AND activo = true;

-- ================================================
-- CONSULTAS PARA PROBAR RENDIMIENTO (con EXPLAIN)
-- ================================================

-- Probar índice por email
EXPLAIN SELECT * FROM estudiantes WHERE email = 'roberto.rivas@devops.edu';

-- Probar índice por carrera
EXPLAIN SELECT * FROM estudiantes WHERE carrera = 'Ingeniería DevOps';

-- Probar índice compuesto nombre-apellido
EXPLAIN SELECT * FROM estudiantes WHERE apellido = 'Rivas' AND nombre = 'Roberto';

-- ================================================
-- CONSULTAS PARA PROBAR FUNCIONES Y PROCEDIMIENTOS
-- ================================================

-- Usar función de validación de email
SELECT email, ValidarEmail(email) as email_valido 
FROM estudiantes 
LIMIT 5;

-- Usar procedimiento de estadísticas por carrera
CALL ObtenerEstadisticasPorCarrera('Ingeniería DevOps');

-- Estadísticas de todas las carreras
CALL ObtenerEstadisticasPorCarrera(NULL);

-- ================================================
-- CONSULTAS PARA VALIDAR INTEGRIDAD DE DATOS
-- ================================================

-- Verificar que no hay registros con datos nulos críticos
SELECT COUNT(*) as registros_con_nulos
FROM estudiantes 
WHERE nombre IS NULL 
   OR apellido IS NULL 
   OR email IS NULL 
   OR carrera IS NULL 
   OR fecha_ingreso IS NULL;

-- Verificar formato de emails
SELECT email 
FROM estudiantes 
WHERE email NOT REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'
   OR email IS NULL;

-- Verificar fechas de ingreso válidas
SELECT * 
FROM estudiantes 
WHERE fecha_ingreso > CURDATE() 
   OR fecha_ingreso < '2020-01-01';

-- ================================================
-- CONSULTAS PARA AUDITORÍA Y LOG
-- ================================================

-- Ver eliminaciones registradas en log
SELECT * FROM log_eliminaciones ORDER BY fecha_eliminacion DESC;

-- Contar eliminaciones por tabla
SELECT tabla, COUNT(*) as eliminaciones 
FROM log_eliminaciones 
GROUP BY tabla;

-- ================================================
-- CONSULTAS DE LIMPIEZA PARA TESTING
-- ================================================

-- Eliminar el registro de prueba (opcional)
DELETE FROM estudiantes WHERE email = 'test.usuario@testing.edu';

-- Restablecer auto_increment (opcional)
-- ALTER TABLE estudiantes AUTO_INCREMENT = 1;

-- ================================================
-- INSTRUCCIONES DE USO
-- ================================================

/*
CÓMO USAR ESTE ARCHIVO:

1. PREPARACIÓN:
   - Ejecutar estudiantes_schema.sql
   - Ejecutar datos_ejemplo.sql
   - Luego ejecutar este archivo

2. TESTING PASO A PASO:
   - Ejecutar las consultas una por una
   - Verificar los resultados esperados
   - Usar EXPLAIN para validar uso de índices

3. VALIDACIONES INCLUIDAS:
   - Operaciones CRUD completas
   - Validación de restricciones
   - Pruebas de rendimiento
   - Verificación de integridad
   - Auditoría de cambios

4. RESULTADOS ESPERADOS:
   - Todos los SELECT deben retornar datos consistentes
   - Los EXPLAIN deben mostrar uso de índices
   - Las validaciones no deben mostrar errores
   - Los procedimientos deben ejecutarse sin problemas

5. NOTAS IMPORTANTES:
   - Algunas consultas están comentadas (pueden causar errores intencionalmente)
   - El archivo incluye limpieza opcional al final
   - Todas las consultas están optimizadas para MySQL 5.7+
*/
