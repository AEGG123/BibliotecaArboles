package app;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

import java.util.List;
import java.util.Scanner;

/** Menú interactivo del catálogo de la Biblioteca FISEI. */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static ArbolBiblioteca biblioteca = DatosPrueba.crearBibliotecaInicial();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Hasta pronto.");
    }

    private static void mostrarMenu() {
        System.out.println("\n===== CATÁLOGO BIBLIOTECA FISEI =====");
        System.out.println("1.  Cargar estructura inicial");
        System.out.println("2.  Mostrar jerarquía");
        System.out.println("3.  Agregar unidad");
        System.out.println("4.  Buscar unidad");
        System.out.println("5.  Consultar altura, hojas, nivel y total de libros");
        System.out.println("6.  Insertar libro en un estante");
        System.out.println("7.  Buscar libro en un estante");
        System.out.println("8.  Modificar libro de un estante");
        System.out.println("9.  Eliminar libro de un estante");
        System.out.println("10. Mostrar recorridos con su traza");
        System.out.println("11. Ejecutar retos");
        System.out.println("0.  Salir");
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1:
                biblioteca = DatosPrueba.crearBibliotecaInicial();
                System.out.println("Estructura inicial cargada.");
                break;
            case 2:
                System.out.print(biblioteca.mostrarJerarquia());
                break;
            case 3:
                agregarUnidad();
                break;
            case 4:
                buscarUnidad();
                break;
            case 5:
                consultas();
                break;
            case 6:
                insertarLibro();
                break;
            case 7:
                buscarLibro();
                break;
            case 8:
                modificarLibro();
                break;
            case 9:
                eliminarLibro();
                break;
            case 10:
                recorridos();
                break;
            case 11:
                retos();
                break;
            case 0:
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // ---------------------------------------------------------- unidades
    private static void agregarUnidad() {
        String padre = leerTexto("Código de la unidad padre: ");
        String codigo = leerTexto("Código de la nueva unidad: ");
        String nombre = leerTexto("Nombre: ");
        TipoUnidad tipo = leerTipoUnidad();
        if (tipo == null) {
            System.out.println("Tipo no válido.");
            return;
        }
        boolean agregada = biblioteca.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(agregada ? "Unidad agregada."
                : "No se pudo agregar (padre inexistente, código repetido o jerarquía inválida).");
    }

    private static TipoUnidad leerTipoUnidad() {
        int t = leerEntero("Tipo (1 = AREA, 2 = ESTANTE, 3 = BIBLIOTECA): ");
        switch (t) {
            case 1:
                return TipoUnidad.AREA;
            case 2:
                return TipoUnidad.ESTANTE;
            case 3:
                return TipoUnidad.BIBLIOTECA;
            default:
                return null;
        }
    }

    private static void buscarUnidad() {
        NodoUnidad n = biblioteca.buscar(leerTexto("Código de la unidad: "));
        System.out.println(n == null ? "No existe esa unidad." : "Encontrada: " + n.getDato());
    }

    private static void consultas() {
        System.out.println("Altura (aristas): " + biblioteca.altura());
        System.out.println("Hojas: " + biblioteca.contarHojas());
        System.out.println("Total de libros: " + biblioteca.contarLibrosTotal());
        String codigo = leerTexto("Código para consultar su nivel (Enter para omitir): ");
        if (!codigo.isEmpty()) {
            System.out.println("Nivel de " + codigo + ": " + biblioteca.nivel(codigo));
        }
    }

    // ------------------------------------------------------------ libros
    /** Pide el código de un estante y devuelve su catálogo, o null si no es válido. */
    private static CatalogoABB pedirEstante() {
        String codigo = leerTexto("Código del estante (ej. SW-E1): ");
        NodoUnidad n = biblioteca.buscar(codigo);
        if (n == null || n.getDato().getTipo() != TipoUnidad.ESTANTE) {
            System.out.println("No existe un ESTANTE con ese código.");
            return null;
        }
        return n.getCatalogo();
    }

    private static void insertarLibro() {
        CatalogoABB cat = pedirEstante();
        if (cat == null) {
            return;
        }
        int codigo = leerEntero("Código del libro: ");
        String titulo = leerTexto("Título: ");
        String autor = leerTexto("Autor: ");
        int anio = leerEntero("Año (1900-2026): ");
        boolean ok = cat.insertar(new Libro(codigo, titulo, autor, anio));
        System.out.println(ok ? "Libro insertado." : "El código ya existe en el estante.");
    }

    private static void buscarLibro() {
        CatalogoABB cat = pedirEstante();
        if (cat == null) {
            return;
        }
        Libro l = cat.buscar(leerEntero("Código del libro: "));
        System.out.println(l == null ? "No existe ese libro." : "Encontrado: " + l);
    }

    private static void modificarLibro() {
        CatalogoABB cat = pedirEstante();
        if (cat == null) {
            return;
        }
        int codigo = leerEntero("Código del libro a modificar: ");
        String titulo = leerTexto("Nuevo título: ");
        String autor = leerTexto("Nuevo autor: ");
        int anio = leerEntero("Nuevo año (1900-2026): ");
        System.out.println(cat.modificar(codigo, titulo, autor, anio)
                ? "Libro modificado." : "No existe ese libro.");
    }

    private static void eliminarLibro() {
        CatalogoABB cat = pedirEstante();
        if (cat == null) {
            return;
        }
        System.out.println(cat.eliminar(leerEntero("Código del libro a eliminar: "))
                ? "Libro eliminado." : "No existe ese libro.");
    }

    private static void recorridos() {
        CatalogoABB cat = pedirEstante();
        if (cat == null) {
            return;
        }
        mostrarRecorrido(cat, TipoRecorrido.PREORDEN);
        mostrarRecorrido(cat, TipoRecorrido.INORDEN);
        mostrarRecorrido(cat, TipoRecorrido.POSORDEN);
    }

    private static void mostrarRecorrido(CatalogoABB cat, TipoRecorrido tipo) {
        System.out.println("\n--- " + tipo + " ---");
        System.out.print(cat.trazaRecorrido(tipo));
        List<Libro> libros = cat.recorrer(tipo);
        System.out.println("Libros: " + libros.size());
    }

    private static void retos() {
        System.out.println("a) contarEnRango  b) caminoBusqueda  c) esABBValido  d) desdePreorden");
        String op = leerTexto("Reto: ").toLowerCase();
        if (op.equals("d")) {
            retoDesdePreorden();
            return;
        }
        CatalogoABB cat = pedirEstante();
        if (cat == null) {
            return;
        }
        switch (op) {
            case "a":
                int a = leerEntero("Límite inferior a: ");
                int b = leerEntero("Límite superior b: ");
                System.out.println("Códigos en [" + a + ", " + b + "]: " + cat.contarEnRango(a, b));
                break;
            case "b":
                int codigo = leerEntero("Código a buscar: ");
                System.out.println("Camino: " + cat.caminoBusqueda(codigo));
                break;
            case "c":
                System.out.println("¿Es un ABB válido? " + cat.esABBValido());
                break;
            default:
                System.out.println("Reto no válido.");
        }
    }

    private static void retoDesdePreorden() {
        String linea = leerTexto("Preorden separado por espacios (ej. 50 30 20 40 70): ");
        String[] partes = linea.isEmpty() ? new String[0] : linea.split("\\s+");
        int[] preorden = new int[partes.length];
        try {
            for (int i = 0; i < partes.length; i++) {
                preorden[i] = Integer.parseInt(partes[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Solo se permiten números enteros.");
            return;
        }
        CatalogoABB cat = CatalogoABB.desdePreorden(preorden);
        System.out.print(cat.trazaRecorrido(TipoRecorrido.INORDEN));
    }

    // ------------------------------------------------------------- entrada
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.hasNextLine() ? sc.nextLine().trim() : "";
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            if (!sc.hasNextLine()) {
                return 0;
            }
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }
}
