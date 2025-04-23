package com.bridgelabz.employeepayroll.controller;

import com.bridgelabz.employeepayroll.dto.LoginDTO;
import com.bridgelabz.employeepayroll.dto.RegisterDTO;
import com.bridgelabz.employeepayroll.dto.ResetPasswordDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.User;
import com.bridgelabz.employeepayroll.repository.UserRepository;
import com.bridgelabz.employeepayroll.service.UserService;
import com.bridgelabz.employeepayroll.utility.JwtUserDetailsService;
import com.bridgelabz.employeepayroll.utility.JwtUtility;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService service;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody RegisterDTO request) {
        return service.updateUser(id, request);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody ResetPasswordDTO request) {
        return service.updatePassword(request);
    }
}
