package com.xzh.empmanagement_back.model.view;

import lombok.Data;

import java.io.Serializable;

/**
 * 考勤视图
 *
 *
 * @TableName product
 */
@Data
public class AttendanceChartVO implements Serializable {



    /**
     * 考勤类型（正常、迟到、缺勤、请假）
     */
    private String attendanceType;

    /**
     * 考勤次数
     */

    private Integer attendanceCount;

    private static final long serialVersionUID = 1L;
}