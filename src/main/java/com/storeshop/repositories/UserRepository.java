package com.storeshop.repositories;

import com.storeshop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de gestion de la persistance pour les utilisateurs.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
  /**
   * Recherche un utilisateur par son nom d'utilisateur (username).
   * 
   * @param username Le nom d'utilisateur.
   * @return L'utilisateur correspondant ou null.
   */
  User findByUsername(String username);
}
