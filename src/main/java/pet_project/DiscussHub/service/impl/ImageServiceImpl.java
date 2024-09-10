package pet_project.DiscussHub.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pet_project.DiscussHub.service.ImageService;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final AmazonS3 s3Client;

  @Value("${aws.bucket-name}")
  private String bucketName;

  @Override
  public String uploadImage(MultipartFile image) {
    validateImageFile(image);
    String fileName = UUID.randomUUID().toString();

    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(image.getContentType());
      metadata.setContentLength(image.getSize());

      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata);
      s3Client.putObject(putObjectRequest);
    } catch (IOException e) {
      System.err.println("Error while uploading file to S3: " + e.getMessage());
    }

    return fileName;
  }

  @Override
  public String buildImageUrl(String fileName) {
    return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
  }

  @Override
  public void deleteImage(String fileName) {
    try {
      DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
      s3Client.deleteObject(deleteObjectRequest);
    } catch (Exception e) {
      System.err.println("Error deleting the image: " + e.getMessage());
    }
  }

  private void validateImageFile(MultipartFile image) {
    String fileType = image.getContentType();
    if (fileType == null || !fileType.startsWith("image/")) {
      throw new IllegalArgumentException("Only image files are allowed.");
    }
  }
}
