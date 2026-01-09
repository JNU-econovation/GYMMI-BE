package gymmi.etc.service;

import gymmi.etc.domain.ImageFileUploader;
import gymmi.etc.domain.entity.ProfileImage;
import gymmi.etc.domain.entity.User;
import gymmi.global.exceptionhandler.exception.AlreadyExistException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.etc.repository.ProfileImageRepository;
import gymmi.etc.repository.UserRepository;
import gymmi.etc.controller.request.EditingMyPageRequest;
import gymmi.etc.controller.response.MyPageResponse;
import gymmi.etc.controller.response.ProfileImageResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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

        String extension = StringUtils.getFilenameExtension(profileImageFile.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;
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
            throw new AlreadyExistException(ErrorCode.ALREADY_USED_NICKNAME);
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

