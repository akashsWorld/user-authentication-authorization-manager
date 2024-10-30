package com.cromxt.user.service.impl;

import com.cromxt.user.dtos.requests.*;
import com.cromxt.user.entity.ProfileAvatar;
import com.cromxt.user.entity.RecoveryAccountDetails;
import com.cromxt.user.entity.UserEntity;
import com.cromxt.user.exceptions.ImageTypeNotValidException;
import com.cromxt.user.exceptions.UserAlreadyExistsException;
import com.cromxt.user.exceptions.UserNotFoundException;
import com.cromxt.user.repository.ProfileAvatarRepository;
import com.cromxt.user.repository.RecoveryAccountDetailsRepository;
import com.cromxt.user.repository.UserEntityRepository;
import com.cromxt.user.service.DTOService;
import com.cromxt.user.service.StorageService;
import com.cromxt.user.service.UserDetailService;
import com.cromxt.user.service.UtilService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailService{

    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("image/png", "image/jpg", "image/jpeg");
    private static final String EMAIL_DOMAIN= "@cmail.com";

    private final DTOService dtoService;
    private final UserEntityRepository userEntityRepository;
    private final RecoveryAccountDetailsRepository recoveryAccountDetailsRepository;
    private final ProfileAvatarRepository profileAvatarRepository;
    private final StorageService storageService;
    private final UtilService utilService;
    private final DateFormatter dateFormatter;

    @Override
    @Transactional
    public void saveUser(RegisterUserDTO registerUser) {
        String email = registerUser.email();
        boolean exists = userEntityRepository.existsByUsername(email);
        if(exists)
            throw new UserAlreadyExistsException("Already exists a user with email: " + email);
        UserEntity user = dtoService.getUserEntity(registerUser);
        RecoveryAccountDetails recoveryAccountDetails = dtoService.getRecoveryAccountDetails(registerUser.recoveryAccountDetails());
        userEntityRepository.save(user);
        recoveryAccountDetails.setUser(user);
        recoveryAccountDetailsRepository.save(recoveryAccountDetails);
    }

    @Override
    @Transactional
    public void updateUser(String email, UpdateUserDTO updateUserDTO) {
        UserEntity userEntity = userEntityRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        dtoService.updateUser(userEntity,updateUserDTO);
        RecoveryAccountDetailsDTO recoveryAccountDetailsDTO = updateUserDTO.recoveryAccountDetails();
//
        if(recoveryAccountDetailsDTO!=null){
            recoveryAccountDetailsRepository.findById(userEntity.getId()).ifPresent(
                    recoveryAccountDetails ->{
                        dtoService.recoveryAccountDetails(recoveryAccountDetails,recoveryAccountDetailsDTO);
                        recoveryAccountDetailsRepository.save(recoveryAccountDetails);
                    }
            );
        }
        userEntityRepository.save(userEntity);
    }

    @Override
    public void updateEmail(String email, EmailDetailDTO emailDetailDTO) {
        UserEntity userEntity = userEntityRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        userEntity.setUsername(emailDetailDTO.newEmail());
        userEntityRepository.save(userEntity);
    }

    @Override
    public void updatePassword(String email, PasswordDetailsDTO passwordDetails) {
//            TODO: Mail a link into mailbox to update password through a html page.
    }

    @Override
    @Transactional
    public void updateProfileImage(String email, MultipartFile profileImage) {
        if(!ALLOWED_FILE_EXTENSIONS.contains(profileImage.getContentType())){
            throw new ImageTypeNotValidException("Invalid image type: " + profileImage.getContentType());
        }
        UserEntity userEntity = userEntityRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));

        Optional<ProfileAvatar> profileAvatarOptional = profileAvatarRepository.findById(userEntity.getId());

        profileAvatarOptional.ifPresentOrElse(avatar->{
                storageService.saveProfileAvatar(profileImage,avatar.getUrl());
        },()->{
            String profileUrl = utilService.generateUrlForAvatar(userEntity.getId(),profileImage.getOriginalFilename());
            ProfileAvatar profileAvatar = dtoService.getProfileAvatar(profileImage,profileUrl);
            profileAvatar.setUser(userEntity);
            profileAvatarRepository.save(profileAvatar);
            storageService.saveProfileAvatar(profileImage,profileUrl);
        });
    }

    @Override
    @Transactional
    public void deleteProfileAvatar(String email) {
        UserEntity userEntity = userEntityRepository.findByUsername(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        profileAvatarRepository.deleteById(userEntity.getId());
        storageService.deleteProfileAvatar(userEntity.getId());
    }

    @Override
    public Boolean isEmailValid(String email) {
        return true;
    }


}
