package gymmi.response;

import gymmi.entity.ProfileImage;
import lombok.Getter;

@Getter
public class ProfileImageResponse {


    private final Long id;
    private final String originName;
    private final String storedName;


    public ProfileImageResponse(ProfileImage profileImage) {
        this.id = profileImage.getId();
        this.originName = profileImage.getOriginName();
        this.storedName = profileImage.getStoredName();
    }
}
