package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.Employee;
import com.bridgelabz.employeepayroll.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository repository;

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return repository.findAll();
    }

    @Override
    public ResponseEntity<Employee> addEmployee(EmployeeDTO employee) {
        Employee emp = new Employee();
        if(employee.getSalary() < 0 || employee.getName()==null){
            log.error("Failed to add employee");
            return new ResponseEntity<>(emp, HttpStatus.BAD_REQUEST);
        }
        emp.setName(employee.getName());
        emp.setSalary(employee.getSalary());
        emp.setGender(employee.getGender());
        emp.setStartDate(employee.getStartDate());
        repository.save(emp);
        log.info("Employee added successfully");
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(Long id){
        Employee emp = repository.findById(id).orElse(null);
        if(emp==null){
            log.error("Employee with ID " + id + " not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        log.info("Employee with ID " + id + " found");
        return new ResponseEntity<>(emp, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseDTO> updateEmployee(Long id, EmployeeDTO employee){
        Employee emp = repository.findById(id).orElse(null);
        if(emp!=null) {
            emp.setName(employee.getName());
            emp.setSalary(employee.getSalary());
            emp.setGender(employee.getGender());
            emp.setStartDate(employee.getStartDate());
            repository.save(emp);
            ResponseDTO response = new ResponseDTO("employee updated successfully", emp);
            log.info("Employee with ID " + id + " updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ResponseDTO response= new ResponseDTO("employee not found", HttpStatus.NOT_FOUND);
        log.info("Employee with ID " + id + " not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> deleteEmployee(Long id){
        Employee emp = repository.findById(id).orElse(null);
        if(emp!=null){
            repository.deleteById(id);
            log.info("Employee with ID " + id + " deleted successfully");
            return new ResponseEntity<>("Employee with ID " + id + " deleted successfully.", HttpStatus.OK);
        }
        log.info("Employee with ID " + id + " not found");
        return new ResponseEntity<>("employee Id not found", HttpStatus.NOT_FOUND);
    }
}

