package negocio;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

/**
 * Árbol general n-ario: Biblioteca -> Áreas -> Estantes.
 * Todas las operaciones son recursivas.
 */
public class ArbolBiblioteca {
    private final NodoUnidad raiz;

    public ArbolBiblioteca(UnidadBiblioteca unidadRaiz) {
        if (unidadRaiz == null || unidadRaiz.getTipo() != TipoUnidad.BIBLIOTECA) {
            throw new IllegalArgumentException("La raíz debe ser una unidad de tipo BIBLIOTECA.");
        }
        this.raiz = new NodoUnidad(unidadRaiz);
    }

    public NodoUnidad getRaiz() {
        return raiz;
    }

    /** Agrega una unidad respetando BIBLIOTECA -> AREA -> ESTANTE y sin códigos repetidos. */
    public boolean agregar(String codigoPadre, UnidadBiblioteca nueva) {
        if (codigoPadre == null || nueva == null) {
            return false;
        }
        if (buscar(nueva.getCodigo()) != null) {
            return false;
        }
        NodoUnidad padre = buscar(codigoPadre);
        if (padre == null) {
            return false;
        }
        if (!padre.getDato().getTipo().puedeTenerHijo(nueva.getTipo())) {
            return false;
        }
        padre.getHijos().add(new NodoUnidad(nueva));
        return true;
    }

    /** Busca una unidad sin distinguir mayúsculas. Devuelve null si no existe. */
    public NodoUnidad buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(raiz, codigo.trim());
    }

    private NodoUnidad buscar(NodoUnidad nodo, String codigo) {
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nodo;
        }
        return buscarEnHijos(nodo, 0, codigo);
    }

    private NodoUnidad buscarEnHijos(NodoUnidad nodo, int i, String codigo) {
        if (i >= nodo.getHijos().size()) {
            return null;
        }
        NodoUnidad encontrado = buscar(nodo.getHijos().get(i), codigo);
        if (encontrado != null) {
            return encontrado;
        }
        return buscarEnHijos(nodo, i + 1, codigo);
    }

    /** Altura del árbol en aristas (solo raíz = 0). */
    public int altura() {
        return altura(raiz);
    }

    private int altura(NodoUnidad nodo) {
        return nodo.esHoja() ? 0 : 1 + alturaMaxHijos(nodo, 0);
    }

    private int alturaMaxHijos(NodoUnidad nodo, int i) {
        if (i >= nodo.getHijos().size()) {
            return 0;
        }
        return Math.max(altura(nodo.getHijos().get(i)), alturaMaxHijos(nodo, i + 1));
    }

    /** Número de unidades sin hijos. */
    public int contarHojas() {
        return contarHojas(raiz);
    }

    private int contarHojas(NodoUnidad nodo) {
        return nodo.esHoja() ? 1 : contarHojasHijos(nodo, 0);
    }

    private int contarHojasHijos(NodoUnidad nodo, int i) {
        if (i >= nodo.getHijos().size()) {
            return 0;
        }
        return contarHojas(nodo.getHijos().get(i)) + contarHojasHijos(nodo, i + 1);
    }

    /** Nivel de la unidad (raíz = 0). -1 si no existe. */
    public int nivel(String codigo) {
        if (codigo == null) {
            return -1;
        }
        return nivel(raiz, codigo.trim(), 0);
    }

    private int nivel(NodoUnidad nodo, String codigo, int nivelActual) {
        if (nodo.getDato().getCodigo().equalsIgnoreCase(codigo)) {
            return nivelActual;
        }
        return nivelEnHijos(nodo, 0, codigo, nivelActual + 1);
    }

    private int nivelEnHijos(NodoUnidad nodo, int i, String codigo, int nivelHijo) {
        if (i >= nodo.getHijos().size()) {
            return -1;
        }
        int n = nivel(nodo.getHijos().get(i), codigo, nivelHijo);
        if (n != -1) {
            return n;
        }
        return nivelEnHijos(nodo, i + 1, codigo, nivelHijo);
    }

    /** Jerarquía en preorden, con 2 espacios por nivel. */
    public String mostrarJerarquia() {
        StringBuilder sb = new StringBuilder();
        mostrarJerarquia(raiz, 0, sb);
        return sb.toString();
    }

    private void mostrarJerarquia(NodoUnidad nodo, int nivel, StringBuilder sb) {
        sb.append(espacios(nivel))
          .append(nodo.getDato().getCodigo()).append(" ")
          .append(nodo.getDato().getNombre());
        if (nodo.getCatalogo() != null) {
            sb.append(" [").append(nodo.getCatalogo().contar()).append(" libros]");
        }
        sb.append("\n");
        mostrarJerarquiaHijos(nodo, 0, nivel + 1, sb);
    }

    private void mostrarJerarquiaHijos(NodoUnidad nodo, int i, int nivel, StringBuilder sb) {
        if (i >= nodo.getHijos().size()) {
            return;
        }
        mostrarJerarquia(nodo.getHijos().get(i), nivel, sb);
        mostrarJerarquiaHijos(nodo, i + 1, nivel, sb);
    }

    private String espacios(int nivel) {
        return nivel <= 0 ? "" : "  " + espacios(nivel - 1);
    }

    /** Total de libros de todos los estantes. */
    public int contarLibrosTotal() {
        return contarLibros(raiz);
    }

    private int contarLibros(NodoUnidad nodo) {
        int propios = (nodo.getCatalogo() != null) ? nodo.getCatalogo().contar() : 0;
        return propios + contarLibrosHijos(nodo, 0);
    }

    private int contarLibrosHijos(NodoUnidad nodo, int i) {
        if (i >= nodo.getHijos().size()) {
            return 0;
        }
        return contarLibros(nodo.getHijos().get(i)) + contarLibrosHijos(nodo, i + 1);
    }
}
