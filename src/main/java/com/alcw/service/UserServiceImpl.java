package com.alcw.service;




import com.alcw.dto.UpdateProfileDTO;
import com.alcw.exception.DuplicateEmailException;
import com.alcw.exception.InvalidCredentialsException;
import com.alcw.model.User;
import com.alcw.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final EmailService emailService;
    private final Cloudinary cloudinary;


    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }

        if (!isPasswordValid(user.getPassword())) {
            throw new InvalidCredentialsException(
                    "Password must contain 8+ characters, 1 uppercase, and 1 special character"
            );
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        String otp = otpService.generateOTP(user.getEmail());
        emailService.sendOTPEmail(user.getEmail(), user.getName(), otp);

        return savedUser;
    }

    @Override
    public User verifyOTP(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        otpService.validateOTP(email, otp);
        user.setVerified(true);
        user.setMembershipId("ALCWB" + user.getId().substring(0, 4).toUpperCase());

        User updatedUser = userRepository.save(user);
        emailService.sendWelcomeEmail(updatedUser);

        return updatedUser;
    }

    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if (!user.isVerified()) {
            throw new InvalidCredentialsException("Account not verified");
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$");
    }

    @Override
    public User updateProfile(String email, UpdateProfileDTO updateDto, MultipartFile image) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // Validate password if present
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            if (!updateDto.getPassword().matches("^(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")) {
                throw new InvalidCredentialsException(
                        "Password must contain 8+ characters, 1 uppercase, and 1 special character");
            }
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }

        // Update other fields
        if (updateDto.getName() != null && !updateDto.getName().isEmpty()) {
            user.setName(updateDto.getName());
        }

        if (updateDto.getOccupation() != null && !updateDto.getOccupation().isEmpty()) {
            user.setOccupation(User.Occupation.valueOf(updateDto.getOccupation()));
        }

        // Handle image upload
        if (image != null && !image.isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.asMap("folder", "alc_profiles")
                );
                user.setProfileImageUrl((String) uploadResult.get("secure_url"));
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return userRepository.save(user);
    }
}
