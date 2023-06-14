package com.finanacialtracing.repository;

import com.finanacialtracing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FinancialOperationRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserIdAndIsDeletedAndIsPersonal(Long userId, Boolean isDeleted, Boolean isPersonal);

    List<Transaction> findAllByUserIdAndIsDeletedAndTypeId(Long userId, Boolean isDeleted, Long typeId);

    Boolean existsByTypeIdAndIsDeletedAndIsPersonal(Long typeId, Boolean isDeleted, Boolean isPersonal);

    @Query(value = "select * from financial_operations where  user_id = :user_id and\n" +
            "                                          type_id = :type_id and\n" +
            "                                          is_deleted = :is_deleted and\n" +
            "                                          is_personal = :is_personal ", nativeQuery = true)
    List<Transaction> getFinancialOperations(@Param("user_id") Long user_id,
                                             @Param("type_id") Long type_id,
                                             @Param("is_deleted") Boolean is_deleted,
                                             @Param("is_personal") Boolean is_personal);

    Optional<Transaction> findByIdAndIsDeletedAndIsPersonal(Long id, Boolean isDeleted, Boolean isPersonal);

    @Query(value = "SELECT *\n" +
            "FROM financial_operations fo\n" +
            "WHERE (NOT :sortedByAmount OR (:sortedByAmount AND fo.amount  BETWEEN :minAmount AND :maxAmount))\n" +
            "  AND (NOT :sortedByFoType OR (:sortedByFoType AND fo.type_id = :foTypeId))\n" +
            "  AND fo.user_id = :userId\n" +
            "ORDER BY id ASC\n" +
            "LIMIT :size OFFSET (:page - 1);\n", nativeQuery = true)
    List<Transaction> getPersonalFinancialOperationsList(
            @Param("sortedByAmount") Boolean sortedByAmount,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("sortedByFoType") Boolean sortedByFoType,
            @Param("foTypeId") Long foTypeId,
            @Param("userId") Long userId,
            @Param("size") Integer size,
            @Param("page") Integer page
    );


    @Query(value = "SELECT *\n" +
            "FROM financial_operations fo\n" +
            "WHERE (NOT :sortedByAmount OR (:sortedByAmount AND fo.amount BETWEEN :minAmount AND :maxAmount))\n" +
            "  AND (NOT :sortedByFoType OR (:sortedByFoType AND fo.type_id = :foTypeId))\n" +
            "  AND fo.org_id = :orgId and is_deleted = true\n" +
            "ORDER BY id ASC\n" +
            "LIMIT :size OFFSET (:page - 1);", nativeQuery = true)
    List<Transaction> getOrganizationFinancialOperationsList(
            @Param("sortedByAmount") Boolean sortedByAmount,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("sortedByFoType") Boolean sortedByFoType,
            @Param("foTypeId") Long foTypeId,
            @Param("orgId") Long orgId,
            @Param("size") Integer size,
            @Param("page") Integer page);

}
