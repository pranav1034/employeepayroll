package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.LoginDTO;
import com.bridgelabz.employeepayroll.dto.RegisterDTO;
import com.bridgelabz.employeepayroll.dto.ResetPasswordDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        ResponseDTO response = new ResponseDTO("User updated successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        userRepository.delete(user);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody ResetPasswordDTO request){
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        ResponseDTO response = new ResponseDTO("Password updated successfully", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
