package com.alura.literatura_alura;

import com.alura.literatura_alura.principal.Principal;
import com.alura.literatura_alura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaAluraApplication implements CommandLineRunner {

	@Autowired
	private AutorRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.muestraMenu();
	}
}
