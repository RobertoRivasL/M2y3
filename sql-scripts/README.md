# Scripts SQL - Proyecto CRUD con TDD

Este directorio contiene los scripts SQL organizados para el proyecto CRUD con TDD de Roberto Rivas López.

## 📁 Estructura de Archivos

### 1. `estudiantes_schema.sql`
**Propósito**: Definición del esquema de base de datos
- Creación de la base de datos `crud_estudiantes_tdd`
- Definición de la tabla `estudiantes` con todos sus campos e índices
- Tabla de auditoría `log_eliminaciones`
- Procedimientos almacenados y funciones
- Triggers para auditoría de eliminaciones

### 2. `datos_ejemplo.sql`
**Propósito**: Datos de prueba para testing y demostración
- 15 estudiantes de ejemplo de diversas carreras
- 1 estudiante inactivo para probar eliminación lógica
- Datos representativos de un ambiente universitario real
- Consultas de verificación de inserción

### 3. `consultas_testing.sql`
**Propósito**: Consultas para validar funcionalidad
- Pruebas de operaciones CRUD completas
- Validación de restricciones e integridad
- Consultas de rendimiento con EXPLAIN
- Pruebas de procedimientos y funciones
- Consultas de auditoría y limpieza

## 🚀 Orden de Ejecución

Para configurar correctamente la base de datos, ejecutar en este orden:

```sql
1. estudiantes_schema.sql   -- Crear estructura
2. datos_ejemplo.sql        -- Insertar datos de prueba
3. consultas_testing.sql    -- Validar funcionalidad
```

## 🔧 Configuración para Diferentes Entornos

### SQLonline (Recomendado para demostración)
- URL: https://sqlonline.com/
- Seleccionar "MySQL" como motor
- Ejecutar los scripts en orden

### MySQL Local
```bash
mysql -u root -p < estudiantes_schema.sql
mysql -u root -p < datos_ejemplo.sql
mysql -u root -p < consultas_testing.sql
```

### H2 Database (Para pruebas unitarias)
Los scripts son compatibles con H2 con adaptaciones menores.

## 📊 Contenido de Datos

### Carreras Incluidas:
- **Ingeniería DevOps** (3 estudiantes) - Especialidad del autor
- **Ingeniería en Sistemas** (1 estudiante)
- **Ingeniería de Software** (2 estudiantes)
- **Ciencia de Datos** (2 estudiantes)
- **Ciberseguridad** (1 estudiante)
- **Computación en la Nube** (1 estudiante)
- **Redes y Comunicaciones** (1 estudiante)
- **Inteligencia Artificial** (1 estudiante)
- **Desarrollo Móvil** (1 estudiante)
- **Blockchain** (1 estudiante)
- **UX/UI Design** (1 estudiante)

### Características de los Datos:
- ✅ Emails únicos y válidos
- ✅ Fechas de ingreso variadas (2023-2024)
- ✅ Nombres realistas hispanos
- ✅ Un registro inactivo para probar eliminación lógica
- ✅ Distribución equilibrada por carreras

## 🧪 Validaciones Incluidas

### En `consultas_testing.sql`:
- **CRUD Completo**: Create, Read, Update, Delete
- **Validaciones**: Restricciones de integridad
- **Rendimiento**: Uso de índices con EXPLAIN
- **Integridad**: Verificación de datos nulos e inválidos
- **Auditoría**: Consultas de log de eliminaciones
- **Funciones**: Pruebas de procedimientos almacenados

## 📝 Notas Importantes

1. **Eliminación Lógica**: Se usa `activo = false` en lugar de DELETE
2. **Auditoría**: Los cambios se registran automáticamente via triggers
3. **Índices**: Optimizados para consultas frecuentes (email, carrera, nombres)
4. **Charset**: UTF-8 para soporte de caracteres especiales
5. **Compatibilidad**: MySQL 5.7+ y 8.0+

## 🔗 Integración con Java

Los scripts están diseñados para trabajar con:
- **EstudianteRepositorioBD.java**: Implementación con base de datos
- **EstudianteRepositorioTest.java**: Pruebas unitarias
- **Maven/JUnit**: Framework de testing

## 📈 Estadísticas de Testing

Después de ejecutar todos los scripts:
- **Total de registros**: 15 estudiantes
- **Estudiantes activos**: 14
- **Estudiantes inactivos**: 1
- **Carreras diferentes**: 11
- **Período de datos**: Sept 2023 - Mar 2024

---

**Autor**: Roberto Rivas López  
**Proyecto**: CRUD con TDD - Módulo 3 Portafolio 2 y 3  
**Fecha**: Julio 2025
