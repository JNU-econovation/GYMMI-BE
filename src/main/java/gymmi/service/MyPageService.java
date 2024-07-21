package gymmi.service;

import gymmi.entity.ProfileImage;
import gymmi.entity.User;
import gymmi.exception.AlreadyExistException;
import gymmi.repository.ProfileImageRepository;
import gymmi.repository.UserRepository;
import gymmi.request.EditingMyPageRequest;
import gymmi.response.MyPageResponse;
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
    private final UserRepository userRepository;

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

    @Transactional
    public void deleteProfileImage(User loginedUser) {
        ProfileImage profileImage = profileImageRepository.getByUserId(loginedUser.getId());
        profileImageRepository.delete(profileImage);
        imageFileUploader.delete(profileImage.getStoredName());
    }

    @Transactional
    public void editMyPage(User loginedUser, EditingMyPageRequest request) {
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new AlreadyExistException("이미 존재하는 닉네임 입니다.");
        }
        loginedUser.changeNickname(request.getNickname());
    }

    public MyPageResponse getMyInfo(User loginedUser) {
        return MyPageResponse.builder()
                .nickname(loginedUser.getNickname())
                .email(loginedUser.getEmail())
                .loginId(loginedUser.getLoginId())
                .profileImage(loginedUser.getProfileImageName())
                .build();
    }
}

