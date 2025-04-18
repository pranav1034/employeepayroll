package com.bridgelabz.employeepayroll.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;


public @Data class EmployeeDTO {

    @Pattern(regexp = "^[A-Z]{1}[A-Za-z\\s]{2,}$",message = "Employee name invalid")
    private String name;

    @Min(value = 0,message = "Salary cannot be less than 0")
    private double salary;

    @Pattern(regexp = "male|female",message = "Gender needs to be male or female")
    private String gender;

    @JsonFormat(pattern = "dd MMM yyyy")
    @NotNull(message = "Start Date cannot be null")
    @PastOrPresent(message = "Start date should be past or todays date")
    private LocalDate startDate;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String name, double salary,String gender, LocalDate startDate) {
        this.name = name;
        this.salary = salary;
        this.gender=gender;
        this.startDate = startDate;
    }
}
