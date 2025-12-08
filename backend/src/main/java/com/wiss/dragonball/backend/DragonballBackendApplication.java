package com.wiss.dragonball.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse der Spring Boot Anwendung für das Dragonball-Backend.
 * <p>
 * Diese Klasse enthält den Einstiegspunkt der Applikation
 * und startet den eingebetteten Server sowie die Spring-Konfiguration.
 * </p>
 *
 * @author Thierno
 * @version 1.0
 */
@SpringBootApplication
public class DragonballBackendApplication {

	/**
	 * Einstiegspunkt der Anwendung.
	 *
	 * @param args Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		SpringApplication.run(DragonballBackendApplication.class, args);
	}

}
