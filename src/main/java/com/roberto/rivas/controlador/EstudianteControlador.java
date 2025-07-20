package com.roberto.rivas.controlador;

import com.roberto.rivas.repositorio.IEstudianteRepositorio;
import com.roberto.rivas.repositorio.EstudianteRepositorioMemoria;
import com.roberto.rivas.repositorio.EstudianteRepositorioBD;
import com.roberto.rivas.servicio.IEstudianteServicio;
import com.roberto.rivas.servicio.EstudianteServicioImpl;
import com.roberto.rivas.servicio.dto.EstudianteDTO;
import com.roberto.rivas.servicio.excepcion.ServicioException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controlador principal del sistema CRUD de Estudiantes
 * Aplica patrón Facade y principio de Responsabilidad Única
 * Integra todas las capas del sistema siguiendo arquitectura por capas
 * 
 * @author Roberto Rivas Lopez
 */
public class EstudianteControlador {

    private final IEstudianteServicio estudianteServicio;
    private final Scanner scanner;
    private boolean sistemaActivo;

    /**
     * Constructor que aplica Inyección de Dependencias
     * Permite configurar tipo de repositorio (memoria vs base de datos)
     */
    public EstudianteControlador(boolean usarBaseDatos) {
        // Patrón Factory para crear repositorio según configuración
        IEstudianteRepositorio repositorio = usarBaseDatos ? 
            new EstudianteRepositorioBD() : 
            new EstudianteRepositorioMemoria();
            
        this.estudianteServicio = new EstudianteServicioImpl(repositorio);
        this.scanner = new Scanner(System.in);
        this.sistemaActivo = true;
        
        System.out.println("🚀 Sistema CRUD de Estudiantes - TDD Iniciado");
        System.out.println("📚 Repositorio: " + (usarBaseDatos ? "Base de Datos" : "Memoria"));
        System.out.println("👨‍💻 Desarrollado por: Roberto Rivas López");
    }

    /**
     * Método principal que ejecuta el menú interactivo
     */
    public void ejecutar() {
        mostrarBienvenida();
        
        while (sistemaActivo) {
            try {
                mostrarMenu();
                int opcion = leerOpcion();
                procesarOpcion(opcion);
            } catch (Exception e) {
                manejarError("Error inesperado en el sistema", e);
            }
        }
        
        cerrarSistema();
    }

