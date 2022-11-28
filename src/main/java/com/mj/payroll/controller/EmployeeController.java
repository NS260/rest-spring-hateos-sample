package com.mj.payroll.controller;

import com.mj.payroll.model.assembler.EmployeeModelAssembler;
import com.mj.payroll.exception.EmployeeNotFoundException;
import com.mj.payroll.model.Employee;
import com.mj.payroll.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> getAllEmployees() {

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> addNewEmployee(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = assembler.toModel(repository.save(employee));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> editEmployeeById(@RequestBody Employee employee, @PathVariable Long id) {
        Employee editedEmployee = repository.findById(id)
                .map(user -> {
                    user.setName(employee.getName());
                    user.setRole(employee.getRole());
                    return repository.save(user);
                }).orElseGet(() -> {
                    employee.setId(id);
                    return repository.save(employee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(editedEmployee);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployeeById(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
