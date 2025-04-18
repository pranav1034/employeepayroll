package com.bridgelabz.employeepayroll.service;

import com.bridgelabz.employeepayroll.dto.EmployeeDTO;
import com.bridgelabz.employeepayroll.dto.ResponseDTO;
import com.bridgelabz.employeepayroll.model.Employee;
import com.bridgelabz.employeepayroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Employee> addEmployee(EmployeeDTO employee) {
        Employee emp = new Employee();
        if(employee.getSalary() < 0 || employee.getName()==null){
            return new ResponseEntity<>(emp, HttpStatus.BAD_REQUEST);
        }
        emp.setName(employee.getName());
        emp.setSalary(employee.getSalary());
        repository.save(emp);
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(Long id){
        Employee emp = repository.findById(id).orElse(null);
        if(emp==null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(emp, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ResponseDTO> updateEmployee(Long id, EmployeeDTO employee){
        Employee emp = repository.findById(id).orElse(null);
        if(emp!=null) {
            emp.setName(employee.getName());
            emp.setSalary(employee.getSalary());
            repository.save(emp);
            ResponseDTO response = new ResponseDTO("employee updated successfully", emp);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ResponseDTO response= new ResponseDTO("employee not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> deleteEmployee(Long id){
        Employee emp = repository.findById(id).orElse(null);
        if(emp!=null){
            repository.deleteById(id);
            return new ResponseEntity<>("Employee with ID " + id + " deleted successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("employee Id not found", HttpStatus.NOT_FOUND);
    }
}
