package com.finanacialtracing.financialtracingapp.controller;

import com.finanacialtracing.financialtracingapp.dto.CommonResult;
import com.finanacialtracing.financialtracingapp.exception.Errors;
import com.finanacialtracing.financialtracingapp.exception.GenericException;
import com.finanacialtracing.financialtracingapp.util.securityutils.JwtUtils;
import com.finanacialtracing.financialtracingapp.dto.auth.JwtResponse;
import com.finanacialtracing.financialtracingapp.dto.auth.LoginRequest;
import com.finanacialtracing.financialtracingapp.dto.auth.RegisterDTO;
import com.finanacialtracing.financialtracingapp.entity.User;
import com.finanacialtracing.financialtracingapp.repository.RoleRepository;
import com.finanacialtracing.financialtracingapp.repository.UserRepository;
import com.finanacialtracing.financialtracingapp.util.constants.AuthorizationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private static final String USERNAME_VALIDATION_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{4,}$";


    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User principal = (User) authenticate.getPrincipal();
        String jwt = jwtUtils.generateToken(principal.getUsername(), principal);
        return new CommonResult(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterDTO registerDto) throws GenericException {
        log.info("Start register method");
        if (registerDto.getUsername() != null && !registerDto.getUsername().matches(USERNAME_VALIDATION_PATTERN)) {
            throw new GenericException(Errors.USERNAME_VALIDATION_ERROR);
        }
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new GenericException(Errors.USERNAME_ALREADY_TAKEN);
        }
        User user = new User();
        user.setPassword(encoder.encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setFullName(registerDto.getFullName());
        user.setRoles(Set.of(roleRepository.findByName(AuthorizationConstants.USER_ROLE)
                .orElseThrow(() -> new GenericException(Errors.USER_ROLE_NOT_FOUND))));
        User save = userRepository.save(user);

        return new CommonResult(save.getUsername() + " Successfully Registered");
    }

}
