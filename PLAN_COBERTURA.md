# Plan de Cobertura de Código - CRUD TDD

## 📊 Estado Actual de Cobertura

### Resumen General (Estado Inicial: 27%)

| Paquete | Cobertura Actual | Líneas Cubiertas | Líneas Totales | Oportunidad de Mejora |
|---------|------------------|------------------|----------------|----------------------|
| **modelo** | 79% | 70/80 | 80 | ✅ Ya cumple objetivo |
| **servicio** | 61% | 74/111 | 111 | 🔄 Mejorar a 80%+ |
| **servicio.dto** | 53% | 32/57 | 57 | 🔄 Mejorar a 80%+ |
| **servicio.excepcion** | 50% | 16/33 | 33 | 🔄 Mejorar a 80%+ |
| **repositorio** | 22% | 95/315 | 315 | 🚨 Crítico - Implementar pruebas BD |
| **controlador** | 0% | 0/268 | 268 | 🚨 Crítico - Implementar pruebas integración |

## 🎯 Objetivo: Alcanzar 80% de Cobertura Global

### Estrategia de Mejora por Prioridad

#### **Prioridad 1: EstudianteRepositorioBD (0% → 60%)**
- **Impacto**: +19% cobertura global
- **Esfuerzo**: Medio
- **Acciones**:
  - Configurar base de datos H2 en memoria para pruebas
  - Crear `EstudianteRepositorioBDTest.java`
  - Probar todas las operaciones CRUD con BD real
  - Probar manejo de errores SQL

#### **Prioridad 2: EstudianteControlador (0% → 70%)**
- **Impacto**: +17% cobertura global
- **Esfuerzo**: Alto
- **Acciones**:
  - Crear pruebas de integración con MockMvc
  - Probar endpoints REST
  - Validar serialización JSON
  - Probar manejo de errores HTTP

#### **Prioridad 3: EstudianteDTO (53% → 85%)**
- **Impacto**: +3% cobertura global
- **Esfuerzo**: Bajo
- **Acciones**:
  - Completar pruebas de validación
  - Probar métodos equals/hashCode
  - Probar constructores adicionales

#### **Prioridad 4: ServicioException (50% → 85%)**
- **Impacto**: +2% cobertura global
- **Esfuerzo**: Bajo
- **Acciones**:
  - Probar todos los tipos de error
  - Probar constructores con causa
  - Probar métodos auxiliares

#### **Prioridad 5: EstudianteServicioImpl (61% → 85%)**
- **Impacto**: +2% cobertura global
- **Esfuerzo**: Medio
- **Acciones**:
  - Agregar casos edge adicionales
  - Probar validaciones complejas
  - Mejorar cobertura de branches

## 📋 Plan de Implementación

### Fase 1: Configuración de Infraestructura (30 min)
1. ✅ Configurar H2 para pruebas de BD
2. ✅ Agregar dependencias de testing adicionales
3. ✅ Configurar profiles de Maven

### Fase 2: Pruebas de Repositorio BD (45 min)
1. ✅ Crear `EstudianteRepositorioBDSimpleTest.java` (2 pruebas básicas)
2. 🔄 Completar `EstudianteRepositorioBDTest.java` (15 pruebas adicionales)
3. 🔄 Probar manejo de errores SQL
4. 🔄 Validar transacciones

**✅ PROGRESO ACTUAL: 34.5% cobertura (+7.5% desde inicio)**
- EstudianteRepositorioBD: 33% cobertura (278/851 líneas)
- 49 pruebas pasando exitosamente
- 2 pruebas BD funcionando con H2

### Fase 3: Pruebas de Controlador (60 min)
1. ✅ Crear `EstudianteControladorTest.java`
2. ✅ Configurar MockMvc y contexto Spring
3. ✅ Implementar pruebas de endpoints REST
4. ✅ Validar respuestas JSON y códigos HTTP

### Fase 4: Completar DTOs y Excepciones (20 min)
1. ✅ Expandir `EstudianteDTOTest.java`
2. ✅ Crear `ServicioExceptionTest.java`
3. ✅ Cubrir casos faltantes

### Fase 5: Optimizar Servicio (15 min)
1. ✅ Agregar casos edge a `EstudianteServicioTest.java`
2. ✅ Mejorar cobertura de branches

## 📈 Proyección de Resultados

| Fase | Cobertura Esperada | Incremento |
|------|-------------------|------------|
| Inicial | 27% | - |
| Fase 1-2 | 55% | +28% |
| Fase 3 | 70% | +15% |
| Fase 4 | 77% | +7% |
| Fase 5 | **82%** | +5% |

## 🔧 Herramientas y Tecnologías

### Testing
- **JUnit 5**: Framework de pruebas principal
- **Mockito**: Mocking para pruebas unitarias
- **H2 Database**: Base de datos en memoria para pruebas
- **Spring Test**: Para pruebas de integración
- **MockMvc**: Para pruebas de controladores REST

### Cobertura
- **JaCoCo**: Análisis de cobertura de código
- **Maven Surefire**: Ejecución de pruebas
- **Reports**: HTML y XML para CI/CD

## 📝 Métricas de Calidad

### Objetivos de Cobertura por Tipo
- **Líneas**: ≥ 80%
- **Branches**: ≥ 75%
- **Métodos**: ≥ 85%
- **Clases**: 100%

### Criterios de Aceptación
- ✅ Sin pruebas que fallen
- ✅ Sin código muerto
- ✅ Cobertura de casos edge importantes
- ✅ Pruebas rápidas (< 10 segundos total)
- ✅ Pruebas independientes y determinísticas

## 🚀 Comandos de Validación

```bash
# Ejecutar todas las pruebas
mvn clean test

# Generar reporte de cobertura
mvn jacoco:report

# Verificar cobertura mínima
mvn jacoco:check -Djacoco.haltOnFailure=true

# Ver reporte en navegador
start target/site/jacoco/index.html
```

## 📋 Checklist de Implementación

### Configuración
- [ ] H2 configurado para pruebas
- [ ] Dependencies actualizadas
- [ ] Profiles de Maven configurados

### Pruebas de Repositorio
- [ ] `EstudianteRepositorioBDTest.java` creado
- [ ] Pruebas CRUD implementadas
- [ ] Manejo de errores probado
- [ ] Cobertura BD ≥ 60%

### Pruebas de Controlador
- [ ] `EstudianteControladorTest.java` creado
- [ ] MockMvc configurado
- [ ] Endpoints REST probados
- [ ] Cobertura controlador ≥ 70%

### Completar Cobertura
- [ ] DTOs mejorados a 85%
- [ ] Excepciones mejoradas a 85%
- [ ] Servicio optimizado a 85%

### Validación Final
- [ ] Cobertura global ≥ 80%
- [ ] Todas las pruebas pasan
- [ ] Reporte JaCoCo generado
- [ ] Documentación actualizada

---

**Autor**: Roberto Rivas López  
**Fecha**: Julio 2025  
**Objetivo**: Alcanzar 80%+ cobertura de código en proyecto CRUD con TDD
