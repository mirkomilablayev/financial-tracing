package com.finanacialtracing;


import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.ChangePasswordDto;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.UserRole;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.repository.UserRepository;
import com.finanacialtracing.service.UserService;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User getTestUser() {
        User user = new User();
        user.setId(3L);
        user.setUsername("test user");
        user.setPassword("test user password");
        user.setIsDeleted(false);
        user.setFullName("Test user full name");
        Set<UserRole> roles = new HashSet<>(Set.of(new UserRole(1L, "USER_ROLE", Boolean.FALSE)));
        user.setRoles(roles);
        return user;
    }

    private User getMockUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setFullName("Full name");
        user.setIsDeleted(false);
        Set<UserRole> roles = new HashSet<>(Set.of(
                new UserRole(1L, AuthorizationConstants.USER_ROLE, Boolean.FALSE),
                new UserRole(1L, AuthorizationConstants.ADMIN_ROLE, Boolean.FALSE),
                new UserRole(1L, AuthorizationConstants.SUPER_ADMIN, Boolean.FALSE)));
        user.setRoles(roles);
        return user;
    }

    @Test
    void addAdminShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(userRepository.existsByUsernameAndIsDeleted(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(false);

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .thenReturn(mockUser);


            CommonResult commonResult = userService.changeUsername("username");
            Assertions.assertEquals(Errors.SUCCESS.getCode(), commonResult.getCode());
        }
    }

    @Test
    void addAdminShouldReturnUSER_ALREADY_TAKEN() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(userRepository.existsByUsernameAndIsDeleted(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(true);

            GenericException exception = Assertions.assertThrows(GenericException.class, () ->
                    userService.changeUsername("username")
            );

            Assertions.assertEquals(Errors.USERNAME_ALREADY_TAKEN, exception.getError());
        }
    }


    @Test
    public void changePasswordShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(true);
            Mockito.when(passwordEncoder.encode(ArgumentMatchers.anyString())).thenReturn("dhjfsjkdhfasjdhf4f74fh47hfhdsfs78dfh");

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .thenReturn(mockUser);

            CommonResult commonResult = userService.changePassword(new ChangePasswordDto(mockUser.getUsername(), mockUser.getPassword(), "newPassword"));
            Assertions.assertEquals(Errors.SUCCESS.getMessage(), commonResult.getMessage());
        }
    }


    @Test
    public void changePasswordShouldReturnCANNOT_CHANGE() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);


            Mockito.when(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(false);

            GenericException genericException = Assertions.assertThrows(GenericException.class, () -> userService.changePassword(new ChangePasswordDto(mockUser.getUsername(), mockUser.getPassword(), "newPassword")));
            Assertions.assertEquals(Errors.CANNOT_CHANGE, genericException.getError());
        }
    }

    @Test
    public void deleteUserShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .thenReturn(mockUser);
            Assertions.assertEquals(Errors.SUCCESS.getMessage(), userService.deleteUser().getMessage());
        }
    }

    @Test
    public void deleteUserShouldReturnCANNOT_DELETE() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            GenericException genericException = Assertions.assertThrows(GenericException.class, () -> userService.deleteUser());
            Assertions.assertEquals(Errors.CANNOT_DELETE.getMessage(), genericException.getError().getMessage());
        }
    }

    @Test
    public void editFullNameShouldReturnSUCCESS(){
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .thenReturn(mockUser);
            Assertions.assertEquals(Errors.SUCCESS.getMessage(), userService.editFullName("new full name").getMessage());
        }
    }

    @Test
    public void editFullNameShouldReturnCANNOT_CHANGE(){
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);


            GenericException genericException = Assertions.assertThrows(GenericException.class, () -> userService.editFullName(""));
            Assertions.assertEquals(Errors.CANNOT_CHANGE.getMessage(), genericException.getError().getMessage());
        }
    }

}
