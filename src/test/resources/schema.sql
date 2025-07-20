-- Schema H2 para pruebas - Compatible con EstudianteRepositorioBD
-- Basado en estudiantes_schema.sql pero adaptado para H2

CREATE TABLE IF NOT EXISTS estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    edad INT NOT NULL CHECK (edad >= 16 AND edad <= 100),
    carrera VARCHAR(100) NOT NULL,
    fecha_registro DATE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_estudiantes_email ON estudiantes(email);
CREATE INDEX IF NOT EXISTS idx_estudiantes_carrera ON estudiantes(carrera);
CREATE INDEX IF NOT EXISTS idx_estudiantes_activo ON estudiantes(activo);

-- Insertar datos de prueba básicos
INSERT INTO estudiantes (nombre, email, edad, carrera, fecha_registro, activo) VALUES
('Juan Pérez', 'juan.perez@test.com', 20, 'Ingeniería Informática', '2024-01-15', true),
('María González', 'maria.gonzalez@test.com', 22, 'Psicología', '2024-01-20', true),
('Carlos López', 'carlos.lopez@test.com', 25, 'Administración', '2024-02-01', true),
('Ana Martínez', 'ana.martinez@test.com', 19, 'Enfermería', '2024-02-10', false),
('Roberto Silva', 'roberto.silva@test.com', 23, 'Ingeniería Civil', '2024-03-01', true);
