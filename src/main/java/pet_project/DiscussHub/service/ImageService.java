package pet_project.DiscussHub.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile image);
    String buildImageUrl(String fileName);

    void deleteImage(String fileName);
}
