package com.xzh.empmanagement_back.service;

import com.xzh.empmanagement_back.model.domain.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 12702
* @description 针对表【attendance(考勤表)】的数据库操作Service
* @createDate 2024-03-28 23:46:13
*/
public interface AttendanceService extends IService<Attendance> {
    void validAttendance(Attendance attendance, boolean add);
}
