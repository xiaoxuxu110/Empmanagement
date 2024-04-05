package com.xzh.empmanagement_back.model.domain.request.department;

import com.xzh.empmanagement_back.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeptQueryRequest extends PageRequest implements Serializable {

    /**
     * 部门
     */
    private String departmentName;

    private static final long serialVersionUID = 1L;
}