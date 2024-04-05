package com.xzh.empmanagement_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xzh.empmanagement_back.common.ErrorCode;
import com.xzh.empmanagement_back.exception.BusinessException;
import com.xzh.empmanagement_back.mapper.EmployeeMapper;
import com.xzh.empmanagement_back.model.domain.Employee;
import com.xzh.empmanagement_back.service.EmployeeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @description 针对表【employee(员工表)】的数据库操作Service实现
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    @Override
    public void validEmployee(Employee employee, boolean add) {
        if (employee == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long departmentId = employee.getDepartmentId();
        String employeeName = employee.getEmployeeName();
        String companyCode = employee.getCompanyCode();
        String employeeAvatar = employee.getEmployeeAvatar();
        Integer gender = employee.getGender();
        String employeePosition = employee.getEmployeePosition();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(String.valueOf(departmentId), employeeName, companyCode, employeeAvatar, employeePosition) || ObjectUtils.anyNull(gender)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }
}




