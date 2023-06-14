package com.finanacialtracing.service.impl;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.AddAdminDto;
import com.finanacialtracing.dto.admin.UserDTO;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.UserRole;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.repository.RoleRepository;
import com.finanacialtracing.repository.UserRepository;
import com.finanacialtracing.service.AdminService;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public CommonResult addAdmin(AddAdminDto addAdminDto) {
        User currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findByUsernameAndIsDeleted(addAdminDto.getUsername(), Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (Objects.equals(user.getUsername(), currentUser.getUsername())) {
            throw new GenericException(Errors.CANNOT_ADD);
        }
        for (UserRole role : user.getRoles()) {
            if (Objects.equals(role.getName(), AuthorizationConstants.ADMIN_ROLE)) {
                throw new GenericException(Errors.USER_ALREADY_ADMIN);
            }
        }
        UserRole userRole = roleRepository.findByName(AuthorizationConstants.ADMIN_ROLE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        user.getRoles().add(userRole);
        User updatedUser = userRepository.save(user);
        return new CommonResult(updatedUser.getUsername() + " is now admin !");
    }

    @Override
    public CommonResult deleteAdmin(Long adminId) {
        User currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findByIdAndIsDeleted(adminId, Boolean.FALSE).orElseThrow(() -> new GenericException(Errors.NOT_FOUND));
        if (Objects.equals(user.getId(), currentUser.getId())) {
            throw new GenericException(Errors.CANNOT_DELETE);
        }
        user.getRoles().removeIf(userRole -> Objects.equals(userRole.getName(), AuthorizationConstants.ADMIN_ROLE) && Objects.equals(userRole.getName(), AuthorizationConstants.SUPER_ADMIN));
        User savedUser = userRepository.save(user);
        return new CommonResult(savedUser.getUsername() + " is successfully delete admin role");
    }

}
