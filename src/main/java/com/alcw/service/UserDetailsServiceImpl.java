package com.alcw.service;


import com.alcw.model.Admin;
import com.alcw.model.User;
import com.alcw.repository.AdminRepository;
import com.alcw.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // First try to find a regular user by email
        Optional<User> user = userRepository.findByEmail(usernameOrEmail);
        if (user.isPresent()) {
            if (!user.get().isVerified()) {
                throw new UsernameNotFoundException("User not verified");
            }
            return user.get();
        }

        // If not found, try to find an admin by username
        Optional<Admin> admin = adminRepository.findByUsername(usernameOrEmail);
        if (admin.isPresent()) {
            return admin.get();
        }

        throw new UsernameNotFoundException("User not found with identifier: " + usernameOrEmail);
    }
}

