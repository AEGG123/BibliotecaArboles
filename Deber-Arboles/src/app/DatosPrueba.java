package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

/** Construye la estructura inicial de la Biblioteca FISEI. */
public class DatosPrueba {
    public static final int[] LIBROS_SW_E1 = {60, 35, 80, 20, 45, 70, 90, 40, 75};
    public static final int[] LIBROS_SW_E2 = {15, 10, 25};
    public static final int[] LIBROS_TI_E1 = {50};

    private DatosPrueba() {
    }

    /** Libro de demostración con datos válidos derivados del código. */
    public static Libro crearLibro(int codigo) {
        return new Libro(codigo, "Libro " + codigo, "Autor " + codigo, 1990 + (codigo % 35));
    }

    public static ArbolBiblioteca crearBibliotecaInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));

        cargarLibros(arbol.buscar("SW-E1").getCatalogo(), LIBROS_SW_E1);
        cargarLibros(arbol.buscar("SW-E2").getCatalogo(), LIBROS_SW_E2);
        cargarLibros(arbol.buscar("TI-E1").getCatalogo(), LIBROS_TI_E1);
        return arbol;
    }

    /** Inserta los códigos en el orden indicado (recursivo, sin ciclos). */
    public static void cargarLibros(CatalogoABB catalogo, int[] codigos) {
        cargarLibros(catalogo, codigos, 0);
    }

    private static void cargarLibros(CatalogoABB catalogo, int[] codigos, int i) {
        if (i >= codigos.length) {
            return;
        }
        catalogo.insertar(crearLibro(codigos[i]));
        cargarLibros(catalogo, codigos, i + 1);
    }
}
