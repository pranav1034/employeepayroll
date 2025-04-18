package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    abstract ResponseEntity<Employee> addEmployee(EmployeeDTO employee);

    abstract List<Employee> getAllEmployees();

    abstract ResponseEntity<Employee> getEmployeeById(Long id);

    abstract ResponseEntity<ResponseDTO> updateEmployee(Long id, EmployeeDTO employee);

    abstract ResponseEntity<String> deleteEmployee(Long id);

}
