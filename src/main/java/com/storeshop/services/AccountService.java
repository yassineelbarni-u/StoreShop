package com.storeshop.services;

import com.storeshop.entities.User;

/**
 * Application-level user operations: registration, lookup, and optional bootstrap of a default
 * admin.
 */
public interface AccountService {

  /**
   * Registers a new client account after validating uniqueness and password confirmation.
   *
   * @param username chosen login name
   * @param password plain password (will be encoded before persistence)
   * @param email contact email
   * @param ConfirmPassword must match {@code password}
   * @return the persisted user with role {@code CLIENT}
   * @throws RuntimeException if the username already exists or passwords do not match
   */
  User AddUser(String username, String password, String email, String ConfirmPassword);

  /**
   * Loads a user by login name for authentication and other lookups.
   *
   * @param username login name
   * @return the user, or {@code null} if none exists
   */
  User loadUserByUsername(String username);

  /**
   * Ensures an account exists for the given credentials: creates an {@code ADMIN} user only when
   * missing, otherwise returns the existing record. Intended for seeding or dev setup, not normal
   * client signup.
   *
   * @param username admin login
   * @param password plain password (encoded on create)
   * @param email admin email
   * @return existing or newly created user
   */
  User ensureUserExists(String username, String password, String email);
}
