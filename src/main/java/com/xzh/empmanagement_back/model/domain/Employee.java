package com.xzh.empmanagement_back.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 员工实体类
 *
 *
 */
@Data
public class Employee implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 部门id
     */
    private Long departmentId;

    @TableField(exist = false)
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

    @TableField(exist = false)
    private Integer employeeCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}