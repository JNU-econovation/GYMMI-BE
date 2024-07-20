package gymmi.service;

import gymmi.entity.ProfileImage;
import gymmi.entity.User;
import gymmi.repository.ProfileImageRepository;
import gymmi.response.ProfileImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final ImageFileUploader imageFileUploader;
    private final ProfileImageRepository profileImageRepository;

    @Transactional
    public ProfileImageResponse setProfileImage(User loginedUser, MultipartFile profileImageFile) {
        profileImageRepository.findByUserId(loginedUser.getId())
                .ifPresent(profileImage -> profileImageRepository.delete(profileImage));

        String filename = UUID.randomUUID().toString();
        ProfileImage newProfileImage = ProfileImage.builder()
                .owner(loginedUser)
                .originName(profileImageFile.getOriginalFilename())
                .storedName(filename)
                .build();
        ProfileImage savedProfileImage = profileImageRepository.save(newProfileImage);

        imageFileUploader.upload(profileImageFile, filename);
        return new ProfileImageResponse(savedProfileImage);
    }

}

