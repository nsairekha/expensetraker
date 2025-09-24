package com.expensemanager.expense_backend.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensemanager.expense_backend.model.PasswordResetToken;
import com.expensemanager.expense_backend.model.User;
import com.expensemanager.expense_backend.model.UserLoginRequest;
import com.expensemanager.expense_backend.model.UserLoginResponse;
import com.expensemanager.expense_backend.model.UserRegisterRequest;
import com.expensemanager.expense_backend.repository.PasswordResetTokenRepo;
import com.expensemanager.expense_backend.repository.UserRepository;
import com.expensemanager.expense_backend.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PasswordResetTokenRepo resetTokenRepo;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private JwtUtil jwtUtil;

    public String registerUser(UserRegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
        return "User registered successfully!";
    }
    
    public UserLoginResponse loginUser(UserLoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        // Hide password before returning
        user.setPassword(null);

        return new UserLoginResponse(token, "Login successful", user);
    }


    public void initiatePasswordReset(String email) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hour
        resetTokenRepo.save(resetToken);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        // 🔁 Send using HTML template
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("resetLink", resetLink);
        variables.put("token", token);  

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset Request",
                "password-reset",
                variables
        );
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokenRepo.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        resetTokenRepo.delete(resetToken); // Invalidate token
    }
}
