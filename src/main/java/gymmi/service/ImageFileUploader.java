package gymmi.service;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageFileUploader {

    List<String> SUPPORTABLE_IMAGE_TYPES = List.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);

    String upload(MultipartFile file, String fileName);

    void delete(String filename);

}
