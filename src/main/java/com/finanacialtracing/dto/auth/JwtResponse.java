package com.finanacialtracing.dto.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Setter
@Getter
public class JwtResponse {
    private String jwt;

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }
}
