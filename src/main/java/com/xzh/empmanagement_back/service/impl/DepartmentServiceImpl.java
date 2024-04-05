package com.xzh.empmanagement_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzh.empmanagement_back.common.ErrorCode;
import com.xzh.empmanagement_back.exception.BusinessException;
import com.xzh.empmanagement_back.mapper.DepartmentMapper;
import com.xzh.empmanagement_back.model.domain.Department;
import com.xzh.empmanagement_back.service.DepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @description 针对表【department(部门表)】的数据库操作Service实现
*/
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department>
    implements DepartmentService {

    @Override
    public void validDept(Department department, boolean add) {
        if (department == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String name = department.getDepartmentName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "部门名称不能为空");
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }
}




