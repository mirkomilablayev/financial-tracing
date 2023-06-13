package com.finanacialtracing.financialtracingapp.configurations;


import com.finanacialtracing.financialtracingapp.entity.User;
import com.finanacialtracing.financialtracingapp.entity.WorkerPermission;
import com.finanacialtracing.financialtracingapp.repository.RoleRepository;
import com.finanacialtracing.financialtracingapp.repository.UserRepository;
import com.finanacialtracing.financialtracingapp.repository.WorkerPermissionRepository;
import com.finanacialtracing.financialtracingapp.util.constants.AuthorizationConstants;
import com.finanacialtracing.financialtracingapp.entity.UserRole;
import com.finanacialtracing.financialtracingapp.util.constants.WorkerPermissionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WorkerPermissionRepository workerPermissionRepository;
    private final PasswordEncoder passwordEncoder;


    @Value(value = "${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) throws Exception {
        if (ddl.equalsIgnoreCase("create") || ddl.equalsIgnoreCase("create-drop")) {
            roleRepository.save(new UserRole(AuthorizationConstants.USER_ROLE));
            roleRepository.save(new UserRole(AuthorizationConstants.SUPER_ADMIN));
            UserRole admin_role = roleRepository.save(new UserRole(AuthorizationConstants.ADMIN_ROLE));

            User user = new User();
            user.setFullName("administrator");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin_1212"));
            user.setRoles(new HashSet<>(List.of(roleRepository.findByName(AuthorizationConstants.ADMIN_ROLE).orElse(admin_role))));
            userRepository.save(user);

            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_add_fo));
            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_edit_fo));
            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_delete_fo));
            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_see_fo));


        }
    }
}
