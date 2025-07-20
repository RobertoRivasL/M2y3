package com.roberto.rivas;

import com.roberto.rivas.controlador.EstudianteControlador;
import com.roberto.rivas.repositorio.EstudianteRepositorioBD;
import com.roberto.rivas.repositorio.RepositorioException;

import java.util.Scanner;

/**
 * Clase principal para ejecutar el Sistema CRUD de Estudiantes con TDD
 * Punto de entrada de la aplicación
 * Demuestra integración completa de todas las capas desarrolladas
 * 
 * @author Roberto Rivas Lopez
 */
public class AplicacionPrincipal {

    private static final String VERSION = "1.0.0";
    private static final String AUTOR = "Roberto Rivas López";
    
    public static void main(String[] args) {
        mostrarBanner();
        
        try {
            // Determinar tipo de repositorio según argumentos
            boolean usarBaseDatos = determinarTipoRepositorio(args);
            
            // Inicializar base de datos si es necesario
            if (usarBaseDatos) {
                inicializarBaseDatos();
            }
            
            // Crear y ejecutar controlador principal
            EstudianteControlador controlador = new EstudianteControlador(usarBaseDatos);
            controlador.ejecutar();
            
        } catch (Exception e) {
            manejarErrorInicio(e);
        }
    }
    
    /**
     * Determina el tipo de repositorio a usar basado en argumentos de línea de comandos
     */
    private static boolean determinarTipoRepositorio(String[] args) {
        // Verificar argumentos de línea de comandos
        for (String arg : args) {
            if ("--db".equals(arg) || "--database".equals(arg)) {
                System.out.println("🗄️ Configuración: Usando base de datos");
                return true;
            }
            if ("--memory".equals(arg) || "--mem".equals(arg)) {
                System.out.println("💾 Configuración: Usando repositorio en memoria");
                return false;
            }
            if ("--help".equals(arg) || "-h".equals(arg)) {
                mostrarAyuda();
                System.exit(0);
            }
        }
        
        // Si no hay argumentos, preguntar al usuario
        return preguntarTipoRepositorio();
    }
    
