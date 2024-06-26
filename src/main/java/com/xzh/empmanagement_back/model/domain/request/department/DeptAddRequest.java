package com.xzh.empmanagement_back.model.domain.request.department;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class DeptAddRequest implements Serializable {

    /**
     * 部门
     */
    private String departmentName;

    private static final long serialVersionUID = 1L;
}