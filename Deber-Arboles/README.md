# Deber-Arboles-ApellidoNombre

Catálogo de la Biblioteca FISEI en Java: **árbol general n-ario** (Biblioteca → Áreas → Estantes)
y **Árbol Binario de Búsqueda** por estante (libros ordenados por código).
Asignatura: Estructura de Datos · APE 3 · Deber individual.

## Problema
La biblioteca se organiza en Áreas y cada Área en Estantes (árbol general). Cada Estante guarda sus
libros en un ABB ordenado por código (entero positivo, sin repetidos). Todas las operaciones son
recursivas y no se usan `TreeSet`, `TreeMap`, `PriorityQueue`, `NavigableSet/Map`, `Collections.sort` ni `List.sort`.

## Estructura
```
src/
├── enums/    TipoUnidad.java, TipoRecorrido.java
├── modelo/   Libro.java, UnidadBiblioteca.java
├── negocio/  NodoUnidad.java, NodoLibro.java, ArbolBiblioteca.java, CatalogoABB.java
└── app/      Main.java, DatosPrueba.java, PruebasBiblioteca.java
```

## Ejecución
Requiere JDK 11 o superior.

```bash
# compilar
mkdir -p out
javac -d out $(find src -name "*.java")      # Linux / macOS / Git Bash
# en PowerShell:  javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName

# menú interactivo
java -cp out app.Main

# pruebas (muestra X/N pruebas OK)
java -cp out app.PruebasBiblioteca
```

En Visual Studio Code: abrir la carpeta, marcar `src` como carpeta de fuentes y ejecutar `Main` o `PruebasBiblioteca`.

## Captura de PruebasBiblioteca
_(pegar aquí la captura de la salida `33/33 pruebas OK`)_
