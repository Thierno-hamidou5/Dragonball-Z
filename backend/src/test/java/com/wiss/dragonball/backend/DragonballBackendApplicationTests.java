package com.wiss.dragonball.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basistest zum Laden des Spring Application Contexts.
 * <p>
 * Dieser Test prüft, ob die Anwendung korrekt startet und der Spring-Kontext ohne Fehler geladen wird.
 * Er dient als technischer Gesundheitstest der Konfiguration.
 * </p>
 *
 * <p><b>Hinweis:</b> Wird bei jeder Testausführung standardmäßig von Spring Boot erwartet.</p>
 *
 * @author Thierno
 * @version 1.0
 */
@SpringBootTest
class DragonballBackendApplicationTests {

	/**
	 * Prüft, ob der Spring ApplicationContext ohne Fehler geladen wird.
	 */
	@Test
	void contextLoads() {
		// Wird automatisch bestanden, wenn keine Exceptions beim Laden des Kontexts auftreten
	}
}
