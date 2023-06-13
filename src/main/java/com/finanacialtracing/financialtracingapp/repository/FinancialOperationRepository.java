package com.finanacialtracing.financialtracingapp.repository;

import com.finanacialtracing.financialtracingapp.entity.FOType;
import com.finanacialtracing.financialtracingapp.entity.FinancialOperation;
import com.finanacialtracing.financialtracingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FinancialOperationRepository extends JpaRepository<FinancialOperation, Long> {
    List<FinancialOperation> findAllByUserIdAndIsDeletedAndIsPersonal(Long userId, Boolean isDeleted, Boolean isPersonal);

    List<FinancialOperation> findAllByUserIdAndIsDeletedAndTypeId(Long userId, Boolean isDeleted, Long typeId);

    @Query(value = "select * from financial_operations where  user_id = :user_id and\n" +
            "                                          type_id = :type_id and\n" +
            "                                          is_deleted = :is_deleted and\n" +
            "                                          is_personal = :is_personal ", nativeQuery = true)
    List<FinancialOperation> getFinancialOperations(@Param("user_id") Long user_id,
                                                    @Param("type_id") Long type_id,
                                                    @Param("is_deleted") Boolean is_deleted,
                                                    @Param("is_personal") Boolean is_personal);

    Optional<FinancialOperation> findByIdAndIsDeletedAndIsPersonal(Long id, Boolean isDeleted, Boolean isPersonal);

    @Query(value = "SELECT *\n" +
            "FROM financial_operations fo\n" +
            "WHERE (NOT :sortedByAmount OR (:sortedByAmount AND fo.amount  BETWEEN :minAmount AND :maxAmount))\n" +
            "  AND (NOT :sortedByFoType OR (:sortedByFoType AND fo.type_id = :foTypeId))\n" +
            "  AND fo.user_id = :userId\n" +
            "ORDER BY id ASC\n" +
            "LIMIT :size OFFSET (:page - 1);\n", nativeQuery = true)
    List<FinancialOperation> getFinancialOperationsList(
            @Param("sortedByAmount") Boolean sortedByAmount,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            @Param("sortedByFoType") Boolean sortedByFoType,
            @Param("foTypeId") Long foTypeId,
            @Param("userId") Long userId,
            @Param("size") Integer size,
            @Param("page") Integer page
    );

}
