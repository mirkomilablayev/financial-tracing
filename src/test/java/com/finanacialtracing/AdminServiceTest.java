package com.finanacialtracing;


import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.AddAdminDto;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.UserRole;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.repository.RoleRepository;
import com.finanacialtracing.repository.UserRepository;
import com.finanacialtracing.service.AdminService;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

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

            User testUser = getTestUser();
            Mockito.when(userRepository.findByUsernameAndIsDeleted(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(testUser));

            UserRole roleAdmin = new UserRole(2L, "ROLE_ADMIN", Boolean.FALSE);
            Mockito.when(roleRepository.findByName(ArgumentMatchers.anyString()))
                    .thenReturn(Optional.of(roleAdmin));

            testUser.getRoles().add(roleAdmin);

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .thenReturn(testUser);

            // Invoking the method under test
            CommonResult commonResult = adminService.addAdmin(new AddAdminDto("test user"));

            // Verifying the result
            Assertions.assertEquals(Errors.SUCCESS.getCode(), commonResult.getCode());
        }
    }

    @Test
    void addAdminShouldThrowCANNOT_ADD() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(userRepository.findByUsernameAndIsDeleted(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(mockUser));


            GenericException exception = Assertions.assertThrows(GenericException.class, () ->
                    adminService.addAdmin(new AddAdminDto("test user"))
            );

            Assertions.assertEquals(Errors.CANNOT_ADD, exception.getError());
        }
    }



    @Test
    void addAdminShouldThrowUSER_ALREADY_ADMIN() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            User testUser = getTestUser();
            UserRole userRole = new UserRole(2L, AuthorizationConstants.ADMIN_ROLE, Boolean.FALSE);
            testUser.getRoles().add(userRole);
            Mockito.when(userRepository.findByUsernameAndIsDeleted(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(testUser));;

            GenericException exception = Assertions.assertThrows(GenericException.class, () ->
                    adminService.addAdmin(new AddAdminDto("test user"))
            );

            Assertions.assertEquals(Errors.USER_ALREADY_ADMIN, exception.getError());
        }
    }

    @Test
    public void deleteAdminShouldReturnSuccess(){
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            User testUser = getTestUser();
            Mockito.when(userRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(testUser));

            Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                    .thenReturn(testUser);

            // Invoking the method under test
            CommonResult commonResult = adminService.deleteAdmin(3L);

            // Verifying the result
            Assertions.assertEquals(Errors.SUCCESS.getCode(), commonResult.getCode());
        }
    }


    @Test
    public void deleteAdminShouldReturnCANNOT_DELETE(){
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getMockUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(userRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(mockUser));

            GenericException exception = Assertions.assertThrows(GenericException.class, () ->
                    adminService.deleteAdmin(3L)
            );

            Assertions.assertEquals(Errors.CANNOT_DELETE, exception.getError());
        }
    }

}
