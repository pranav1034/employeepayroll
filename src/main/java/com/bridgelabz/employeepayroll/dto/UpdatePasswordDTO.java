package com.bridgelabz.employeepayroll.dto;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
    private String email;

    private String password;
}
