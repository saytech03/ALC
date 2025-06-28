package com.alcw.service;


import com.alcw.dto.LoginRequest;
import com.alcw.dto.PatronLoginRequest;
import com.alcw.dto.UserDTO;
import com.alcw.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setOccupation(userDTO.getOccupation());

        return userService.registerUser(user);
    }

    @Override
    public User verifyOTP(String email, String otp) {
        return userService.verifyOTP(email, otp); // Pass through to UserService
    }

    @Override
    public User loginUser(LoginRequest loginRequest) {
        return userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @Override
    public User loginWithPatronId(PatronLoginRequest request) {
        return userService.loginWithPatronId(request.getPatronId(), request.getPassword());
    }
}
