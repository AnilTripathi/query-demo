package com.query.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.query.demo.entity.Employee;
import com.query.demo.exception.EmployeeNotFoundException;
import com.query.demo.services.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employee")
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Search employees by firstName and department")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the employees",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) }),
        @ApiResponse(responseCode = "404", description = "No employees found")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @Parameter(description = "First name to search for") @RequestParam(required = false) String firstName,
            @Parameter(description = "Department to search for") @RequestParam(required = false) String department) {
        List<Employee> employees = employeeService.findEmployeesWithCustomMatcher(firstName, department);
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Find employees by example")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the employees",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) })
    })
    @PostMapping("/search/example")
    public List<Employee> findByExample(@RequestBody @Valid Employee employee) {
        return employeeService.findEmployeesByExample(employee);
    }

    @Operation(summary = "Find one employee by example")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the employee",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class)) }),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PostMapping("/search/example/one")
    public Employee findOneByExample(@RequestBody @Valid Employee employee) {
        return employeeService.findOneEmployeeByExample(employee)
                .orElseThrow(() -> new EmployeeNotFoundException("No employee found matching the example"));
    }

    @Operation(summary = "Count employees matching the example")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns the count of matching employees",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)) })
    })
    @PostMapping("/count")
    public long countByExample(@RequestBody @Valid Employee employee) {
        return employeeService.countEmployeesByExample(employee);
    }

    @Operation(summary = "Check if any employee matches the example")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Returns whether matching employees exist",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)) })
    })
    @PostMapping("/exists")
    public boolean existsByExample(@RequestBody @Valid Employee employee) {
        return employeeService.existsByExample(employee);
    }
}
