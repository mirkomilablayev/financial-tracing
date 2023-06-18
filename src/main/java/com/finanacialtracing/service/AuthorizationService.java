package com.finanacialtracing.service;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.ChangePasswordDto;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.UserRole;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.repository.UserRepository;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.finanacialtracing.controller.AuthorizationController.USERNAME_VALIDATION_PATTERN;

@Service
@AllArgsConstructor
@Slf4j
public class AuthorizationService  {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public CommonResult changeUsername(String newUsername) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (newUsername != null && !newUsername.matches(USERNAME_VALIDATION_PATTERN)) {
            throw new GenericException(Errors.USERNAME_VALIDATION_ERROR);
        }
        if (userRepository.existsByUsernameAndIsDeleted(newUsername, Boolean.FALSE)) {
            throw new GenericException(Errors.USERNAME_ALREADY_TAKEN);
        }
        currentUser.setUsername(newUsername);
        User save = userRepository.save(currentUser);
        return new CommonResult("Username updated to "+save.getUsername());
    }

    public CommonResult changePassword(ChangePasswordDto changePasswordDto) {
        User currentUser = SecurityUtils.getCurrentUser();

        if (!Objects.equals(currentUser.getUsername(), changePasswordDto.getUsername())){
            throw new GenericException(Errors.CANNOT_CHANGE);
        }
        if (Objects.equals(changePasswordDto.getOldPassword(), changePasswordDto.getNewPassword())){
            throw new GenericException(Errors.CANNOT_CHANGE);
        }

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), currentUser.getPassword())){
            throw new GenericException(Errors.CANNOT_CHANGE);
        }

        currentUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        User savedUser = userRepository.save(currentUser);
        return new CommonResult(savedUser.getUsername() + "'s password is successfully updated");
    }

    public CommonResult deleteUser() {
        User currentUser = SecurityUtils.getCurrentUser();
        boolean flag = false;
        for (UserRole role : currentUser.getRoles()) {
            if (role.getName().equals(AuthorizationConstants.SUPER_ADMIN)) {
                flag = true;
            }
        }
        if (flag) throw new GenericException(Errors.CANNOT_DELETE);
        currentUser.setIsDeleted(Boolean.TRUE);
        User deletedUser = userRepository.save(currentUser);
        return new CommonResult(deletedUser.getUsername() + " s successfully  deleted");
    }

    public CommonResult editFullName(String newFullName) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (!Objects.nonNull(newFullName) || Objects.equals(newFullName.length(), 0)) {
            throw new GenericException(Errors.CANNOT_CHANGE);
        }

        currentUser.setFullName(newFullName);
        User updatedUser = userRepository.save(currentUser);
        return new CommonResult(updatedUser.getUsername() + " s successfully  deleted");
    }
}
