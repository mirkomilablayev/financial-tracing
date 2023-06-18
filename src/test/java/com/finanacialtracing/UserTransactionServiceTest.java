package com.finanacialtracing;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.transaction.TransactionUpdateDto;
import com.finanacialtracing.dto.transaction.personal.PersonalTranCreateDto;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeDTO;
import com.finanacialtracing.dto.transactiontype.PersonalTransactionTypeUpdateDTO;
import com.finanacialtracing.entity.Transaction;
import com.finanacialtracing.entity.TransactionType;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.UserRole;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.exception.NotFoundException;
import com.finanacialtracing.repository.TransactionRepository;
import com.finanacialtracing.repository.TransactionTypeRepository;
import com.finanacialtracing.service.UserTransactionService;
import com.finanacialtracing.util.constants.AuthorizationConstants;
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
public class UserTransactionServiceTest {

    public static final int EXPECTED_SIZE_OF_TRANSACTION_TYPE_LIST = 1;
    @InjectMocks
    private UserTransactionService userTransactionService;
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

    @Test
    public void createTransactionTypeShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);
            Mockito.when(transactionTypeRepository.save(ArgumentMatchers.any(TransactionType.class)))
                    .thenReturn(transactionType);
            Assertions.assertEquals(Errors.SUCCESS.getMessage(), userTransactionService.createTransactionType(new PersonalTransactionTypeDTO("name")).getMessage());
        }
    }

    @Test
    public void updateTransactionTypeShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);
            Mockito.when(transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(getTransactionType(mockUser)));
            Mockito.when(transactionTypeRepository.save(ArgumentMatchers.any(TransactionType.class)))
                    .thenReturn(transactionType);
            Assertions.assertEquals(Errors.SUCCESS.getMessage(), userTransactionService.updateTransactionType(new PersonalTransactionTypeUpdateDTO(1L, "new name")).getMessage());
        }
    }

    @Test
    public void updateTransactionTypeShouldReturnCANNOT_UPDATE() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);
            transactionType.setUserId(34L);
            Mockito.when(transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(transactionType));
            GenericException exception = Assertions.assertThrows(GenericException.class, () ->
                    userTransactionService.updateTransactionType(new PersonalTransactionTypeUpdateDTO(1L, "new name"))
            );
            Assertions.assertEquals(Errors.CANNOT_UPDATE, exception.getError());
        }
    }

    @Test
    public void deleteTransactionTypeShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);
            Mockito.when(transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(transactionType));
            Mockito.when(transactionRepository.getTransactions(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(new ArrayList<>());
            Mockito.when(transactionTypeRepository.save(ArgumentMatchers.any(TransactionType.class)))
                    .thenReturn(transactionType);
            Assertions.assertEquals(Errors.SUCCESS.getMessage(), userTransactionService.deleteTransactionType(1L).getMessage());
        }
    }

    @Test
    public void deleteTransactionTypeShouldReturnCANNOT_DELETE() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);
            transactionType.setUserId(78L);
            Mockito.when(transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(transactionType));
            GenericException exception = Assertions.assertThrows(GenericException.class, () ->
                    userTransactionService.deleteTransactionType(1L)
            );
            Assertions.assertEquals(Errors.CANNOT_DELETE.getMessage(), exception.getError().getMessage());
        }
    }

    @Test
    public void getPersonalTransactionTypesShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);
            transactionType.setUserId(78L);
            Mockito.when(transactionTypeRepository.findAllByUserIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(new ArrayList<>(List.of(getTransactionType(getMockUser()))));
            CommonResult personalTransactionTypes = userTransactionService.getPersonalTransactionTypes();
            List<TransactionType> result = new ObjectMapper().convertValue(personalTransactionTypes.getData(), new TypeReference<List<TransactionType>>() {
            });


            Assertions.assertEquals(EXPECTED_SIZE_OF_TRANSACTION_TYPE_LIST, result.size());

        }
    }

    @Test
    public void createTransactionShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);

            Mockito.when(transactionTypeRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(transactionType));

            Mockito.when(transactionRepository.save(ArgumentMatchers.any(Transaction.class)))
                    .thenReturn(getTransaction());

            Assertions.assertEquals(Errors.SUCCESS.getCode(), userTransactionService.createTransaction(new PersonalTranCreateDto(12D, "", Boolean.TRUE, 1L)).getCode());
        }
    }

    @Test
    public void updateTransactionShouldReturnSUCCESS() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            TransactionType transactionType = getTransactionType(mockUser);

            Mockito.when(transactionRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(getTransaction()));

            Mockito.when(transactionRepository.save(ArgumentMatchers.any(Transaction.class)))
                    .thenReturn(getTransaction());

            Assertions.assertEquals(Errors.SUCCESS.getCode(), userTransactionService.updateTransaction(new TransactionUpdateDto(1L, 12D, "", Boolean.TRUE, getTransactionType(mockUser).getId())).getCode());
        }
    }

    @Test
    public void updateTransactionShouldReturnNOT_FOUND() {
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Mockito.when(transactionRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(getTransaction()));
            NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> userTransactionService.updateTransaction(new TransactionUpdateDto(1L, 12D, "", Boolean.TRUE, 12221L)));
            Assertions.assertEquals(Errors.NOT_FOUND, notFoundException.getError());
        }
    }

    @Test
    public void deleteTransactionShouldReturnSUCCESS(){
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Transaction transaction = getTransaction();
            transaction.setUserId(mockUser.getId());
            Mockito.when(transactionRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(transaction));
            Mockito.when(transactionRepository.save(ArgumentMatchers.any(Transaction.class)))
                    .thenReturn(transaction);
            Assertions.assertEquals(Errors.SUCCESS.getCode(), userTransactionService.deletedTransaction(1L).getCode());
        }
    }

    @Test
    public void deleteTransactionShouldReturnCANNOT_DELETE(){
        try (MockedStatic<SecurityUtils> utilities = BDDMockito.mockStatic(SecurityUtils.class)) {
            User mockUser = getTestUser();
            utilities.when(SecurityUtils::getCurrentUser).thenReturn(mockUser);
            Transaction transaction = getTransaction();
            Mockito.when(transactionRepository.findByIdAndIsDeletedAndIsPersonal(ArgumentMatchers.anyLong(), ArgumentMatchers.anyBoolean(), ArgumentMatchers.anyBoolean()))
                    .thenReturn(Optional.of(transaction));
            GenericException genericException = Assertions.assertThrows(GenericException.class, () -> userTransactionService.deletedTransaction(1L));
            Assertions.assertEquals(Errors.CANNOT_DELETE, genericException.getError());
        }
    }
}
