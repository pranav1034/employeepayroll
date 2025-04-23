package com.bridgelabz.employeepayroll.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    private String token;

    @Column(nullable=true)
    private String email;

    public Collection<?extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Implement this method to return the user's authorities
    }

    public String getUsername() {
        return email;
    }


}
