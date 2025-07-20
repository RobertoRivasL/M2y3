# 🎓 Sistema CRUD de Estudiantes con TDD

**Portafolio Final Módulos 2 y 3: Testing Ágil + TDD en Automatización de Pruebas**

👨‍💻 **Desarrollado por:** Roberto Rivas López  
📅 **Fecha:** Julio 2025  
🏫 **Institución:** [Tu Institución Educativa]  
📦 **Versión:** 1.0.0

---

## 📋 Tabla de Contenidos

- [Descripción del Proyecto](#-descripción-del-proyecto)
- [Objetivos de Aprendizaje](#-objetivos-de-aprendizaje)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Principios Aplicados](#-principios-aplicados)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Uso del Sistema](#-uso-del-sistema)
- [Ejecución de Pruebas](#-ejecución-de-pruebas)
- [Cobertura de Código](#-cobertura-de-código)
- [Ciclos TDD Implementados](#-ciclos-tdd-implementados)
- [Plan de Testing Ágil](#-plan-de-testing-ágil)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Configuración de Base de Datos](#-configuración-de-base-de-datos)
- [Reflexión Personal](#-reflexión-personal)
- [Contribución](#-contribución)

---

## 🚀 Descripción del Proyecto

Este proyecto es un **Sistema CRUD (Create, Read, Update, Delete) de Estudiantes** desarrollado completamente con **Test-Driven Development (TDD)** aplicando metodologías ágiles y principios de diseño sólidos.

### Características Principales

- ✅ **CRUD completo** para gestión de estudiantes
- ✅ **12+ ciclos TDD** (RED-GREEN-REFACTOR)
- ✅ **Principios SOLID** implementados
- ✅ **Cobertura >= 80%** con JaCoCo
- ✅ **Mockito** para testing de dependencias
- ✅ **Arquitectura por capas** bien definida
- ✅ **Doble implementación**: Memoria y Base de Datos
- ✅ **Testing Ágil** integrado en el ciclo de desarrollo

---

## 🎯 Objetivos de Aprendizaje

### Módulo 2: Testing Ágil
- [x] Comprender la importancia del testing en entornos ágiles
- [x] Aplicar criterios de aceptación en historias de usuario
- [x] Integrar testing en sprints de desarrollo
- [x] Definir "Terminado" para incrementos de producto

### Módulo 3: TDD en Automatización
- [x] Dominar el ciclo RED-GREEN-REFACTOR
- [x] Escribir pruebas unitarias efectivas con JUnit 5
- [x] Utilizar Mockito para aislar dependencias
- [x] Medir y mantener cobertura de código >= 80%
- [x] Refactorizar aplicando principios SOLID

---

## 🛠️ Tecnologías Utilizadas

### Core
- **Java 17** - Lenguaje de programación principal
- **Maven 3.9.10** - Gestión de dependencias y construcción

### Testing
- **JUnit 5.10.1** - Framework de testing unitario
- **Mockito 5.8.0** - Framework para mocking
- **JaCoCo 0.8.11** - Análisis de cobertura de código

### Base de Datos
- **H2 2.2.224** - Base de datos en memoria para testing
- **MySQL Connector 8.0.33** - Driver para SQLonline
- **SQLonline** - Servicio de base de datos gratuito

### Desarrollo
- **Visual Studio Code** - IDE principal
- **Git** - Control de versiones
- **GitHub** - Repositorio remoto

---

## 🏗️ Arquitectura del Sistema

El sistema implementa una **arquitectura por capas** siguiendo principios de diseño sólidos:

```
┌─────────────────────────────────────┐
│           Controlador               │ ← Patrón Facade
├─────────────────────────────────────┤
│            Servicio                 │ ← Lógica de negocio
├─────────────────────────────────────┤
│           Repositorio               │ ← Acceso a datos
├─────────────────────────────────────┤
│            Modelo                   │ ← Entidades de dominio
└─────────────────────────────────────┘
```

### Componentes Principales

1. **🎮 Controlador**: Interfaz de usuario y orquestación
2. **🎯 Servicio**: Lógica de negocio y validaciones
3. **💾 Repositorio**: Abstracción de acceso a datos
4. **📄 Modelo**: Entidades y objetos de dominio
5. **🔄 DTO**: Objetos de transferencia de datos

---

## 🎪 Principios Aplicados

### Principios SOLID

| Principio | Implementación | Ejemplo en el Código |
|-----------|----------------|----------------------|
| **SRP** - Responsabilidad Única | Cada clase tiene una sola razón para cambiar | `Estudiante` solo maneja datos del estudiante |
| **OCP** - Abierto/Cerrado | Extensible sin modificar código existente | Interface `IEstudianteRepositorio` |
| **LSP** - Sustitución de Liskov | Subtipos reemplazables | `EstudianteRepositorioMemoria` vs `EstudianteRepositorioBD` |
| **ISP** - Segregación de Interfaces | Interfaces específicas y cohesivas | `IEstudianteServicio` vs `IEstudianteRepositorio` |
| **DIP** - Inversión de Dependencias | Depender de abstracciones | Servicio depende de `IEstudianteRepositorio` |

### Otros Principios

- **🧩 Modularidad**: Código organizado en módulos cohesivos
- **🔒 Encapsulación**: Datos y comportamiento encapsulados
- **🎭 Abstracción**: Interfaces bien definidas
- **🚪 Separación de Intereses**: Cada capa tiene su responsabilidad específica

---

## ⚙️ Instalación y Configuración

### Prerequisitos

- **Java 17+** instalado
- **Maven 3.9+** configurado
- **Git** para clonar el repositorio
- **Visual Studio Code** (recomendado)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/roberto-rivas/crud-tdd-estudiantes.git
   cd crud-tdd-estudiantes
   ```

2. **Verificar instalación de Java y Maven**
   ```bash
   java --version
   mvn --version
   ```

3. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

4. **Ejecutar las pruebas**
   ```bash
   mvn test
   ```

5. **Generar el JAR ejecutable**
   ```bash
   mvn clean package
   ```

---

## 🚀 Uso del Sistema

### Ejecución con Maven

```bash
# Ejecutar con repositorio en memoria (recomendado para pruebas)
mvn exec:java -Dexec.mainClass="com.roberto.rivas.AplicacionPrincipal" -Dexec.args="--memory"

# Ejecutar con base de datos
mvn exec:java -Dexec.mainClass="com.roberto.rivas.AplicacionPrincipal" -Dexec.args="--db"
```

### Ejecución con JAR

```bash
# Generar JAR
mvn clean package

# Ejecutar JAR
java -jar target/crud-tdd-proyecto-1.0.0.jar --memory
java -jar target/crud-tdd-proyecto-1.0.0.jar --db
```

### Opciones de Línea de Comandos

| Opción | Descripción |
|--------|-------------|
| `--memory`, `--mem` | Usar repositorio en memoria |
| `--database`, `--db` | Usar base de datos MySQL |
| `--help`, `-h` | Mostrar ayuda |

### Funcionalidades Disponibles

1. **➕ Registrar nuevo estudiante**
2. **🔍 Buscar estudiante por ID**
3. **📧 Buscar estudiante por email**
4. **📋 Listar todos los estudiantes**
5. **🎓 Listar estudiantes por carrera**
6. **✏️ Actualizar estudiante**
7. **🗑️ Eliminar estudiante (lógico)**
8. **🔄 Reactivar estudiante**
9. **📊 Mostrar estadísticas**
10. **🎯 Demostración completa**

---

## 🧪 Ejecución de Pruebas

### Comandos de Testing

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar pruebas específicas
mvn test -Dtest=EstudianteTest
mvn test -Dtest=EstudianteRepositorioTest
mvn test -Dtest=EstudianteServicioTest

# Ejecutar pruebas con reporte detallado
mvn test -Dmaven.test.failure.ignore=true

# Ejecutar pruebas en modo debug
mvn test -Dmaven.surefire.debug
```

### Estructura de Pruebas

```
src/test/java/
├── com/roberto/rivas/
│   ├── modelo/
│   │   └── EstudianteTest.java          # Ciclos TDD 1-4
│   ├── repositorio/
│   │   └── EstudianteRepositorioTest.java # Ciclos TDD 5-8
│   └── servicio/
│       └── EstudianteServicioTest.java  # Ciclos TDD 9-12
```

### Tipos de Pruebas Implementadas

- **✅ Pruebas Unitarias**: 16+ casos de prueba
- **✅ Pruebas de Integración**: Componentes trabajando juntos
- **✅ Pruebas de Validación**: Reglas de negocio
- **✅ Pruebas con Mocks**: Aislamiento de dependencias

---

## 📊 Cobertura de Código

### Generar Reporte de Cobertura

```bash
# Ejecutar pruebas y generar reporte
mvn clean test jacoco:report

# Ver reporte en navegador
open target/site/jacoco/index.html
```

### Verificar Umbral de Cobertura

```bash
# Verificar que cobertura >= 80%
mvn jacoco:check
```

### Métricas Objetivo

- **🎯 Cobertura de Instrucciones**: >= 80%
- **🎯 Cobertura de Ramas**: >= 75%
- **🎯 Cobertura de Métodos**: >= 85%
- **🎯 Cobertura de Clases**: >= 90%

---

## 🔄 Ciclos TDD Implementados

### Metodología RED-GREEN-REFACTOR

| Ciclo | Componente | Funcionalidad | Estado |
|-------|------------|---------------|--------|
| 1-4 | **Modelo** | Validaciones de Estudiante | ✅ |
| 5-8 | **Repositorio** | Operaciones CRUD básicas | ✅ |
| 9-12 | **Servicio** | Lógica de negocio | ✅ |
| 13+ | **Integración** | Casos especiales | ✅ |

### Detalle de Ciclos

#### 🔴 Ciclos 1-4: Modelo Estudiante
- **RED**: Escribir pruebas para validaciones
- **GREEN**: Implementar validaciones mínimas
- **REFACTOR**: Mejorar encapsulación y abstracción

#### 🔴 Ciclos 5-8: Repositorio
- **RED**: Pruebas para operaciones CRUD
- **GREEN**: Implementación básica con Map
- **REFACTOR**: Optimizar y añadir validaciones

#### 🔴 Ciclos 9-12: Servicio
- **RED**: Pruebas de lógica de negocio
- **GREEN**: Implementar reglas de negocio
- **REFACTOR**: Aplicar principios SOLID

---

## 📋 Plan de Testing Ágil

### Historia de Usuario Principal

> **Como** administrador del sistema  
> **Quiero** gestionar información de estudiantes  
> **Para** mantener un registro actualizado y confiable

### Criterios de Aceptación

#### ✅ Epic: Gestión de Estudiantes

1. **Historia US-001**: Registrar Estudiante
   - [ ] Crear estudiante con datos válidos
   - [ ] Validar email único
   - [ ] Rechazar datos incompletos
   - [ ] Asignar ID automático

2. **Historia US-002**: Consultar Estudiante
   - [ ] Buscar por ID existente
   - [ ] Buscar por email válido
   - [ ] Manejar búsquedas sin resultados
   - [ ] Listar todos los activos

3. **Historia US-003**: Actualizar Estudiante
   - [ ] Modificar datos existentes
   - [ ] Validar email único en actualización
   - [ ] Rechazar estudiante inexistente
   - [ ] Mantener auditoría de cambios

4. **Historia US-004**: Eliminar Estudiante
   - [ ] Eliminación lógica (activo = false)
   - [ ] Confirmar antes de eliminar
   - [ ] No mostrar en listados activos
   - [ ] Permitir reactivación posterior

### Definición de "Terminado" (DoD)

- [x] ✅ Código implementado y compilando
- [x] ✅ Pruebas unitarias pasando (>= 80% cobertura)
- [x] ✅ Principios SOLID aplicados
- [x] ✅ Documentación actualizada
- [x] ✅ Sin deuda técnica significativa
- [x] ✅ Validaciones de negocio implementadas
- [x] ✅ Manejo de errores robusto

### Roles en el Sprint

| Rol | Responsabilidad | Persona |
|-----|----------------|---------|
| **Developer** | Implementar funcionalidades con TDD | Roberto Rivas |
| **Tester** | Diseñar y ejecutar casos de prueba | Roberto Rivas |
| **DevOps** | Configurar pipeline y reportes | Roberto Rivas |

---

## 📁 Estructura del Proyecto

```
crud-tdd-proyecto/
├── 📄 pom.xml                          # Configuración Maven
├── 📖 README.md                        # Documentación principal
├── 📋 PLAN_TESTING_AGIL.md            # Plan de testing detallado
├── 💭 REFLEXION_PERSONAL.md           # Reflexión del aprendizaje
├── 📊 PRINCIPIOS_SOLID.md             # Documentación de principios
├── src/
│   ├── main/java/com/roberto/rivas/
│   │   ├── 🚀 AplicacionPrincipal.java # Punto de entrada
│   │   ├── controlador/
│   │   │   └── 🎮 EstudianteControlador.java
│   │   ├── servicio/
│   │   │   ├── 🎯 IEstudianteServicio.java
│   │   │   ├── 🎯 EstudianteServicioImpl.java
│   │   │   ├── dto/
│   │   │   │   └── 🔄 EstudianteDTO.java
│   │   │   └── excepcion/
│   │   │       └── ⚠️ ServicioException.java
│   │   ├── repositorio/
│   │   │   ├── 💾 IEstudianteRepositorio.java
│   │   │   ├── 💾 EstudianteRepositorioMemoria.java
│   │   │   ├── 💾 EstudianteRepositorioBD.java
│   │   │   └── ⚠️ RepositorioException.java
│   │   └── modelo/
│   │       └── 📄 Estudiante.java
│   └── test/java/com/roberto/rivas/
│       ├── modelo/
│       │   └── 🧪 EstudianteTest.java
│       ├── repositorio/
│       │   └── 🧪 EstudianteRepositorioTest.java
│       └── servicio/
│           └── 🧪 EstudianteServicioTest.java
├── sql-scripts/
│   ├── 🗄️ estudiantes_schema.sql      # Script de creación
│   ├── 📊 datos_ejemplo.sql           # Datos de prueba
│   └── 📋 consultas_testing.sql       # Consultas para testing
├── target/
│   ├── classes/                        # Clases compiladas
│   ├── test-classes/                   # Pruebas compiladas
│   ├── site/jacoco/                    # Reportes de cobertura
│   └── crud-tdd-proyecto-1.0.0.jar    # JAR ejecutable
└── docs/
    ├── 📸 screenshots/                 # Capturas de pantalla
    ├── 📊 diagramas/                  # Diagramas de arquitectura
    └── 📝 notas/                      # Notas de desarrollo
```

---

## 🗄️ Configuración de Base de Datos

### Opción 1: SQLonline (Recomendado para Demostración)

1. **Registrarse en SQLonline**
   ```
   🌐 URL: https://sqlonline.com/
   📧 Crear cuenta gratuita
   🗄️ Seleccionar MySQL
   ```

2. **Ejecutar Script de Inicialización**
   ```sql
   # Copiar contenido de sql-scripts/estudiantes_schema.sql
   # Pegar y ejecutar en SQLonline
   ```

3. **Configurar Credenciales**
   ```java
   // En EstudianteRepositorioBD.java
   private final String url = "jdbc:mysql://sql.freedb.tech:3306/tu_base_datos";
   private final String usuario = "tu_usuario";
   private final String contrasena = "tu_contraseña";
   ```

### Opción 2: H2 en Memoria (Para Testing)

```java
// Configuración automática para pruebas
// No requiere configuración adicional
```

### Opción 3: MySQL Local

```properties
# application.properties (si se usa Spring Boot en futuro)
spring.datasource.url=jdbc:mysql://localhost:3306/crud_estudiantes
spring.datasource.username=root
spring.datasource.password=tu_password
```

---

## 💡 Reflexión Personal

### ¿Qué aprendí sobre Testing Ágil y TDD?

Durante el desarrollo de este proyecto, experimenté de primera mano cómo **TDD transforma completamente la forma de programar**. El ciclo RED-GREEN-REFACTOR me obligó a pensar primero en el comportamiento esperado antes de escribir código, lo que resultó en un diseño más limpio y modular.

**Aprendizajes clave:**
- TDD no es solo sobre testing, sino sobre **diseño dirigido por pruebas**
- Las pruebas actúan como **documentación viva** del sistema
- La refactorización constante mantiene el código limpio y mantenible
- Los principios SOLID emergen naturalmente cuando se sigue TDD

### ¿Qué dificultades enfrenté y cómo las resolví?

**Dificultad 1: Configuración inicial de Mockito**
- *Problema*: Integración compleja con JUnit 5
- *Solución*: Uso de `@ExtendWith(MockitoExtension.class)` y configuración Maven correcta

**Dificultad 2: Diseño de interfaces coherentes**
- *Problema*: Interfaces muy generales que violaban ISP
- *Solución*: Segregación en interfaces específicas por responsabilidad

**Dificultad 3: Cobertura de código en casos edge**
- *Problema*: Alcanzar 80% incluyendo manejo de excepciones
- *Solución*: Pruebas específicas para cada rama de decisión

### ¿Cómo me sentí trabajando con ciclos TDD?

Inicialmente fue **desafiante** porque requiere cambiar la mentalidad de "código primero" a "prueba primero". Sin embargo, después de los primeros ciclos, experimenté:

- **Mayor confianza** en el código desarrollado
- **Diseño más limpio** que emerge naturalmente
- **Feedback inmediato** sobre la calidad del diseño
- **Refactorización segura** respaldada por pruebas

### ¿Qué mejorarías si volvieras a realizar este proyecto?

1. **Implementar CI/CD desde el inicio** con GitHub Actions
2. **Añadir más tipos de pruebas** (integración, aceptación)
3. **Usar Spring Boot** para aprovechar inyección de dependencias automática
4. **Implementar logging estructurado** para mejor observabilidad
5. **Añadir validaciones más robustas** usando Bean Validation
6. **Crear interfaz web** con Spring MVC o React

---

## 🤝 Contribución

### Cómo Contribuir

1. **Fork** el repositorio
2. **Crear** una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. **Escribir pruebas** siguiendo TDD
4. **Implementar** la funcionalidad
5. **Verificar** cobertura >= 80%
6. **Commit** con mensaje descriptivo
7. **Push** a tu rama (`git push origin feature/nueva-funcionalidad`)
8. **Crear** Pull Request

### Estándares de Código

- ✅ Seguir convenciones de naming en español
- ✅ Aplicar principios SOLID
- ✅ Mantener cobertura >= 80%
- ✅ Documentar métodos públicos
- ✅ Usar TDD para nueva funcionalidad

### Ideas para Futuras Mejoras

- 🚀 **API REST** con Spring Boot
- 🎨 **Interfaz Web** con React o Thymeleaf
- 🔐 **Autenticación y autorización**
- 📊 **Dashboard con métricas**
- 🐳 **Containerización** con Docker
- ☁️ **Despliegue en cloud** (AWS, Azure)
- 📱 **Aplicación móvil** con React Native

---

## 📞 Contacto y Soporte

**👨‍💻 Desarrollador:** Roberto Rivas López  
**📧 Email:** roberto.rivas@ejemplo.com  
**🔗 LinkedIn:** [linkedin.com/in/roberto-rivas-lopez](https://linkedin.com/in/roberto-rivas-lopez)  
**🐙 GitHub:** [github.com/roberto-rivas](https://github.com/roberto-rivas)  

### Reportar Issues

🐛 **Bugs:** [GitHub Issues](https://github.com/roberto-rivas/crud-tdd-estudiantes/issues)  
💡 **Features:** [GitHub Discussions](https://github.com/roberto-rivas/crud-tdd-estudiantes/discussions)  
📖 **Documentación:** [Wiki del Proyecto](https://github.com/roberto-rivas/crud-tdd-estudiantes/wiki)

---

## 📜 Licencia

Este proyecto está licenciado bajo la **Licencia MIT** - ver el archivo [LICENSE](LICENSE) para detalles.

```
MIT License

Copyright (c) 2025 Roberto Rivas López

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🎉 Agradecimientos

- **🏫 [Tu Institución]** por la formación en metodologías ágiles
- **👥 Comunidad Java** por las mejores prácticas
- **📚 Autores de Clean Code** por los principios de diseño
- **🧪 Creadores de JUnit y Mockito** por las herramientas de testing
- **🌟 Comunidad Open Source** por el conocimiento compartido

---

**⭐ Si este proyecto te fue útil, ¡considera darle una estrella en GitHub!**

**🚀 ¡Feliz desarrollo con TDD!**