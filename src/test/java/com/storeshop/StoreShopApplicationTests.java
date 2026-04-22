package com.storeshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de démarrage de l'application.
 * Vérifie que le contexte Spring Boot se charge correctement sans erreur.
 */
@SpringBootTest
@ActiveProfiles("test")
class StoreShopApplicationTests {

  /**
   * Vérifie le chargement du contexte.
   */
  @Test
  void contextLoads() {}
}
