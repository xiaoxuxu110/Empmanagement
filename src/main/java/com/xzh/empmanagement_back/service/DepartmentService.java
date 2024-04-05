package com.xzh.empmanagement_back.service;

import com.xzh.empmanagement_back.model.domain.Department;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 12702
* @description 针对表【department(部门表)】的数据库操作Service
* @createDate 2024-03-29 01:07:58
*/
public interface DepartmentService extends IService<Department> {
    void validDept(Department department, boolean add);
}
