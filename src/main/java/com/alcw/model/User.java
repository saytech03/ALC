package com.alcw.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Occupation occupation;
    private String membershipId;
    private boolean verified = false;
    private String profileImageUrl;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;


    public enum Occupation {
        STUDENT, LEGAL_PROFESSIONAL, ART_PROFESSIONAL, ARTIST, OTHER
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
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
        return verified;
    }
}

