package com.finanacialtracing.configurations;


import com.finanacialtracing.entity.User;
import com.finanacialtracing.entity.WorkerPermission;
import com.finanacialtracing.repository.RoleRepository;
import com.finanacialtracing.repository.UserRepository;
import com.finanacialtracing.repository.WorkerPermissionRepository;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import com.finanacialtracing.entity.UserRole;
import com.finanacialtracing.util.constants.WorkerPermissionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
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
            UserRole save = roleRepository.save(new UserRole(AuthorizationConstants.SUPER_ADMIN));
            roleRepository.save(new UserRole(AuthorizationConstants.ADMIN_ROLE));

            User user = new User();
            user.setFullName("administrator");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin_1212"));
            user.setRoles(new HashSet<>(List.of(roleRepository.findByName(AuthorizationConstants.ADMIN_ROLE).orElse(save))));
            userRepository.save(user);

            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_add_transaction));
            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_edit_transaction));
            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_delete_transaction));
            workerPermissionRepository.save(new WorkerPermission(WorkerPermissionConstants.can_see_transaction));
        }
    }
}
