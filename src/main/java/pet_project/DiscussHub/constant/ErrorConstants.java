package pet_project.DiscussHub.constant;

public class ErrorConstants {
  private ErrorConstants() {}

  public static final String USER_NOT_VERIFIED_MESSAGE = "User is not verified!";
  public static final String ENTITY_NOT_FOUND_MESSAGE =
      "Entity with such value = %s was not found!";
  public static final String INVALID_TOKEN_MESSAGE = "Invalid Token";
  public static final String FILE_UPLOADING_ERROR_MESSAGE = "Error while uploading file to S3";
  public static final String FILE_DELETING_ERROR_MESSAGE = "Error deleting the file";
  public static final String IMAGE_TYPE_RESTRICTION_MESSAGE = "Only image files are allowed";
}
