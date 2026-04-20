package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeResult {

    @JsonProperty("EMP_ID")
    private Integer empId;

    @JsonProperty("FIRST_NAME")
    private String firstName;

    @JsonProperty("LAST_NAME")
    private String lastName;

    @JsonProperty("DEPARTMENT_NAME")
    private String departmentName;

    @JsonProperty("YOUNGER_EMPLOYEES_COUNT")
    private Integer youngerEmployeesCount;

    public EmployeeResult() {
    }

    public EmployeeResult(Integer empId, String firstName, String lastName, String departmentName, Integer youngerEmployeesCount) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentName = departmentName;
        this.youngerEmployeesCount = youngerEmployeesCount;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getYoungerEmployeesCount() {
        return youngerEmployeesCount;
    }

    public void setYoungerEmployeesCount(Integer youngerEmployeesCount) {
        this.youngerEmployeesCount = youngerEmployeesCount;
    }
}

