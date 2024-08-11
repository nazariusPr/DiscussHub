package pet_project.DiscussHub.utils;

import pet_project.DiscussHub.exception.NullDtoReferenceException;

public class ValidationUtils {
  public static void validateRequest(Object request) {
    if (request == null) throw new NullDtoReferenceException("Request can not be null!");
  }
}
