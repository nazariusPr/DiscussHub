package pet_project.DiscussHub.exception;

public class TokenExpirationException extends RuntimeException {
  public TokenExpirationException() {}

  public TokenExpirationException(String message) {
    super(message);
  }
}
