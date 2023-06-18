package com.finanacialtracing;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.organization.OrganizationCreateDto;
import com.finanacialtracing.dto.organization.OrganizationUpdateDto;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCreateDto;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeDTO;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeUpdateDTO;
import com.finanacialtracing.entity.*;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.exception.NotFoundException;
import com.finanacialtracing.repository.*;
import com.finanacialtracing.service.OrganizationTransactionService;
import com.finanacialtracing.service.UserTransactionService;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import com.finanacialtracing.util.constants.WorkerPermissionConstants;
import com.finanacialtracing.util.securityutils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class OrganizationTransactionServiceTest {

    public static final int GET_MY_ORGANIZATIONS_LIST_SIZE = 1;
    @InjectMocks
    private OrganizationTransactionService organizationTransactionService;
    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private WorkerPositionRepository workerPositionRepository;
    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkerPermissionRepository workerPermissionRepository;
    @Mock
    private TransactionTypeRepository transactionTypeRepository;
    @Mock
    private TransactionRepository transactionRepository;

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

    private TransactionType getTransactionType(User user) {
        TransactionType transactionType = new TransactionType();
        transactionType.setId(1L);
        transactionType.setUserId(user.getId());
        transactionType.setName("name");
        transactionType.setIsPersonal(Boolean.TRUE);
        transactionType.setIsDeleted(Boolean.FALSE);
        return transactionType;
    }

    private Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAmount(11111D);
        transaction.setDescription("This is description");
        transaction.setTypeId(getTransactionType(getMockUser()).getId());
        transaction.setUserId(getMockUser().getId());
        transaction.setIsIncome(Boolean.TRUE);
        transaction.setIsPersonal(Boolean.TRUE);
        transaction.setIsDeleted(Boolean.FALSE);
        transaction.setId(1L);
        return transaction;
    }

    private Organization getOrganization(User user) {
        Organization organization = new Organization();
        organization.setOrgName("organization name");
        organization.setId(1L);
        organization.setUserId(user.getId());
        organization.setIsDeleted(Boolean.FALSE);
        return organization;
    }

    private WorkerPosition getWorkerPosition(User user) {
        WorkerPosition workerPosition = new WorkerPosition();
        workerPosition.setIsDeleted(Boolean.FALSE);
        workerPosition.setName("Position name");
        workerPosition.setId(1L);
        workerPosition.setOrgId(getOrganization(user).getId());
        return workerPosition;
    }

    private Worker getWorker(User user) {
        Worker worker = new Worker();
        worker.setUserId(user.getId());
        worker.setPositionId(getWorkerPosition(user).getId());
        worker.setAddedBy(12L);
        worker.setPermissions(new HashSet<>(Set.of(
                new WorkerPermission(1L, WorkerPermissionConstants.can_edit_transaction, Boolean.FALSE),
                new WorkerPermission(2L, WorkerPermissionConstants.can_edit_transaction, Boolean.FALSE),
                new WorkerPermission(3L, WorkerPermissionConstants.can_edit_transaction, Boolean.FALSE),
                new WorkerPermission(4L, WorkerPermissionConstants.can_edit_transaction, Boolean.FALSE)
        )));
        worker.setIsDeleted(Boolean.FALSE);
        worker.setOrgId(getOrganization(user).getId());
        return worker;
    }

    @Test
    public void createOrganizationShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Mockito.when(organizationRepository.save(ArgumentMatchers.any(Organization.class)))
                    .thenReturn(getOrganization(mockUser));
            Assertions.assertEquals(Errors.SUCCESS.getCode(), organizationTransactionService.createOrganization(new OrganizationCreateDto("This is org name")).getCode());
        }
    }

    @Test
    public void updateOrganizationShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Mockito.when(organizationRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(getOrganization(mockUser)));
            Mockito.when(organizationRepository.save(ArgumentMatchers.any(Organization.class)))
                    .thenReturn(getOrganization(mockUser));
            Assertions.assertEquals(Errors.SUCCESS.getCode(), organizationTransactionService.updateOrganization(new OrganizationUpdateDto(1L, "new org name")).getCode());
        }
    }

    @Test
    public void updateOrganizationShouldReturnCANNOT_UPDATE() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Organization organization = getOrganization(mockUser);
            organization.setUserId(12L);
            Mockito.when(organizationRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(organization));

            GenericException genericException = Assertions.assertThrows(GenericException.class, () -> organizationTransactionService.updateOrganization(new OrganizationUpdateDto(1L, "new org name")));
            Assertions.assertEquals(Errors.CANNOT_UPDATE, genericException.getError());
        }
    }

    @Test
    public void deleteOrganizationShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Mockito.when(organizationRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(getOrganization(mockUser)));
            Mockito.when(organizationRepository.deleteOrganization(ArgumentMatchers.anyLong()))
                    .thenReturn(1);
            Assertions.assertEquals(Errors.SUCCESS.getCode(), organizationTransactionService.deleteOrganization(1L).getCode());
        }
    }

    @Test
    public void deleteOrganizationShouldReturnCANNOT_DELETE() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Organization organization = getOrganization(mockUser);
            organization.setUserId(12L);
            Mockito.when(organizationRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(organization));
            GenericException genericException = Assertions.assertThrows(GenericException.class, () -> organizationTransactionService.deleteOrganization(1L));
            Assertions.assertEquals(Errors.CANNOT_DELETE, genericException.getError());
        }
    }

    @Test
    public void getOrganizationIAmWorkingShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(workerRepository.findAllByUserIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(new ArrayList<>(List.of(
                            getWorker(mockUser)
                    )));
            Mockito.when(organizationRepository.findByIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(getOrganization(mockUser)));

            Assertions.assertEquals(Errors.SUCCESS.getCode(),  organizationTransactionService.getOrganizationIAmWorking().getCode());
        }
    }

    @Test
    public void getMyOrganizationsShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);

            Mockito.when(organizationRepository.findAllByUserIdAndIsDeleted(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(new ArrayList<>(List.of(getOrganization(mockUser))));

            CommonResult myOrganizations = organizationTransactionService.getMyOrganizations();
            List<TransactionType> result = new ObjectMapper().convertValue(myOrganizations.getData(), new TypeReference<List<TransactionType>>() {
            });


            Assertions.assertEquals(GET_MY_ORGANIZATIONS_LIST_SIZE, result.size());
        }
    }


}
