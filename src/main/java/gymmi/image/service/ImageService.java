package gymmi.image.service;



import gymmi.image.controller.response.PresignedUrlResponse;
import gymmi.global.exception.exceptiontype.InvalidStateException;
import gymmi.global.exception.message.ErrorCode;
import gymmi.global.infra.s3.S3Client;
import gymmi.image.domain.ImageUse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;

    public PresignedUrlResponse getPresingedUrlWithPut(ImageUse imageUse) {
        return s3Client.generatePresignedUrlWithPut(imageUse.getDirectory());
    }

    public String getPresignedUrl(ImageUse imageUse, String filename) {
        return s3Client.generatePresignedUrl(imageUse.getDirectory(), filename);
    }

    public void delete(ImageUse imageUse, String filename) {
        s3Client.deleteObject(imageUse.getDirectory(), filename);
    }

    public void validateObjectPresence(ImageUse imageUse, String filename) {
        if (!s3Client.doesObjectExist(imageUse.getDirectory(), filename)) {
            throw new InvalidStateException(ErrorCode.NOT_FOUND_IMAGE_OBJECT);
        }
    }

    public String copy(ImageUse sourceImageUse, String sourceFilename, ImageUse destinationImageUse) {
        validateObjectPresence(sourceImageUse, sourceFilename);
        return s3Client.copyObject(sourceImageUse.getDirectory(), sourceFilename, destinationImageUse.getDirectory());
    }

}
