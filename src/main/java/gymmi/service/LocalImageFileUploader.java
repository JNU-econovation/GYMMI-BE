package gymmi.service;

import static gymmi.exceptionhandler.message.ErrorCode.EMPTY_FILE;
import static gymmi.exceptionhandler.message.ErrorCode.FAILED_FILE_UPLOAD;
import static gymmi.exceptionhandler.message.ErrorCode.MISSING_FILE_EXTENSION;
import static gymmi.exceptionhandler.message.ErrorCode.NOT_FOUND_FILE;
import static gymmi.exceptionhandler.message.ErrorCode.UNSUPPORTED_FILE;

import gymmi.exceptionhandler.exception.FileIOFailException;
import gymmi.exceptionhandler.exception.InvalidFileException;
import gymmi.exceptionhandler.exception.NotFoundException;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalImageFileUploader implements ImageFileUploader {

    private final String storagePath;

    public LocalImageFileUploader(
            @Value("${file.storage-path}") String storagePath
    ) {
        this.storagePath = storagePath;
    }

    public String upload(MultipartFile imageFile, String fileName) {
        validateImageFile(imageFile);
        try {
            File file = new File(storagePath + fileName);
            imageFile.transferTo(file);
            file.setReadOnly();
            return fileName;
        } catch (IOException e) {
            throw new FileIOFailException(FAILED_FILE_UPLOAD, e);
        }
    }

    public void delete(String imageFileName) {
        File file = new File(storagePath + imageFileName);
        if (!file.exists()) {
            throw new NotFoundException(NOT_FOUND_FILE);
        }
        if (!file.delete()) {
            throw new FileIOFailException(FAILED_FILE_UPLOAD);
        }
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException(EMPTY_FILE);
        }
        if (!SUPPORTABLE_IMAGE_TYPES.contains(file.getContentType())) {
            throw new InvalidFileException(UNSUPPORTED_FILE);
        }
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null) {
            throw new InvalidFileException(MISSING_FILE_EXTENSION);
        }

    }

}