    /**
     * Procesar la opción seleccionada por el usuario
     * Aplica patrón Command implícito
     */
    private void procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1 -> registrarNuevoEstudiante();
                case 2 -> buscarEstudiantePorId();
                case 3 -> buscarEstudiantePorEmail();
                case 4 -> listarTodosLosEstudiantes();
                case 5 -> listarEstudiantesPorCarrera();
                case 6 -> actualizarEstudiante();
                case 7 -> eliminarEstudiante();
                case 8 -> reactivarEstudiante();
                case 9 -> mostrarEstadisticas();
                case 10 -> ejecutarDemostracionCompleta();
                case 0 -> sistemaActivo = false;
                default -> System.out.println("❌ Opción no válida. Intente nuevamente.");
            }
        } catch (ServicioException e) {
            manejarErrorServicio(e);
        } catch (Exception e) {
            manejarError("Error al procesar la opción", e);
        }
    }

    // ================================================
    // OPERACIONES CRUD (Casos de uso principales)
    // ================================================

    private void registrarNuevoEstudiante() throws ServicioException {
        System.out.println("\n📝 REGISTRAR NUEVO ESTUDIANTE");
        System.out.println("================================");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Carrera: ");
        String carrera = scanner.nextLine().trim();
        
        EstudianteDTO nuevoEstudiante = new EstudianteDTO(nombre, apellido, email, carrera);
        EstudianteDTO estudianteCreado = estudianteServicio.registrarEstudiante(nuevoEstudiante);
        
        System.out.println("✅ Estudiante registrado exitosamente:");
        mostrarDetallesEstudiante(estudianteCreado);
    }

    private void buscarEstudiantePorId() throws ServicioException {
        System.out.println("\n🔍 BUSCAR ESTUDIANTE POR ID");
        System.out.println("============================");
        
        System.out.print("ID del estudiante: ");
        Long id = leerLong();
        
        Optional<EstudianteDTO> estudiante = estudianteServicio.buscarEstudiantePorId(id);
        
        if (estudiante.isPresent()) {
            System.out.println("✅ Estudiante encontrado:");
            mostrarDetallesEstudiante(estudiante.get());
        } else {
            System.out.println("❌ No se encontró estudiante con ID: " + id);
        }
    }

    private void buscarEstudiantePorEmail() throws ServicioException {
        System.out.println("\n📧 BUSCAR ESTUDIANTE POR EMAIL");
        System.out.println("===============================");
        
        System.out.print("Email del estudiante: ");
        String email = scanner.nextLine().trim();
        
        Optional<EstudianteDTO> estudiante = estudianteServicio.buscarEstudiantePorEmail(email);
        
        if (estudiante.isPresent()) {
            System.out.println("✅ Estudiante encontrado:");
            mostrarDetallesEstudiante(estudiante.get());
        } else {
            System.out.println("❌ No se encontró estudiante con email: " + email);
        }
    }

    private void listarTodosLosEstudiantes() throws ServicioException {
        System.out.println("\n📋 LISTA DE TODOS LOS ESTUDIANTES ACTIVOS");
        System.out.println("==========================================");
        
        List<EstudianteDTO> estudiantes = estudianteServicio.listarEstudiantesActivos();
        
        if (estudiantes.isEmpty()) {
            System.out.println("📭 No hay estudiantes registrados.");
        } else {
            System.out.println("📊 Total de estudiantes activos: " + estudiantes.size());
            System.out.println();
            
            for (EstudianteDTO estudiante : estudiantes) {
                mostrarResumenEstudiante(estudiante);
            }
        }
    }

    private void listarEstudiantesPorCarrera() throws ServicioException {
        System.out.println("\n🎓 LISTAR ESTUDIANTES POR CARRERA");
        System.out.println("==================================");
        
        System.out.print("Nombre de la carrera: ");
        String carrera = scanner.nextLine().trim();
        
        List<EstudianteDTO> estudiantes = estudianteServicio.listarEstudiantesPorCarrera(carrera);
        
        if (estudiantes.isEmpty()) {
            System.out.println("📭 No hay estudiantes en la carrera: " + carrera);
        } else {
            System.out.println("📊 Estudiantes en " + carrera + ": " + estudiantes.size());
            System.out.println();
            
            for (EstudianteDTO estudiante : estudiantes) {
                mostrarResumenEstudiante(estudiante);
            }
        }
    }

    private void actualizarEstudiante() throws ServicioException {
        System.out.println("\n✏️ ACTUALIZAR ESTUDIANTE");
        System.out.println("=========================");
        
        System.out.print("ID del estudiante a actualizar: ");
        Long id = leerLong();
        
        Optional<EstudianteDTO> estudianteExistente = estudianteServicio.buscarEstudiantePorId(id);
        
        if (!estudianteExistente.isPresent()) {
            System.out.println("❌ No se encontró estudiante con ID: " + id);
            return;
        }
        
        EstudianteDTO estudiante = estudianteExistente.get();
        System.out.println("📄 Datos actuales:");
        mostrarDetallesEstudiante(estudiante);
        
        System.out.println("\n📝 Ingrese los nuevos datos (Enter para mantener actual):");
        
        System.out.print("Nombre [" + estudiante.getNombre() + "]: ");
        String nuevoNombre = scanner.nextLine().trim();
        if (!nuevoNombre.isEmpty()) {
            estudiante.setNombre(nuevoNombre);
        }
        
        System.out.print("Apellido [" + estudiante.getApellido() + "]: ");
        String nuevoApellido = scanner.nextLine().trim();
        if (!nuevoApellido.isEmpty()) {
            estudiante.setApellido(nuevoApellido);
        }
        
        System.out.print("Email [" + estudiante.getEmail() + "]: ");
        String nuevoEmail = scanner.nextLine().trim();
        if (!nuevoEmail.isEmpty()) {
            estudiante.setEmail(nuevoEmail);
        }
        
        System.out.print("Carrera [" + estudiante.getCarrera() + "]: ");
        String nuevaCarrera = scanner.nextLine().trim();
        if (!nuevaCarrera.isEmpty()) {
            estudiante.setCarrera(nuevaCarrera);
        }
        
        EstudianteDTO estudianteActualizado = estudianteServicio.actualizarEstudiante(estudiante);
        
        System.out.println("✅ Estudiante actualizado exitosamente:");
        mostrarDetallesEstudiante(estudianteActualizado);
    }

    private void eliminarEstudiante() throws ServicioException {
        System.out.println("\n🗑️ ELIMINAR ESTUDIANTE");
        System.out.println("=======================");
        
        System.out.print("ID del estudiante a eliminar: ");
        Long id = leerLong();
        
        Optional<EstudianteDTO> estudiante = estudianteServicio.buscarEstudiantePorId(id);
        
        if (!estudiante.isPresent()) {
            System.out.println("❌ No se encontró estudiante con ID: " + id);
            return;
        }
        
        System.out.println("📄 Estudiante a eliminar:");
        mostrarDetallesEstudiante(estudiante.get());
        
        System.out.print("¿Confirma la eliminación? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if ("s".equals(confirmacion) || "si".equals(confirmacion)) {
            boolean eliminado = estudianteServicio.eliminarEstudiante(id);
            
            if (eliminado) {
                System.out.println("✅ Estudiante eliminado exitosamente (eliminación lógica).");
            } else {
                System.out.println("❌ No se pudo eliminar el estudiante.");
            }
        } else {
            System.out.println("❌ Eliminación cancelada.");
        }
    }

    private void reactivarEstudiante() throws ServicioException {
        System.out.println("\n🔄 REACTIVAR ESTUDIANTE");
        System.out.println("========================");
        
        System.out.print("ID del estudiante a reactivar: ");
        Long id = leerLong();
        
        EstudianteDTO estudianteReactivado = estudianteServicio.reactivarEstudiante(id);
        
        System.out.println("✅ Estudiante reactivado exitosamente:");
        mostrarDetallesEstudiante(estudianteReactivado);
    }

    private void mostrarEstadisticas() throws ServicioException {
        System.out.println("\n📊 ESTADÍSTICAS DEL SISTEMA");
        System.out.println("============================");
        
        Map<String, Object> estadisticas = estudianteServicio.obtenerEstadisticas();
        
        System.out.println("👥 Total estudiantes activos: " + estadisticas.get("totalEstudiantesActivos"));
        
        @SuppressWarnings("unchecked")
        Map<String, Long> estudiantesPorCarrera = (Map<String, Long>) estadisticas.get("estudiantesPorCarrera");
        
        if (estudiantesPorCarrera != null && !estudiantesPorCarrera.isEmpty()) {
            System.out.println("\n🎓 Estudiantes por carrera:");
            estudiantesPorCarrera.forEach((carrera, cantidad) -> 
                System.out.println("   • " + carrera + ": " + cantidad + " estudiantes"));
        }
        
        System.out.println("📅 Fecha de generación: " + estadisticas.get("fechaGeneracion"));
    }

    // ================================================
    // DEMOSTRACIÓN COMPLETA DEL SISTEMA
    // ================================================

    private void ejecutarDemostracionCompleta() {
        System.out.println("\n🎯 DEMOSTRACIÓN COMPLETA DEL SISTEMA TDD");
        System.out.println("=========================================");
        System.out.println("Esta demostración mostrará todas las funcionalidades desarrolladas con TDD:");
        
        try {
            // Demostrar creación (CREATE)
            System.out.println("\n1️⃣ DEMOSTRANDO CREACIÓN DE ESTUDIANTES...");
            EstudianteDTO demo1 = new EstudianteDTO("Demo", "Usuario", "demo@tdd.test", "Ingeniería TDD");
            EstudianteDTO creado = estudianteServicio.registrarEstudiante(demo1);
            System.out.println("   ✅ Estudiante creado con ID: " + creado.getId());
            
            // Demostrar lectura (READ)
            System.out.println("\n2️⃣ DEMOSTRANDO BÚSQUEDA...");
            Optional<EstudianteDTO> encontrado = estudianteServicio.buscarEstudiantePorId(creado.getId());
            System.out.println("   ✅ Estudiante encontrado: " + encontrado.get().getNombreCompleto());
            
            // Demostrar actualización (UPDATE)
            System.out.println("\n3️⃣ DEMOSTRANDO ACTUALIZACIÓN...");
            creado.setNombre("Demo Actualizado");
            EstudianteDTO actualizado = estudianteServicio.actualizarEstudiante(creado);
            System.out.println("   ✅ Estudiante actualizado: " + actualizado.getNombreCompleto());
            
            // Demostrar eliminación (DELETE)
            System.out.println("\n4️⃣ DEMOSTRANDO ELIMINACIÓN LÓGICA...");
            boolean eliminado = estudianteServicio.eliminarEstudiante(creado.getId());
            System.out.println("   ✅ Estudiante eliminado: " + eliminado);
            
            // Demostrar listado
            System.out.println("\n5️⃣ DEMOSTRANDO LISTADO DE ACTIVOS...");
            List<EstudianteDTO> activos = estudianteServicio.listarEstudiantesActivos();
            System.out.println("   ✅ Total estudiantes activos: " + activos.size());
            
            // Demostrar estadísticas
            System.out.println("\n6️⃣ DEMOSTRANDO ESTADÍSTICAS...");
            Map<String, Object> stats = estudianteServicio.obtenerEstadisticas();
            System.out.println("   ✅ Estadísticas generadas correctamente");
            
            System.out.println("\n🎉 ¡DEMOSTRACIÓN COMPLETADA EXITOSAMENTE!");
            System.out.println("   📋 Todas las operaciones CRUD funcionan correctamente");
            System.out.println("   🧪 TDD aplicado en 12+ ciclos de desarrollo");
            System.out.println("   🏗️ Principios SOLID implementados");
            System.out.println("   🔧 Mockito utilizado para testing");
            
        } catch (Exception e) {
            manejarError("Error en la demostración", e);
        }
    }

    // ================================================
    // MÉTODOS DE UTILIDAD Y PRESENTACIÓN
    // ================================================

    private void mostrarBienvenida() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎓 SISTEMA CRUD DE ESTUDIANTES CON TDD");
        System.out.println("👨‍💻 Desarrollado por: Roberto Rivas López");
        System.out.println("📚 Portafolio Final Módulos 2 y 3");
        System.out.println("🧪 Testing Ágil + TDD en Automatización de Pruebas");
        System.out.println("=".repeat(60));
    }

    private void mostrarMenu() {
        System.out.println("\n📋 MENÚ PRINCIPAL");
        System.out.println("=================");
        System.out.println("1️⃣  Registrar nuevo estudiante");
        System.out.println("2️⃣  Buscar estudiante por ID");
        System.out.println("3️⃣  Buscar estudiante por email");
        System.out.println("4️⃣  Listar todos los estudiantes");
        System.out.println("5️⃣  Listar estudiantes por carrera");
        System.out.println("6️⃣  Actualizar estudiante");
        System.out.println("7️⃣  Eliminar estudiante");
        System.out.println("8️⃣  Reactivar estudiante");
        System.out.println("9️⃣  Mostrar estadísticas");
        System.out.println("🔟 Ejecutar demostración completa");
        System.out.println("0️⃣  Salir del sistema");
        System.out.print("\n👉 Seleccione una opción: ");
    }

    private void mostrarDetallesEstudiante(EstudianteDTO estudiante) {
        System.out.println("   📋 ID: " + estudiante.getId());
        System.out.println("   👤 Nombre completo: " + estudiante.getNombreCompleto());
        System.out.println("   📧 Email: " + estudiante.getEmail());
        System.out.println("   🎓 Carrera: " + estudiante.getCarrera());
        System.out.println("   📅 Fecha ingreso: " + estudiante.getFechaIngreso());
        System.out.println("   ✅ Estado: " + (estudiante.isActivo() ? "Activo" : "Inactivo"));
    }

    private void mostrarResumenEstudiante(EstudianteDTO estudiante) {
        System.out.printf("   📋 ID: %-3d | 👤 %-25s | 🎓 %-20s | 📧 %s%n",
            estudiante.getId(),
            estudiante.getNombreCompleto(),
            estudiante.getCarrera(),
            estudiante.getEmail()
        );
    }

    private int leerOpcion() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Long leerLong() {
        try {
            String input = scanner.nextLine().trim();
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Debe ingresar un número válido.");
            return 0L;
        }
    }

    private void manejarErrorServicio(ServicioException e) {
        System.out.println("❌ Error en el servicio: " + e.getMessage());
        
        if (e.esErrorDeValidacion()) {
            System.out.println("   🔍 Tipo: Error de validación");
        } else if (e.esErrorDeNegocio()) {
            System.out.println("   🏢 Tipo: Error de reglas de negocio");
        } else if (e.esErrorDeDatos()) {
            System.out.println("   💾 Tipo: Error de acceso a datos");
        }
        
        System.out.println("   🔧 Código: " + e.getCodigoError());
    }

    private void manejarError(String mensaje, Exception e) {
        System.out.println("❌ " + mensaje);
        System.out.println("   🔧 Detalle: " + e.getMessage());
        
        // En un entorno de producción, aquí se loggearía el error completo
        if (System.getProperty("debug", "false").equals("true")) {
            e.printStackTrace();
        }
    }

    private void cerrarSistema() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("👋 ¡Gracias por usar el Sistema CRUD de Estudiantes!");
        System.out.println("🧪 TDD aplicado exitosamente");
        System.out.println("🏗️ Principios SOLID implementados");
        System.out.println("👨‍💻 Desarrollado por: Roberto Rivas López");
        System.out.println("=".repeat(50));
        
        scanner.close();
    }
}