package btree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Biblioteca {
    private BTree<Libro> arbolLibros;

    public Biblioteca() {
        this.arbolLibros = null; // Se inicia dinámicamente al leer la cabecera del txt
    }

    // Construye el árbol a partir del archivo indicado
    public void cargarDesdeArchivo(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea = br.readLine();
            if (linea == null) {
                System.out.println("Error: El archivo de inicialización se encuentra vacío.");
                return;
            }
            
            // primera línea define el orden estructural
            int orden = Integer.parseInt(linea.trim());
            this.arbolLibros = new BTree<>(orden);
            System.out.println("-> Árbol B de la Biblioteca creado con Orden: " + orden);

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(",");
                if (datos.length == 4) {
                    String isbn = datos[0].trim();
                    String titulo = datos[1].trim();
                    String autor = datos[2].trim();
                    int anio = Integer.parseInt(datos[3].trim());

                    Libro nuevoLibro = new Libro(isbn, titulo, autor, anio);
                    try {
                        arbolLibros.insert(nuevoLibro);
                    } catch (ItemDuplicated e) {
                        System.out.println("[Alerta de Duplicado] " + e.getMessage());
                    }
                }
            }
            System.out.println("-> Proceso de carga masiva finalizado exitosamente.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error crítico al procesar el archivo: " + e.getMessage());
        }
    }

    public void agregarLibro(Libro libro) {
        if (arbolLibros == null) {
            System.out.println("Biblioteca no inicializada.");
            return;
        }
        try {
            arbolLibros.insert(libro);
            System.out.println("Libro insertado correctamente.");
        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }

    public void buscarLibroPorIsbn(String isbn) {
        if (arbolLibros == null || arbolLibros.isEmpty()) {
            System.out.println("No hay registros que buscar.");
            return;
        }
        System.out.println("\nIniciando búsqueda para ISBN: " + isbn);
        // objeto pivote para mapear la coincidencia por ISBN
        Libro dummy = new Libro(isbn, "", "", 0);
        Libro resultado = arbolLibros.searchWithPath(dummy);
        
        if (resultado != null) {
            System.out.println("Resultado de coincidencia:\n   " + resultado);
        } else {
            System.out.println("El libro solicitado no está registrado.");
        }
    }

    public void eliminarLibro(String isbn) {
        if (arbolLibros == null || arbolLibros.isEmpty()) {
            System.out.println("Nada que eliminar.");
            return;
        }
        Libro dummy = new Libro(isbn, "", "", 0);
        arbolLibros.remove(dummy);
    }

    public void mostrarTodosLosLibros() {
        if (arbolLibros == null || arbolLibros.isEmpty()) {
            System.out.println("La biblioteca está vacía.");
            return;
        }
        arbolLibros.printInOrder();
    }

    public void mostrarMetricas() {
        if (arbolLibros == null) return;
        System.out.println("\n=== MÉTRICAS DE LA ESTRUCTURA ===");
        System.out.println("Altura del Árbol B: " + arbolLibros.getHeight());
        System.out.println("Cantidad total de libros almacenados: " + arbolLibros.size());
    }
}