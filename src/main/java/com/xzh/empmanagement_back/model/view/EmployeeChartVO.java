package com.xzh.empmanagement_back.model.view;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工视图
 *
 */
@Data
public class EmployeeChartVO implements Serializable {



    private String department;

    private Integer employeeCount;

    private static final long serialVersionUID = 1L;
}