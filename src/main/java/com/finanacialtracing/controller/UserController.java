package com.finanacialtracing.controller;

import com.finanacialtracing.dto.CommonResult;
import com.finanacialtracing.dto.admin.ChangePasswordDto;
import com.finanacialtracing.dto.auth.JwtResponse;
import com.finanacialtracing.dto.auth.LoginRequest;
import com.finanacialtracing.dto.auth.RegisterDTO;
import com.finanacialtracing.entity.User;
import com.finanacialtracing.exception.Errors;
import com.finanacialtracing.exception.GenericException;
import com.finanacialtracing.exception.NotFoundException;
import com.finanacialtracing.service.UserService;
import com.finanacialtracing.util.securityutils.JwtUtils;
import com.finanacialtracing.repository.RoleRepository;
import com.finanacialtracing.repository.UserRepository;
import com.finanacialtracing.util.constants.AuthorizationConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public static final String USERNAME_VALIDATION_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]{4,}$";

    private final UserService userService;

    /**
     *  You can login here
     * @param loginRequest there are two fields username and password in this class
     * @return CommonResult
     */
    @PostMapping("/login")
    public CommonResult login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        User principal = (User) authenticate.getPrincipal();
        String jwt = jwtUtils.generateToken(principal.getUsername(), principal);
        return new CommonResult(new JwtResponse(jwt));
    }

    /**
     * You can register here
     * @param registerDto
     * @return
     * @throws GenericException
     */
    @PostMapping("/register")
    public CommonResult register(@RequestBody RegisterDTO registerDto) throws GenericException {
        log.info("Start register method");
        if (registerDto.getUsername() != null && !registerDto.getUsername().matches(USERNAME_VALIDATION_PATTERN)) {
            throw new GenericException(Errors.USERNAME_VALIDATION_ERROR);
        }
        if (userRepository.existsByUsernameAndIsDeleted(registerDto.getUsername(), Boolean.FALSE)) {
            throw new GenericException(Errors.USERNAME_ALREADY_TAKEN);
        }
        User user = new User();
        user.setPassword(encoder.encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setFullName(registerDto.getFullName());
        user.setRoles(Set.of(roleRepository.findByName(AuthorizationConstants.USER_ROLE)
                .orElseThrow(() -> new NotFoundException(Errors.NOT_FOUND))));
        User save = userRepository.save(user);

        return new CommonResult(save.getUsername() + " Successfully Registered");
    }


    /**
     * You can change your username here
     * @param newUsername
     * @return
     */
    @PutMapping("/change-username")
    public CommonResult changeUsername(@RequestParam String newUsername){
        log.info("Rest request to changeUsername()");
        return userService.changeUsername(newUsername);
    }

    /**
     * you can change your password here
     * @param changePasswordDto
     * @return
     */
    @PutMapping("/change-password")
    public CommonResult changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        log.info("Rest request to changePassword()");
        return userService.changePassword(changePasswordDto);
    }

    /**
     * you can delete your account here
     * @return
     */
    @DeleteMapping("/delete-account")
    public CommonResult deleteUser(){
        log.info("Rest request to deleteUser()");
        return userService.deleteUser();
    }

    /**
     * you can edit your full name here
     * @param newFullName
     * @return
     */
    @PutMapping("/change-full-name")
    public CommonResult editFullName(@RequestParam String newFullName){
        log.info("Rest request to editFullName()");
        return userService.editFullName(newFullName);
    }

}
