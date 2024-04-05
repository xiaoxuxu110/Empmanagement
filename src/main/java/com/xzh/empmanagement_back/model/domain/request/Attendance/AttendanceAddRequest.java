package com.xzh.empmanagement_back.model.domain.request.Attendance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class AttendanceAddRequest implements Serializable {

    /**
     * 公司编号
     */
    private String companyCode;

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

    private static final long serialVersionUID = 1L;
}