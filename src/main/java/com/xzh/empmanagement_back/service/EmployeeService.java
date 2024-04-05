package com.xzh.empmanagement_back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xzh.empmanagement_back.model.domain.Employee;

public interface EmployeeService extends IService<Employee> {
    void validEmployee(Employee employee, boolean add);
}
