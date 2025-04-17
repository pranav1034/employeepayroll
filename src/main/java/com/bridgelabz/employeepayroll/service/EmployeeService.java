package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.model.Employee;

import java.util.List;

public interface EmployeeService {

    abstract Employee addEmployee(Employee employee);

    abstract List<Employee> getAllEmployees();

    abstract Employee getEmployeeById(Long id);

    abstract Employee updateEmployee(Long id, Employee employee);

    abstract void deleteEmployee(Long id);

}
