package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.model.Employee;
import com.bridgelabz.employeepayroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository repository;

    @Override
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public Employee addEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id){
        return repository.findById(id).orElse(null);
    }

    public Employee updateEmployee(Long id, Employee employee){
        Employee emp = repository.findById(id).orElse(null);
        if(emp!=null){
            emp.setName(employee.getName());
            emp.setSalary(employee.getSalary());
        }
        if(emp==null){
            return null;
        }
       return repository.save(emp);
    }

    public void deleteEmployee(Long id){
        repository.deleteById(id);
    }
}
