package com.xzh.empmanagement_back.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤表
 * @TableName attendance
 */
@TableName(value ="attendance")
@Data
public class Attendance implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公司编号
     */
    private String companyCode;

    /**
     * 姓名
     */
    private String name;

    /**
     * 考勤日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date attendanceDate;

    /**
     * 考勤类型（正常、迟到、缺勤、请假）
     */
    private String attendanceType;

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

    @TableField(exist = false)
    private int attendanceCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}