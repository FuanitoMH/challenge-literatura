package com.alura.literatura_alura.principal;

import com.alura.literatura_alura.model.Autor;
import com.alura.literatura_alura.model.Datos;
import com.alura.literatura_alura.model.DatosLibro;
import com.alura.literatura_alura.model.Libro;
import com.alura.literatura_alura.repository.AutorRepository;
import com.alura.literatura_alura.services.ConsumoAPI;
import com.alura.literatura_alura.services.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private AutorRepository repository;

    public Principal(AutorRepository repositorio) {
        this.repository = repositorio;
    }

    public void muestraMenu(){
        var opcion = -1;
        var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;

        while (opcion != 0) {
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    buscarLibrosRegistrados();
                    break;
                case 3:
                    buscarAutoresRegistrados();
                    break;
                case 4:
                    buscarAutoresPorAno();
                    break;
                case 5:
                    buscarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion");
                    break;
                default:
                    System.out.println("Opcion no valida!");
            }
        }
    }



    private DatosLibro getDatos() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search="+ nombreLibro.replace(" ", "+"));
        //System.out.println(json);

        Datos datos = conversor.obtenerDatos(json, Datos.class);
        //System.out.println("Datos: " + datos.resultados());

        Optional<DatosLibro> datosLibro = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        // Si el libro buscado existe
        if (datosLibro.isPresent()) {
            DatosLibro libroEncontrado = datosLibro.get();
            return libroEncontrado;
        }
        return null;
    }

    private Autor guardarAutor(Autor autor){
        Optional autorEncontrado = repository.findAutorByNombreContainingIgnoreCase(autor.getNombre());

        if (autorEncontrado.isPresent()){
            // System.out.println("Autor ya estaba registrado: \n");
            Autor autorRegistrado = (Autor) autorEncontrado.get();
            return autorRegistrado;
        }else{
            repository.save(autor);
            // System.out.println("El autor del libro fue registrado");
            return autor;
        }
    }

    private void buscarLibro() {
        DatosLibro libroEncontrado = getDatos();

        if (libroEncontrado == null) {
            System.out.println("Libro no encontrado:(");
            return;
        }

        System.out.println("Libro encontrado: ");
        Libro libro = new Libro(libroEncontrado);
        System.out.println(libro);

        // Guardamos el autor o obtenemos el que ya esté registrado
        Autor autorLibro = new Autor(libroEncontrado.autores().get(0));
        Autor autorRegistrado = guardarAutor(autorLibro);

        Optional<Libro> libroDeAutor = autorRegistrado.getLibros().stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(libro.getTitulo()))
                .findFirst();

        // Si el libro no está agregado al autor, se lo agregamos
        if (!libroDeAutor.isPresent()) {
            // agregamos el libro al autor y guardamos los cambios
            autorRegistrado.agregarLibro(libro);
            // System.out.println("Autor del libro: \n"+ autorRegistrado);
            repository.save(autorRegistrado);
            // System.out.println("Se registro el libro al autor");
        }
    }

    private void buscarLibrosRegistrados() {
        List<Libro> libros = repository.obtenerLibros();
        libros.stream()
                .forEach(l -> System.out.println(l + "\n"));
    }

    private void buscarAutoresRegistrados(){
        List<Autor> autores = repository.obtenerAutores();
        autores.stream()
                .forEach(a -> System.out.println(a + "\n"));
    }

    public void buscarAutoresPorAno(){
        System.out.println("Ingrese el año para obtener los autores vivos:");
        int ano = teclado.nextInt();
        List<Autor> autores = repository.obtenerAutoresPorAno(ano);
        autores.stream()
                .forEach(a -> System.out.println(a + "\n"));
    }

    public void buscarLibrosPorIdioma() {
        System.out.println("""
                Escriba la clave del idioma 
                en - ingles
                es - español
                fr - frances
                pt - portugués
                """);
        String idioma = teclado.nextLine();

        List<String> idiomas = new ArrayList<>();
        idiomas.add("en");
        idiomas.add("es");
        idiomas.add("fr");
        idiomas.add("pt");

        if(!idiomas.contains(idioma)){
            System.out.println("Idioma no valido");
            return;
        }

        List<Libro> libros = repository.obtenerLibrosPorIdioma(idioma);
        libros.stream()
                .forEach(l -> System.out.println(l + "\n"));
    }
}
