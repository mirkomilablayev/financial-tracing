package com.finanacialtracing.financialtracingapp.util.securityutils;

import com.finanacialtracing.financialtracingapp.entity.User;
import com.finanacialtracing.financialtracingapp.exception.Errors;
import com.finanacialtracing.financialtracingapp.exception.GenericException;
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
