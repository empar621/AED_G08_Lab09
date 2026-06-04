package btree;

public class Libro implements Comparable<Libro> {
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(String isbn, String titulo, String autor, int anio) {
        this.isbn = isbn.trim();
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anio = anio;
    }

    public String getIsbn() { return isbn; }

    @Override
    public int compareTo(Libro o) {
        return this.isbn.compareTo(o.isbn);
    }

    @Override
    public String toString() {
        return String.format("ISBN: %-14s | Título: %-20s | Autor: %-15s | Año: %d", 
                isbn, titulo, autor, anio);
    }
}