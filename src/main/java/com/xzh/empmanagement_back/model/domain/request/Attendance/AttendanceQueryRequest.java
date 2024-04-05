package com.xzh.empmanagement_back.model.domain.request.Attendance;


import com.xzh.empmanagement_back.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttendanceQueryRequest extends PageRequest implements Serializable {

    /**
     * 公司编号
     */
    private String companyCode;
    /**
     * 员工姓名
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

    private static final long serialVersionUID = 1L;
}