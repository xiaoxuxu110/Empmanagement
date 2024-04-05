package com.xzh.empmanagement_back.model.domain.request.employee;

import com.xzh.empmanagement_back.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 用户查询请求
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeQueryRequest extends PageRequest implements Serializable {
    /**
     * 部门 id
     */
    private String department;

    /**
     * 员工名
     */
    private String employeeName;

    /**
     * 公司编号
     */
    private String companyCode;

    /**
     * 员工头像
     */
    private String employeeAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 职位
     */
    private String employeePosition;

    private static final long serialVersionUID = 1L;
}