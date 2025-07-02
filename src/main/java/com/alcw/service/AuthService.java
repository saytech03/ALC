package com.alcw.service;


import com.alcw.dto.LoginRequest;
import com.alcw.dto.PatronLoginRequest;
import com.alcw.dto.UserDTO;
import com.alcw.model.User;

public interface AuthService {
    User registerUser(UserDTO userDTO);
    User verifyOTP(String email, String otp);
    User loginUser(LoginRequest loginRequest);
    User loginWithPatronId(PatronLoginRequest request);
}
