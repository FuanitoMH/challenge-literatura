package com.alura.literatura_alura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anoNacimiento;
    private Integer anoFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.anoNacimiento = datosAutor.anoNacimiento();
        this.anoFallecimiento = datosAutor.anoFallecimiento();
        this.libros = new ArrayList<Libro>();
    }

    public void agregarLibro(Libro libro){
        libro.setAutor(this);
        this.libros.add(libro);
    }
    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getAnoNacimiento() {
        return anoNacimiento;
    }

    public Integer getAnoFallecimiento() {
        return anoFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    @Override
    public String toString() {
        return "----- AUTOR -----" + "\n" +
                "Autor: " + this.getNombre() + "\n" +
                "Fecha de nacimiento: " + this.getAnoNacimiento() + "\n" +
                "Fecha de fallecimeinto: " + this.getAnoFallecimiento() + "\n"+
                "Libros: " + this.getLibros().stream().map(l -> l.getTitulo()).collect(Collectors.toList());
    }
}
