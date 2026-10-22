package negocio;

import enums.TipoRecorrido;
import modelo.Libro;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol Binario de Búsqueda de libros ordenado por código.
 * Todas las operaciones son recursivas y no se usa ninguna estructura ordenada de la API.
 */
public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoLibro raiz) {
        this.raiz = raiz;
    }

    // ---------------------------------------------------------------- insertar
    /** Inserta por código. Devuelve false si el código ya existe (o el libro es nulo). */
    public boolean insertar(Libro libro) {
        if (libro == null || buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ------------------------------------------------------------------ buscar
    /** Busca un libro por código. null si no existe. */
    public Libro buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Libro buscar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        if (codigo == nodo.getDato().getCodigo()) {
            return nodo.getDato();
        }
        return codigo < nodo.getDato().getCodigo()
                ? buscar(nodo.getIzquierdo(), codigo)
                : buscar(nodo.getDerecho(), codigo);
    }

    // ------------------------------------------------------------------ contar
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        return nodo == null ? 0 : 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    // --------------------------------------------------------------- recorrer
    public List<Libro> recorrer(TipoRecorrido tipo) {
        List<Libro> resultado = new ArrayList<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> out) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            out.add(nodo.getDato());
        }
        recorrer(nodo.getIzquierdo(), tipo, out);
        if (tipo == TipoRecorrido.INORDEN) {
            out.add(nodo.getDato());
        }
        recorrer(nodo.getDerecho(), tipo, out);
        if (tipo == TipoRecorrido.POSORDEN) {
            out.add(nodo.getDato());
        }
    }

    /**
     * Traza del recorrido: "entra X" al entrar, "VISITA X" al procesar y
     * "null -> regresa" en un hijo vacío; 2 espacios por nivel; última línea "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        trazar(raiz, tipo, 0, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(":").append(secuencia).append("\n");
        return traza.toString();
    }

    private void trazar(NodoLibro nodo, TipoRecorrido tipo, int nivel,
                        StringBuilder traza, StringBuilder secuencia) {
        String sangria = espacios(nivel);
        if (nodo == null) {
            traza.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        traza.append(sangria).append("entra ").append(codigo).append("\n");
        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        trazar(nodo.getIzquierdo(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
        trazar(nodo.getDerecho(), tipo, nivel + 1, traza, secuencia);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, traza, secuencia);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder traza, StringBuilder secuencia) {
        traza.append(sangria).append("VISITA ").append(codigo).append("\n");
        secuencia.append(" ").append(codigo);
    }

    private String espacios(int nivel) {
        return nivel <= 0 ? "" : "  " + espacios(nivel - 1);
    }

    // --------------------------------------------------------------- modificar
    /** Modifica título, autor y año (nunca el código). false si no existe o los datos son inválidos. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) {
            return false;
        }
        new Libro(codigo, titulo, autor, anio); // valida los datos; lanza IllegalArgumentException
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnio(anio);
        return true;
    }

    // ---------------------------------------------------------------- eliminar
    /** Elimina hoja, nodo con un hijo o nodo con dos hijos (reemplazo por el sucesor inorden). */
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();      // hoja o solo hijo derecho
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();    // solo hijo izquierdo
            }
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        return nodo.getIzquierdo() == null ? nodo : minimo(nodo.getIzquierdo());
    }

    // -------------------------------------------------------------------- retos
    /** Cuenta los códigos en [a, b] podando las ramas que no pueden contener valores del rango. */
    public int contarEnRango(int a, int b) {
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int c = nodo.getDato().getCodigo();
        if (c < a) {
            return contarEnRango(nodo.getDerecho(), a, b);
        }
        if (c > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b);
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

    /** Códigos visitados desde la raíz hasta el libro (o hasta donde la búsqueda termina). */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private void caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int c = nodo.getDato().getCodigo();
        camino.add(c);
        if (codigo < c) {
            caminoBusqueda(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > c) {
            caminoBusqueda(nodo.getDerecho(), codigo, camino);
        }
    }

    /** Verifica que TODO el árbol cumpla la propiedad ABB usando límites mínimo y máximo. */
    public boolean esABBValido() {
        return esValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esValido(NodoLibro nodo, long min, long max) {
        if (nodo == null) {
            return true;
        }
        long c = nodo.getDato().getCodigo();
        if (c <= min || c >= max) {
            return false;
        }
        return esValido(nodo.getIzquierdo(), min, c) && esValido(nodo.getDerecho(), c, max);
    }

    /**
     * Extra: reconstruye el ABB desde su preorden en O(n), sin insertar uno por uno.
     * Los libros se crean con datos genéricos (solo se conoce el código).
     */
    public static CatalogoABB desdePreorden(int[] preorden) {
        if (preorden == null) {
            throw new IllegalArgumentException("El preorden no puede ser nulo.");
        }
        CatalogoABB catalogo = new CatalogoABB();
        int[] indice = {0};
        catalogo.raiz = construir(preorden, indice, Long.MIN_VALUE, Long.MAX_VALUE);
        if (indice[0] != preorden.length) {
            throw new IllegalArgumentException("El arreglo no es un preorden válido de un ABB.");
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] pre, int[] indice, long min, long max) {
        if (indice[0] >= pre.length) {
            return null;
        }
        int valor = pre[indice[0]];
        if (valor <= min || valor >= max) {
            return null;
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(valor, "Libro " + valor, "Autor desconocido", 2000));
        nodo.setIzquierdo(construir(pre, indice, min, valor));
        nodo.setDerecho(construir(pre, indice, valor, max));
        return nodo;
    }
}