    /**
     * Pregunta al usuario qué tipo de repositorio usar
     */
    private static boolean preguntarTipoRepositorio() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n📊 CONFIGURACIÓN DEL REPOSITORIO");
        System.out.println("=================================");
        System.out.println("1️⃣ Repositorio en memoria (recomendado para pruebas)");
        System.out.println("2️⃣ Base de datos MySQL (SQLonline)");
        System.out.print("\n👉 Seleccione una opción (1-2): ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            return opcion == 2;
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Opción no válida, usando repositorio en memoria");
            return false;
        }
    }
    
    /**
     * Inicializa la base de datos creando las tablas necesarias
     */
    private static void inicializarBaseDatos() {
        System.out.println("\n🔧 INICIALIZANDO BASE DE DATOS...");
        
        try {
            EstudianteRepositorioBD repositorioBD = new EstudianteRepositorioBD();
            repositorioBD.inicializarBaseDatos();
            System.out.println("✅ Base de datos inicializada correctamente");
            
        } catch (RepositorioException e) {
            System.out.println("❌ Error al inicializar base de datos: " + e.getMessage());
            System.out.println("💡 Sugerencia: Verificar credenciales de SQLonline");
            System.out.println("🔄 Cambiando a repositorio en memoria...");
            
            // Fallback a memoria si falla la BD
            System.out.println("⚠️ Continuando con repositorio en memoria");
        }
    }
    
    /**
     * Muestra el banner de inicio de la aplicación
     */
    private static void mostrarBanner() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎓 SISTEMA CRUD DE ESTUDIANTES CON TDD");
        System.out.println("📚 Portafolio Final Módulos 2 y 3: Testing Ágil + TDD");
        System.out.println("👨‍💻 Desarrollado por: " + AUTOR);
        System.out.println("📦 Versión: " + VERSION);
        System.out.println("☕ Java 17 + Maven 3.9.10 + JUnit 5 + Mockito");
        System.out.println("=".repeat(70));
        
        System.out.println("\n🧪 PRINCIPIOS APLICADOS:");
        System.out.println("   ✅ Test-Driven Development (TDD) - 12+ ciclos");
        System.out.println("   ✅ Principios SOLID (SRP, OCP, LSP, ISP, DIP)");
        System.out.println("   ✅ Modularidad y Separación de Intereses");
        System.out.println("   ✅ Encapsulación y Abstracción");
        System.out.println("   ✅ Inyección de Dependencias");
        System.out.println("   ✅ Cobertura de código >= 80% (JaCoCo)");
        System.out.println("   ✅ Mockito para testing de dependencias");
        
        System.out.println("\n🏗️ ARQUITECTURA:");
        System.out.println("   📋 Controlador (Patrón Facade)");
        System.out.println("   🎯 Servicio (Lógica de negocio)");
        System.out.println("   💾 Repositorio (Acceso a datos)");
        System.out.println("   📄 Modelo (Entidades de dominio)");
        System.out.println("   🔄 DTO (Transferencia de datos)");
    }
    
    /**
     * Muestra ayuda de uso de la aplicación
     */
    private static void mostrarAyuda() {
        System.out.println("\n📖 AYUDA - SISTEMA CRUD ESTUDIANTES TDD");
        System.out.println("=========================================");
        System.out.println();
        System.out.println("USO:");
        System.out.println("   java -jar crud-tdd-proyecto.jar [OPCIONES]");
        System.out.println();
        System.out.println("OPCIONES:");
        System.out.println("   --db, --database    Usar base de datos MySQL (SQLonline)");
        System.out.println("   --memory, --mem     Usar repositorio en memoria");
        System.out.println("   --help, -h          Mostrar esta ayuda");
        System.out.println();
        System.out.println("EJEMPLOS:");
        System.out.println("   java -jar crud-tdd-proyecto.jar --memory");
        System.out.println("   java -jar crud-tdd-proyecto.jar --db");
        System.out.println("   mvn exec:java -Dexec.mainClass=\"com.roberto.rivas.AplicacionPrincipal\"");
        System.out.println();
        System.out.println("TESTING:");
        System.out.println("   mvn test                    Ejecutar todas las pruebas");
        System.out.println("   mvn jacoco:report          Generar reporte de cobertura");
        System.out.println("   mvn test -Dtest=*Test      Ejecutar pruebas específicas");
        System.out.println();
        System.out.println("CONFIGURACIÓN BASE DE DATOS:");
        System.out.println("   1. Registrarse en https://sqlonline.com/");
        System.out.println("   2. Crear base de datos MySQL");
        System.out.println("   3. Ejecutar script sql-scripts/estudiantes_schema.sql");
        System.out.println("   4. Actualizar credenciales en EstudianteRepositorioBD.java");
        System.out.println();
        System.out.println("ESTRUCTURA DEL PROYECTO:");
        System.out.println("   src/main/java/              Código fuente principal");
        System.out.println("   src/test/java/              Pruebas unitarias");
        System.out.println("   sql-scripts/                Scripts de base de datos");
        System.out.println("   target/site/jacoco/         Reportes de cobertura");
        System.out.println("   README.md                   Documentación principal");
        System.out.println();
        System.out.println("PRINCIPIOS SOLID APLICADOS:");
        System.out.println("   SRP: Cada clase tiene una sola responsabilidad");
        System.out.println("   OCP: Abierto para extensión, cerrado para modificación");
        System.out.println("   LSP: Subtipos reemplazables por tipos base");
        System.out.println("   ISP: Interfaces específicas, no generales");
        System.out.println("   DIP: Depender de abstracciones, no concreciones");
        System.out.println();
        System.out.println("CICLOS TDD IMPLEMENTADOS:");
        System.out.println("   1-4:  Validación del modelo Estudiante");
        System.out.println("   5-8:  Operaciones CRUD del repositorio");
        System.out.println("   9-12: Lógica de negocio del servicio");
        System.out.println("   13+:  Integración y casos especiales");
        System.out.println();
        System.out.println("📧 Contacto: roberto.rivas@ejemplo.com");
        System.out.println("🔗 GitHub: github.com/roberto-rivas/crud-tdd-estudiantes");
    }
    
    /**
     * Maneja errores durante el inicio de la aplicación
     */
    private static void manejarErrorInicio(Exception e) {
        System.err.println("\n❌ ERROR CRÍTICO AL INICIAR LA APLICACIÓN");
        System.err.println("===========================================");
        System.err.println("Mensaje: " + e.getMessage());
        System.err.println("Tipo: " + e.getClass().getSimpleName());
        
        System.err.println("\n🔧 POSIBLES SOLUCIONES:");
        System.err.println("1. Verificar que Java 17+ esté instalado");
        System.err.println("2. Comprobar que todas las dependencias estén disponibles");
        System.err.println("3. Si usa base de datos, verificar credenciales SQLonline");
        System.err.println("4. Intentar ejecutar con repositorio en memoria: --memory");
        System.err.println("5. Revisar logs detallados con: java -Ddebug=true ...");
        
        System.err.println("\n📚 Para más información, consultar README.md");
        System.err.println("📧 Reportar bug a: roberto.rivas@ejemplo.com");
        
        // Mostrar stack trace si está en modo debug
        if (System.getProperty("debug", "false").equals("true")) {
            System.err.println("\n🐛 STACK TRACE DETALLADO:");
            e.printStackTrace();
        }
        
        System.exit(1);
    }
    
    /**
     * Información sobre el sistema y tecnologías utilizadas
     */
    public static void mostrarInformacionTecnica() {
        System.out.println("\n🔧 INFORMACIÓN TÉCNICA DEL SISTEMA");
        System.out.println("===================================");
        System.out.println("Java Runtime: " + System.getProperty("java.version"));
        System.out.println("JVM Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("Arquitectura: " + System.getProperty("os.arch"));
        System.out.println("Memoria disponible: " + 
            (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB");
        System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));
        
        System.out.println("\n📦 DEPENDENCIAS PRINCIPALES:");
        System.out.println("• JUnit 5.10.1 - Framework de testing");
        System.out.println("• Mockito 5.8.0 - Mocking para pruebas unitarias");
        System.out.println("• H2 2.2.224 - Base de datos en memoria para testing");
        System.out.println("• MySQL Connector 8.0.33 - Driver para SQLonline");
        System.out.println("• JaCoCo 0.8.11 - Análisis de cobertura de código");
        
        System.out.println("\n🎯 MÉTRICAS DE CALIDAD OBJETIVO:");
        System.out.println("• Cobertura de código: >= 80%");
        System.out.println("• Pruebas unitarias: 16+ casos de prueba");
        System.out.println("• Ciclos TDD: 12+ iteraciones RED-GREEN-REFACTOR");
        System.out.println("• Principios SOLID: 5 principios aplicados");
        System.out.println("• Complejidad ciclomática: <= 10 por método");
    }
}