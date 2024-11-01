package gymmi.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import gymmi.response.PresignedUrlResponse;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.path}")
    private String s3DefaultPath;

    public PresignedUrlResponse getPutPresignedUrl() {
        return generatePutPresignedUrl(bucketName, s3DefaultPath);
    }

    public PresignedUrlResponse generatePutPresignedUrl(String bucket, String filePath) {
        String filename = UUID.randomUUID().toString();
        String imageUrl = filePath + filename;
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, imageUrl)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.Private.toString());

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        String presignedUrl = url.toString();
        return new PresignedUrlResponse(presignedUrl, filename);
    }

    public String getPresignedUrl(String filename){
        String imageUrl = s3DefaultPath + filename;
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, imageUrl)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.Private.toString());

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        String presignedUrl = url.toString();
        return presignedUrl;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

}
