package gymmi.service;

import gymmi.exception.InvalidFileException;
import gymmi.exception.NotFoundResourcesException;
import gymmi.exception.ServerLogicFaultException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class LocalImageFileUploader implements ImageFileUploader {

    private final String storagePath;

    public LocalImageFileUploader(
            @Value("${file.storage-path}") String storagePath
    ) {
        this.storagePath = storagePath;
    }

    public String upload(MultipartFile imageFile, String uuid) {
        validateImageFile(imageFile);
        try {
            String extension = StringUtils.getFilenameExtension(imageFile.getOriginalFilename());
            String filename = uuid + "." + extension;
            File file = new File(storagePath + filename);
            imageFile.transferTo(file);
            file.setReadOnly();
            return filename;
        } catch (IOException e) {
            throw new ServerLogicFaultException("파일 업로드를 실패하였습니다.", e);
        }
    }

    public void delete(String imageFileName) {
        File file = new File(storagePath + imageFileName);
        if (!file.exists()) {
            throw new NotFoundResourcesException("해당 파일이 존재하지 않습니다.");
        }
        if (!file.delete()) {
            throw new ServerLogicFaultException("파일 삭제에 실패하였습니다.");
        }
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("비어있는 파일입니다.");
        }
        if (!SUPPORTABLE_IMAGE_TYPES.contains(file.getContentType())) {
            throw new InvalidFileException("이미지 형식의 파일만 가능합니다.");
        }
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null) {
            throw new InvalidFileException("파일의 확장자가 존재하지 않습니다.");
        }

    }

}
