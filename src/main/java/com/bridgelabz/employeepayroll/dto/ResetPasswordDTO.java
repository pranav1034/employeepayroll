package com.bridgelabz.employeepayroll.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String email;

    private String password;
}
