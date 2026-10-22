package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

import java.util.ArrayList;
import java.util.List;

/**
 * Nodo del árbol general n-ario. Si la unidad es un ESTANTE, guarda su CatalogoABB.
 */
public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
        this.catalogo = (dato.getTipo() == TipoUnidad.ESTANTE) ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() {
        return dato;
    }

    public List<NodoUnidad> getHijos() {
        return hijos;
    }

    /** Catálogo de libros; null si la unidad no es un ESTANTE. */
    public CatalogoABB getCatalogo() {
        return catalogo;
    }

    public boolean esHoja() {
        return hijos.isEmpty();
    }
}
