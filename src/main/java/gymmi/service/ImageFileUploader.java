package gymmi.service;

import java.util.Set;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageFileUploader {

    Set<String> SUPPORTABLE_IMAGE_TYPES = Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);
    Set<String> IMAGE_EXTENSIONS = Set.of("jpeg", "jpg", "png", "webp", "heic", "heif");


    String upload(MultipartFile file, String fileName);

    void delete(String filename);

}
