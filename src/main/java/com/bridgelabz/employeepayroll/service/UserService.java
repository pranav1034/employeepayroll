package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.RegisterDTO;
import com.bridgelabz.employeepayroll.dto.ResetPasswordDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {

    public List<User> getAllUsers();

    public ResponseEntity<User> getUserById(@PathVariable Long id);

    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody RegisterDTO request);

    public ResponseEntity<String> deleteUser(@PathVariable Long id);

    public ResponseEntity<ResponseDTO> updatePassword(@RequestBody ResetPasswordDTO request);


}
