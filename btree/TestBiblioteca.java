package btree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestBiblioteca {
    public static void main(String[] args) {
        String archivoTxt = "biblioteca.txt";

        crearArchivoSimulado(archivoTxt);

        Biblioteca biblioteca = new Biblioteca();

        System.out.println("=== 1. CARGA AUTOMÁTICA DESDE ARCHIVO ===");

        biblioteca.cargarDesdeArchivo(archivoTxt);

        System.out.println("\n=== 2. MOSTRAR INVENTARIO INICIAL (ORDENADO POR ISBN) ===");
        biblioteca.mostrarTodosLosLibros();
        biblioteca.mostrarMetricas();

        System.out.println("\n=== 3. INTENTO DE INSERCIÓN DE UN ISBN DUPLICADO ===");

        Libro libroDuplicado = new Libro("9780132350884", "Clean Code (Copia)", "Robert Martin", 2008);
        biblioteca.agregarLibro(libroDuplicado);

        System.out.println("\n=== 4. INSERCIÓN MANUAL DE UN NUEVO LIBRO ===");
        Libro nuevoLibro = new Libro("9780131103627", "The C Programming Language", "Brian Kernighan", 1988);
        biblioteca.agregarLibro(nuevoLibro);

        System.out.println("\n=== 5. PRUEBAS DE BÚSQUEDA DETALLADA CON CAMINO ===");

        biblioteca.buscarLibroPorIsbn("9780201633610"); 

        biblioteca.buscarLibroPorIsbn("9999999999999");

        System.out.println("\n=== 6. ELIMINACIÓN DE UN ELEMENTO ===");
        System.out.println("Eliminando 'Clean Architecture' (ISBN: 9780134494166)...");
        biblioteca.eliminarLibro("9780134494166");

        System.out.println("\n=== 7. MOSTRAR INVENTARIO FINAL Y MÉTRICAS ACTUALIZADAS ===");
        biblioteca.mostrarTodosLosLibros();
        biblioteca.mostrarMetricas();
    }

    // metodo de soporte para generar el archivo biblioteca.txt de manera autónonoma
    private static void crearArchivoSimulado(String rutaArchivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            bw.write("4\n"); 
            bw.write("9780132350884, Clean Code, Robert Martin, 2008\n");
            bw.write("9780134494166, Clean Architecture, Robert Martin, 2017\n");
            bw.write("9780201633610, Design Patterns, GoF, 1994\n");
            bw.write("9780596009205, Head First Java, Kathy Sierra, 2005\n");
            System.out.println("-> Archivo '" + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al generar el archivo de prueba: " + e.getMessage());
        }
    }
}