package modelo;

/**
 * Libro del catálogo. El código es inmutable (no tiene setter).
 */
public class Libro {
    public static final int ANIO_MIN = 1900;
    public static final int ANIO_MAX = 2026;

    private final int codigo;
    private String titulo;
    private String autor;
    private int anio;

    public Libro(int codigo, String titulo, String autor, int anio) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El código debe ser un entero mayor que 0.");
        }
        this.codigo = codigo;
        setTitulo(titulo);
        setAutor(autor);
        setAnio(anio);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        this.autor = autor.trim();
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        if (anio < ANIO_MIN || anio > ANIO_MAX) {
            throw new IllegalArgumentException(
                    "El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX + ".");
        }
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + titulo + " - " + autor + " (" + anio + ")";
    }
}
