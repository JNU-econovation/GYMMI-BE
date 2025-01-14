package gymmi.global.firebase;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class SendingRequest {
    private final String title;
    private final String body;
    private final String userToken;
    private final String redirectUrl;
    private final String createdAt;

}
