package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.*;
import com.bridgelabz.employeepayroll.exceptions.UserNotFoundException;
import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.repository.UserRepository;
import com.bridgelabz.employeepayroll.utility.JwtUserDetailsService;
import com.bridgelabz.employeepayroll.utility.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsService userDetailsService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody RegisterDTO request) {
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found in database having id: "+id));
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        ResponseDTO response = new ResponseDTO("User updated successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found in database having id: "+id));
        userRepository.delete(user);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

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



    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody UpdatePasswordDTO request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UserNotFoundException("User not found in database having email: "+request.getEmail()));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        ResponseDTO response = new ResponseDTO("Password updated successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody LoginDTO request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UserNotFoundException("User not found in database having email: "+request.getEmail()));
        String otp = String.valueOf(Math.round(Math.random() * 9000 + 1000));
        user.setOtp(otp);
        userRepository.save(user);

        mailService.sendMail(request.getEmail(),"Password Reset OTP", "Your OTP is: " + otp);
        ResponseDTO response = new ResponseDTO("OTP sent to your email", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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
