package com.alura.literatura_alura.repository;

import com.alura.literatura_alura.model.Autor;
import com.alura.literatura_alura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findAutorByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT l FROM Libro l")
    List<Libro> obtenerLibros();

    @Query("SELECT a FROM Autor a")
    List<Autor> obtenerAutores();

    @Query("SELECT a FROM Autor a WHERE :ano BETWEEN a.anoNacimiento AND a.anoFallecimiento")
    List<Autor> obtenerAutoresPorAno(int ano);

    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> obtenerLibrosPorIdioma(String idioma);
}
