package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

import java.util.Arrays;
import java.util.List;

/** Casos de prueba obligatorios del deber. Muestra "X/N pruebas OK". */
public class PruebasBiblioteca {
    private static int total = 0;
    private static int ok = 0;

    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            ok++;
        }
        System.out.println((condicion ? "[OK]    " : "[FALLA] ") + nombre);
    }

    private static String codigos(List<Libro> libros) {
        StringBuilder sb = new StringBuilder();
        for (Libro l : libros) {
            sb.append(sb.length() == 0 ? "" : " ").append(l.getCodigo());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== PruebasBiblioteca ===");

        // 1. Solo la raíz
        ArbolBiblioteca soloRaiz = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        verificar("Solo raíz: altura 0", soloRaiz.altura() == 0);
        verificar("Solo raíz: 1 hoja", soloRaiz.contarHojas() == 1);

        // 2. Biblioteca inicial
        ArbolBiblioteca bib = DatosPrueba.crearBibliotecaInicial();
        verificar("Inicial: altura 2", bib.altura() == 2);
        verificar("Inicial: 4 hojas", bib.contarHojas() == 4);
        verificar("Inicial: 13 libros", bib.contarLibrosTotal() == 13);

        // 3. Niveles
        verificar("Nivel BIB = 0", bib.nivel("BIB") == 0);
        verificar("Nivel sw = 1 (sin distinguir mayúsculas)", bib.nivel("sw") == 1);
        verificar("Nivel SW-E2 = 2", bib.nivel("SW-E2") == 2);
        verificar("Nivel XYZ = -1", bib.nivel("XYZ") == -1);

        // 4. Jerarquía inválida
        verificar("Agregar ESTANTE bajo BIBLIOTECA = false",
                !bib.agregar("BIB", new UnidadBiblioteca("X-E1", "Estante X", TipoUnidad.ESTANTE)));
        verificar("Código de unidad repetido = false",
                !bib.agregar("BIB", new UnidadBiblioteca("sw", "Otra", TipoUnidad.AREA)));

        // 5. Recorridos de SW-E1
        CatalogoABB e1 = bib.buscar("SW-E1").getCatalogo();
        verificar("Preorden SW-E1",
                codigos(e1.recorrer(TipoRecorrido.PREORDEN)).equals("60 35 20 45 40 80 70 75 90"));
        verificar("Inorden SW-E1",
                codigos(e1.recorrer(TipoRecorrido.INORDEN)).equals("20 35 40 45 60 70 75 80 90"));
        verificar("Posorden SW-E1",
                codigos(e1.recorrer(TipoRecorrido.POSORDEN)).equals("20 40 45 35 75 70 90 80 60"));

        // 6. Código repetido
        verificar("Insertar código repetido = false", !e1.insertar(DatosPrueba.crearLibro(60)));

        // 7. Retos
        verificar("caminoBusqueda(42) = [60, 35, 45, 40]",
                e1.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        verificar("contarEnRango(40, 75) = 5", e1.contarEnRango(40, 75) == 5);

        // 8. Insertar 42 y eliminar 20, 70 y 60
        verificar("Insertar 42 = true", e1.insertar(DatosPrueba.crearLibro(42)));
        verificar("Eliminar hoja 20", e1.eliminar(20));
        verificar("Eliminar nodo con un hijo 70", e1.eliminar(70));
        verificar("Eliminar nodo con dos hijos 60 (raíz)", e1.eliminar(60));
        verificar("Preorden final = 75 35 45 40 42 80 90",
                codigos(e1.recorrer(TipoRecorrido.PREORDEN)).equals("75 35 45 40 42 80 90"));
        verificar("ABB válido tras eliminaciones", e1.esABBValido());

        // 9. Códigos inexistentes
        verificar("Buscar inexistente = null", e1.buscar(999) == null);
        verificar("Modificar inexistente = false", !e1.modificar(999, "T", "A", 2000));
        verificar("Eliminar inexistente = false", !e1.eliminar(999));
        verificar("Modificar existente = true y no cambia el código",
                e1.modificar(75, "Nuevo título", "Nuevo autor", 2010)
                        && e1.buscar(75).getTitulo().equals("Nuevo título"));

        // 10. Datos inválidos
        verificar("Libro con código 0 -> IllegalArgumentException", lanzaIAE(0, "T", "A", 2000));
        verificar("Libro con año 1899 -> IllegalArgumentException", lanzaIAE(5, "T", "A", 1899));
        verificar("Libro con año 2027 -> IllegalArgumentException", lanzaIAE(5, "T", "A", 2027));
        verificar("Libro con título vacío -> IllegalArgumentException", lanzaIAE(5, " ", "A", 2000));

        // 11. ABB inválido (nieto mal ubicado)
        CatalogoABB malo = new CatalogoABB();
        NodoLibro r = new NodoLibro(DatosPrueba.crearLibro(50));
        NodoLibro izq = new NodoLibro(DatosPrueba.crearLibro(30));
        izq.setDerecho(new NodoLibro(DatosPrueba.crearLibro(60))); // 60 > 50 pero está a la izquierda
        r.setIzquierdo(izq);
        malo.setRaiz(r);
        verificar("esABBValido() con nieto mal ubicado = false", !malo.esABBValido());

        // 12. Extra: desdePreorden
        CatalogoABB rec = CatalogoABB.desdePreorden(new int[]{75, 35, 45, 40, 42, 80, 90});
        verificar("desdePreorden reconstruye el mismo preorden",
                codigos(rec.recorrer(TipoRecorrido.PREORDEN)).equals("75 35 45 40 42 80 90")
                        && rec.esABBValido());

        System.out.println();
        System.out.println(ok + "/" + total + " pruebas OK");
    }

    private static boolean lanzaIAE(int codigo, String titulo, String autor, int anio) {
        try {
            new Libro(codigo, titulo, autor, anio);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
