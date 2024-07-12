package com.alura.literatura_alura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Integer descargas;
    @ManyToOne
    private Autor autor;

    public Libro(){}

    public Libro (DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.autor = new Autor(datosLibro.autores().get(0));
        this.idioma = datosLibro.idiomas().get(0);
        this.descargas = datosLibro.descargas();
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public Integer getDescargas() {
        return descargas;
    }

    @Override
    public String toString() {
        return " ----- LIBRO ----- " + "\n" +
                "Titulo: " + this.getTitulo() + "\n" +
                "Autor: " + this.getAutor().getNombre() + "\n" +
                "Idioma: " + this.getIdioma() + "\n" +
                "Numero de descargas: " + this.getDescargas() + "\n" +
                "----------------- ";
    }
}
