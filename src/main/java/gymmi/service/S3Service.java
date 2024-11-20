package gymmi.service;

import gymmi.exceptionhandler.exception.InvalidStateException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

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

    public void checkObjectExist(ImageUse imageUse, String filename) {
        if (!s3Client.doesObjectExist(imageUse.getDirectory(), filename)) {
            throw new InvalidStateException(ErrorCode.NOT_FOUND_IMAGE_OBJECT);
        }
    }

    public String copy(ImageUse sourceImageUse, String sourceFilename, ImageUse destinationImageUse) {
        checkObjectExist(sourceImageUse, sourceFilename);
        return s3Client.copyObject(sourceImageUse.getDirectory(), sourceFilename, destinationImageUse.getDirectory());
    }

}
