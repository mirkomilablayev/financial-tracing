package com.finanacialtracing.financialtracingapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String fullName;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<UserRole> roles;


    public static User build(User user) {
        Set<UserRole> authorities = user.getRoles().stream()
                .map(role -> new UserRole(role.getId(), role.getName(), role.getIsDeleted()))
                .collect(Collectors.toSet());

        return new User(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.fullName,
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
