package com.storeshop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un utilisateur du système.
 * Gère les informations d'authentification et le rôle (ADMIN, CLIENT).
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
  /**
   * Identifiant unique de l'utilisateur (UUID).
   */
  @Id private String userId;

  /**
   * Nom d'utilisateur unique utilisé pour la connexion.
   */
  @Column(unique = true)
  private String username;

  /**
   * Mot de passe encodé de l'utilisateur.
   */
  private String password;

  /**
   * Adresse email de contact.
   */
  private String email;

  /**
   * Rôle de l'utilisateur définissant ses permissions.
   */
  private Role role;
}
