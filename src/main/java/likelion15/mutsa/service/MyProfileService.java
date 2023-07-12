package likelion15.mutsa.service;

import likelion15.mutsa.dto.PasswordDto;
import likelion15.mutsa.dto.ProfileDto;
import likelion15.mutsa.entity.*;
import likelion15.mutsa.repository.FileConRepository;
import likelion15.mutsa.repository.FileRepository;
import likelion15.mutsa.repository.ProfileRepository;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final FileConRepository fileConRepository;


    @Transactional
    public User join(User user) {
        return userRepository.save(user);
    }

    public User readById(Long userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return userRepository.findById(userId).get();
    }

    public User readUser(String userName) {
        if (userRepository.findByName(userName).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return userRepository.findByName(userName).get();
    }

    public ProfileDto readProfile(String userName) {
        ProfileDto profileDto = new ProfileDto();
        if (profileRepository.findByUser_Name(userName).isEmpty())
            return profileDto;
        Profile profile = profileRepository.findByUser_Name(userName).get();

        profileDto.setContent(profile.getContent());

        if (profile.getFileCon() != null) {
            String imgPath = profile.getFileCon()
                    .get(profile.getFileCon().size() - 1)
                    .getFile()
                    .getPath();
            log.info("자르기전 : {}", imgPath);
            String imgFilename = new java.io.File(imgPath).getName();
            log.info("자르고 나서 : {}", imgFilename);
            profileDto.setImgPath(imgFilename);
        }

        return profileDto;
    }
    @Transactional
    public Long updateRealName(Long userId, String realName) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updateRealName(realName);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updateName(Long userId, String name) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updateName(name);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updateEmail(Long userId, String email) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updateEmail(email);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updatePhoneNumber(Long userId, String phoneNumber) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updatePhoneNumber(phoneNumber);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updatePassword(Long userId, PasswordDto passwordDto) {
        User oldUser = userRepository.findById(userId).get();
        if (
                oldUser.getPassword().equals(passwordDto.getPassword()) &&
                        !passwordDto.getPassword().equals(passwordDto.getNewPassword()) &&
                        passwordDto.getNewPassword().equals(passwordDto.getNewPasswordCheck())
        ) {
            oldUser.updatePassword(passwordDto.getNewPassword());
            return userRepository.save(oldUser).getId();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Long updateProfileContent(Long userId, String content) {
        Optional<Profile> optionalProfile
                = profileRepository.findByUser_Id(userId);
        Profile profile;
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
            profile.updateProfileContent(content);

        } else {
            profile = Profile.builder()
                    .user(userRepository.findById(userId).get())
                    .content(content)
                    .build();
        }
        return profileRepository.save(profile).getId();
    }
    @Transactional
    public Long updateProfileImage(Long userId, MultipartFile image) {
        Optional<Profile> optionalProfile
                = profileRepository.findByUser_Id(userId);
        Profile profile;
        FileCon fileCon;
        File fileEntity = fileService.createFile(image);

        // 프로필 등록한 적 없으면 새로 만들고
        if (optionalProfile.isEmpty()) {
            profile = Profile.builder()
                    .user(userRepository.findById(userId).get())
                    .build();
        }
        // 등록한 적 있으면 찾기
        else profile = optionalProfile.get();

        // filecon, file, profile 양방향 관계 설정
        fileCon = FileCon.builder()
                .file(fileEntity)
                .profile(profile)
                .build();
//        fileEntity.getFileCon().add(fileCon);
//        profile.getFileCon().add(fileCon);

        return fileConRepository.save(fileCon).getId();

    }


}
