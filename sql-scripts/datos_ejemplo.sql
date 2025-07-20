-- ================================================
-- PROYECTO CRUD CON TDD - ROBERTO RIVAS LOPEZ
-- Datos de Ejemplo para Testing
-- ================================================

-- NOTA: Ejecutar primero estudiantes_schema.sql
USE crud_estudiantes_tdd;

-- ================================================
-- DATOS DE PRUEBA PARA DEMOSTRACIÓN
-- ================================================

-- Estudiantes de Ingeniería DevOps (mi especialidad)
INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso) VALUES
('Roberto Carlos', 'Rivas López', 'roberto.rivas@devops.edu', 'Ingeniería DevOps', '2024-01-15'),
('Ana María', 'García Fernández', 'ana.garcia@containers.edu', 'Ingeniería DevOps', '2024-02-01'),
('Carlos Alberto', 'Mendoza Silva', 'carlos.mendoza@kubernetes.edu', 'Ingeniería DevOps', '2024-01-20');

-- Estudiantes de otras carreras para diversidad
INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso) VALUES
('María José', 'Rodríguez Pérez', 'maria.rodriguez@sistemas.edu', 'Ingeniería en Sistemas', '2024-01-10'),
('Juan Pablo', 'López Martínez', 'juan.lopez@software.edu', 'Ingeniería de Software', '2024-02-15'),
('Laura Isabel', 'Hernández Castro', 'laura.hernandez@datos.edu', 'Ciencia de Datos', '2024-01-25'),
('Diego Alejandro', 'Morales Ruiz', 'diego.morales@cyber.edu', 'Ciberseguridad', '2024-02-10'),
('Sofía Elena', 'Torres Jiménez', 'sofia.torres@cloud.edu', 'Computación en la Nube', '2024-01-30'),
('Andrés Felipe', 'Vargas González', 'andres.vargas@redes.edu', 'Redes y Comunicaciones', '2024-02-05'),
('Camila Andrea', 'Ramírez Ortiz', 'camila.ramirez@ia.edu', 'Inteligencia Artificial', '2024-01-18');

-- Estudiante inactivo para probar eliminación lógica
INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso, activo) VALUES
('Pedro Luis', 'Sánchez Díaz', 'pedro.sanchez@test.edu', 'Ingeniería DevOps', '2023-12-01', false);

-- ================================================
-- DATOS ADICIONALES PARA PRUEBAS ESPECÍFICAS
-- ================================================

-- Estudiantes con fechas variadas para probar ordenamiento
INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso) VALUES
('Valentina', 'Moreno Castillo', 'valentina.moreno@mobile.edu', 'Desarrollo Móvil', '2023-09-15'),
('Sebastián', 'Cruz Herrera', 'sebastian.cruz@blockchain.edu', 'Blockchain', '2024-03-01'),
('Isabella', 'Restrepo Ossa', 'isabella.restrepo@ux.edu', 'UX/UI Design', '2023-11-20');

-- Estudiantes con nombres similares para probar búsquedas
INSERT INTO estudiantes (nombre, apellido, email, carrera, fecha_ingreso) VALUES
('Roberto', 'Martínez Gómez', 'roberto.martinez@test2.edu', 'Ingeniería de Software', '2024-01-05'),
('Roberto Antonio', 'Vargas López', 'roberto.vargas@test3.edu', 'Ciencia de Datos', '2024-02-20');

-- ================================================
-- VERIFICACIÓN DE INSERCIÓN
-- ================================================

-- Mostrar resumen de estudiantes insertados
SELECT 
    COUNT(*) as total_estudiantes,
    COUNT(CASE WHEN activo THEN 1 END) as activos,
    COUNT(CASE WHEN NOT activo THEN 1 END) as inactivos,
    COUNT(DISTINCT carrera) as carreras_diferentes
FROM estudiantes;

-- Mostrar distribución por carrera
SELECT 
    carrera,
    COUNT(*) as cantidad
FROM estudiantes 
WHERE activo = true
GROUP BY carrera 
ORDER BY cantidad DESC;

-- ================================================
-- INSTRUCCIONES
-- ================================================

/*
ORDEN DE EJECUCIÓN:
1. Ejecutar estudiantes_schema.sql primero
2. Ejecutar este archivo (datos_ejemplo.sql)
3. Usar consultas_testing.sql para validar

DATOS INCLUIDOS:
- 14 estudiantes activos de diversas carreras
- 1 estudiante inactivo para probar eliminación lógica
- Variedad de fechas de ingreso para pruebas de ordenamiento
- Nombres similares para probar funciones de búsqueda
- Representativo de un ambiente universitario real

CARRERAS INCLUIDAS:
- Ingeniería DevOps (3 estudiantes)
- Ingeniería en Sistemas (1 estudiante)
- Ingeniería de Software (2 estudiantes)
- Ciencia de Datos (2 estudiantes)
- Ciberseguridad (1 estudiante)
- Computación en la Nube (1 estudiante)
- Redes y Comunicaciones (1 estudiante)
- Inteligencia Artificial (1 estudiante)
- Desarrollo Móvil (1 estudiante)
- Blockchain (1 estudiante)
- UX/UI Design (1 estudiante)
*/
