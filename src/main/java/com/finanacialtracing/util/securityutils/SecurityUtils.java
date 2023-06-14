package com.finanacialtracing.util.securityutils;

import com.finanacialtracing.entity.User;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static User getCurrentUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null || principal.getId() == null) {
            throw new GenericException(Errors.FORBIDDEN);
        }
        return principal;
    }

}
