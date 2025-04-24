package com.bridgelabz.employeepayroll.repository;

import com.bridgelabz.employeepayroll.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User ,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByOtp(String otp);


}
