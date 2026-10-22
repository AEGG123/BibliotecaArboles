package negocio;

import modelo.Libro;

/** Nodo del Árbol Binario de Búsqueda de libros. */
public class NodoLibro {
    private Libro dato;
    private NodoLibro izquierdo;
    private NodoLibro derecho;

    public NodoLibro(Libro dato) {
        this.dato = dato;
    }

    public Libro getDato() {
        return dato;
    }

    public void setDato(Libro dato) {
        this.dato = dato;
    }

    public NodoLibro getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoLibro izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoLibro getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoLibro derecho) {
        this.derecho = derecho;
    }
}
