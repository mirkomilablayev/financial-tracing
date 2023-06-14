package com.finanacialtracing.repository;

import com.finanacialtracing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserIdAndIsDeletedAndIsPersonal(Long userId, Boolean isDeleted, Boolean isPersonal);

    List<Transaction> findAllByUserIdAndIsDeletedAndTypeId(Long userId, Boolean isDeleted, Long typeId);

    Boolean existsByTypeIdAndIsDeletedAndIsPersonal(Long typeId, Boolean isDeleted, Boolean isPersonal);

    @Query(value = "select * from transaction where  user_id = :user_id and\n" +
            "                                          type_id = :type_id and\n" +
            "                                          is_deleted = :is_deleted and\n" +
            "                                          is_personal = :is_personal ", nativeQuery = true)
    List<Transaction> getTransactions(@Param("user_id") Long user_id,
                                      @Param("type_id") Long type_id,
                                      @Param("is_deleted") Boolean is_deleted,
                                      @Param("is_personal") Boolean is_personal);

    Optional<Transaction> findByIdAndIsDeletedAndIsPersonal(Long id, Boolean isDeleted, Boolean isPersonal);

    @Query(value = "SELECT *\n" +
            "FROM transaction tr\n" +
            "WHERE (NOT :sortedByAmount OR (:sortedByAmount AND tr.amount  BETWEEN :minAmount AND :maxAmount))\n" +
            "  AND (NOT :sortedByTransactionType OR (:sortedByTransactionType AND tr.type_id = :transactionTypeId))\n" +
            "  AND tr.user_id = :userId\n" +
            "ORDER BY id ASC\n" +
            "LIMIT :size OFFSET (:page - 1);\n", nativeQuery = true)
    List<Transaction> getPersonalTransactionsList(
            @Param("sortedByAmount") Boolean sortedByAmount,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("sortedByTransactionType") Boolean sortedByTransactionType,
            @Param("transactionTypeId") Long transactionTypeId,
            @Param("userId") Long userId,
            @Param("size") Integer size,
            @Param("page") Integer page
    );


    @Query(value = "SELECT *\n" +
            "FROM transaction tran\n" +
            "WHERE (NOT :sortedByAmount OR (:sortedByAmount AND tran.amount BETWEEN :minAmount AND :maxAmount))\n" +
            "  AND (NOT :sortedByTransactionType OR (:sortedByTransactionType AND tran.type_id = :tranTypeId))\n" +
            "  AND tran.org_id = :orgId and is_deleted = true\n" +
            "ORDER BY id ASC\n" +
            "LIMIT :size OFFSET (:page - 1);", nativeQuery = true)
    List<Transaction> getOrganizationTransactionsList(
            @Param("sortedByAmount") Boolean sortedByAmount,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("sortedByTransactionType") Boolean sortedByTransactionType,
            @Param("tranTypeId") Long tranTypeId,
            @Param("orgId") Long orgId,
            @Param("size") Integer size,
            @Param("page") Integer page);

}
