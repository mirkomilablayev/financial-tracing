package com.finanacialtracing.financialtracingapp.dto.auth;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @Size(min = 5, message = "Username length must be at least 5")
    private String username;
    private String password;
    private String fullName;
}
