package com.storeshop.entities;

/**
 * Définit les différents rôles et niveaux d'accès disponibles dans l'application.
 */
public enum Role {
  /** Rôle utilisateur standard. */
  USER,
  /** Rôle client pouvant effectuer des achats. */
  CLIENT,
  /** Rôle administrateur ayant accès à la gestion complète. */
  ADMIN
}
