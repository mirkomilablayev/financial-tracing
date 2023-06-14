package com.finanacialtracing.repository;

import com.finanacialtracing.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    List<Organization> findAllByUserIdAndIsDeleted(Long userId, Boolean isDeleted);

    @Modifying
    @Query(value = "BEGIN;\n" +
            " \n" +
            " update financial_operations\n" +
            " set is_deleted = true\n" +
            " where is_personal = false\n" +
            "  and org_id = :orgId;\n" +
            " \n" +
            " UPDATE fo_types\n" +
            " set is_deleted = true\n" +
            " where org_id = :orgId;\n" +
            " \n" +
            " UPDATE worker_positions\n" +
            " SET is_deleted = true\n" +
            " WHERE org_id = :orgId;\n" +
            " \n" +
            " update workers\n" +
            " set is_deleted = true\n" +
            " where org_id = :orgId;\n" +
            " \n" +
            " update organizations\n" +
            " set is_deleted = true\n" +
            " where id = :orgId;\n" +
            " \n" +
            " COMMIT;", nativeQuery = true)
    int deleteOrganization(@Param("orgId") Long orgId);

    Boolean existsByIdAndIsDeleted(Long id, Boolean isDeleted);
}
