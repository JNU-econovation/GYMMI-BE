package gymmi.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import gymmi.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public PresignedUrlResponse generatePresignedUrlWithPut(String directory) {
        String filename = UUID.randomUUID().toString();
        String imageUrl = directory + filename;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = createPresignedUrlRequest(imageUrl, HttpMethod.PUT);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        String presignedUrl = url.toString();
        return new PresignedUrlResponse(presignedUrl, filename);
    }

    public String generatePresignedUrl(String directory, String filename) {
        String imageUrl = directory + filename;
        GeneratePresignedUrlRequest request = createPresignedUrlRequest(imageUrl, HttpMethod.GET);
        URL url = amazonS3.generatePresignedUrl(request);
        String presignedUrl = url.toString();
        return presignedUrl;
    }

    private GeneratePresignedUrlRequest createPresignedUrlRequest(String imageUrl, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, imageUrl)
                .withMethod(httpMethod)
                .withExpiration(getPreSignedUrlExpiration());
        request.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.Private.toString());
        return request;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 1;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

}
