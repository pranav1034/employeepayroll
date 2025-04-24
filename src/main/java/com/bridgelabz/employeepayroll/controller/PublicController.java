package com.bridgelabz.employeepayroll.controller;

import com.bridgelabz.employeepayroll.dto.*;
//import com.bridgelabz.employeepayroll.model.PasswordReset;
import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.repository.UserRepository;
import com.bridgelabz.employeepayroll.service.MailService;
import com.bridgelabz.employeepayroll.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MailService mailService;


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            ResponseDTO response = new ResponseDTO("user already exists", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        mailService.sendMail(request.getEmail(), "Registration Successful", "Welcome to the Employee Payroll System!");

        ResponseDTO response = new ResponseDTO("User registered successfully", HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            e.printStackTrace();
            ResponseDTO response = new ResponseDTO("Invalid Credentials", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String token = jwtUtility.generateToken(userDetails.getUsername());

        ResponseDTO response = new ResponseDTO(token,HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody ForgotPasswordDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            ResponseDTO response = new ResponseDTO("User not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        String otp = String.valueOf(Math.round(Math.random() * 9000 + 1000));
        user.setOtp(otp);
        userRepository.save(user);

        mailService.sendMail(request.getEmail(),"Password Reset OTP", "Your OTP is: " + otp);
        ResponseDTO response = new ResponseDTO("OTP sent to your email", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO request){
        User user = userRepository.findByOtp(request.getOtp()).orElse(null);
        if (user == null) {
            ResponseDTO response = new ResponseDTO("User not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!user.getOtp().equals(request.getOtp())) {
            ResponseDTO response = new ResponseDTO("Invalid OTP", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        userRepository.save(user);

        mailService.sendMail(user.getEmail(),"Password Reset Successful", "Your password has been reset successfully!");

        ResponseDTO response = new ResponseDTO("Password reset successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
