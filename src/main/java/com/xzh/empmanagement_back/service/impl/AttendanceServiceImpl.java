package com.xzh.empmanagement_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzh.empmanagement_back.common.ErrorCode;
import com.xzh.empmanagement_back.exception.BusinessException;
import com.xzh.empmanagement_back.model.domain.Attendance;
import com.xzh.empmanagement_back.service.AttendanceService;
import com.xzh.empmanagement_back.mapper.AttendanceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author 12702
* @description 针对表【attendance(考勤表)】的数据库操作Service实现
* @createDate 2024-03-28 23:46:13
*/

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance>
        implements AttendanceService{

    @Override
    public void validAttendance(Attendance attendance, boolean add) {
        if (attendance == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String companyCode = attendance.getCompanyCode();
        Date attendanceDate = attendance.getAttendanceDate();
        String attendanceType = attendance.getAttendanceType();
        String name = attendance.getName();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(companyCode,attendanceType, name) || attendanceDate == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "所有参数都是必填项");
            }
        }
    }
}




