package com.cromxt.user.service;


import com.cromxt.user.dtos.requests.RecoveryAccountDetailsDTO;
import com.cromxt.user.dtos.requests.RegisterUserDTO;
import com.cromxt.user.dtos.requests.UpdateUserDTO;
import com.cromxt.user.entity.ProfileAvatar;
import com.cromxt.user.entity.RecoveryAccountDetails;
import com.cromxt.user.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;


public interface DTOService {
    UserEntity getUserEntity(RegisterUserDTO registerUser);
    void updateUser(UserEntity userEntity, UpdateUserDTO updateUserDTO);
    RecoveryAccountDetails getRecoveryAccountDetails(RecoveryAccountDetailsDTO recoveryAccountDetailsDTO);
    ProfileAvatar getProfileAvatar(MultipartFile profileImage, String url);
    void recoveryAccountDetails(RecoveryAccountDetails recoveryAccountDetails, RecoveryAccountDetailsDTO recoveryAccountDetailsDTO);
}
