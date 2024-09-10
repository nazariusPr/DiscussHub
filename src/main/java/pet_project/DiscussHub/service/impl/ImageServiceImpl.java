package pet_project.DiscussHub.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
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
    File file = convertMultiPartFileToFile(image);
    String fileName = uploadFileToS3Bucket(file);
    file.deleteOnExit();

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

  private File convertMultiPartFileToFile(MultipartFile multipartFile) {

    final File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(multipartFile.getBytes());
    } catch (final IOException ex) {
      System.out.println("Error converting the multi-part file to file= " + ex.getMessage());
    }

    return file;
  }

  private String uploadFileToS3Bucket(File file) {
    String fileName = UUID.randomUUID() + file.getName();
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
    s3Client.putObject(putObjectRequest);

    return fileName;
  }
}
