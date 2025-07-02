package com.alcw.service;



import com.alcw.dto.UpdateProfileDTO;
import com.alcw.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User registerUser(User user);
    User verifyOTP(String email, String otp);
    User loginUser(String email, String password);
    User getUserByEmail(String email);
    User updateProfile(String email, UpdateProfileDTO updateDto, MultipartFile image);
    User loginWithPatronId(String patronId, String password);
}

