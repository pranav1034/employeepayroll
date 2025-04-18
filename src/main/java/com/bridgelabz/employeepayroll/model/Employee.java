package com.bridgelabz.employeepayroll.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
public @Data class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String name;
    private double salary;
    private String gender;
    private LocalDate startDate;

    public Employee() {
    }

    public Employee(Long employeeId, String name, double salary,String gender, LocalDate startDate) {
        this.employeeId = employeeId;
        this.name = name;
        this.salary = salary;
        this.gender = gender;
        this.startDate = startDate;
    }
}
