package org.example.musicplayer.exception.errors;

public class UserOrganizationNotFoundException extends RuntimeException {

  public UserOrganizationNotFoundException() {
    super("Organization not found for current user");
  }
}
