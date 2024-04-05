package com.xzh.empmanagement_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzh.empmanagement_back.annotation.AuthCheck;
import com.xzh.empmanagement_back.common.BaseResponse;
import com.xzh.empmanagement_back.common.DeleteRequest;
import com.xzh.empmanagement_back.common.ErrorCode;
import com.xzh.empmanagement_back.constant.CommonConstant;
import com.xzh.empmanagement_back.exception.BusinessException;
import com.xzh.empmanagement_back.mapper.AttendanceMapper;
import com.xzh.empmanagement_back.mapper.EmployeeMapper;
import com.xzh.empmanagement_back.model.domain.Attendance;
import com.xzh.empmanagement_back.model.domain.Employee;
import com.xzh.empmanagement_back.model.domain.User;
import com.xzh.empmanagement_back.model.domain.request.Attendance.AttendanceAddRequest;
import com.xzh.empmanagement_back.model.domain.request.Attendance.AttendanceQueryRequest;
import com.xzh.empmanagement_back.model.domain.request.Attendance.AttendanceUpdateRequest;
import com.xzh.empmanagement_back.service.AttendanceService;
import com.xzh.empmanagement_back.service.UserService;
import com.xzh.empmanagement_back.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.xzh.empmanagement_back.constant.UserConstant.ADMIN_ROLE;
import static com.xzh.empmanagement_back.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 考勤
 *
 *
 */
@RestController
@RequestMapping("/attendance")
@Slf4j
public class AttendanceController {

    @Resource
    private AttendanceService attendanceService;

    @Resource
    private UserService userService;

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private AttendanceMapper attendanceMapper;

    // region 增删改查

    /**
     * 创建
     *
     * @param attendanceAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addAttendance(@RequestBody AttendanceAddRequest attendanceAddRequest, HttpServletRequest request) {
        if (attendanceAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数错误");
        }
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceAddRequest, attendance);
        // 校验
        attendanceService.validAttendance(attendance, true);
        String companyCode= attendanceAddRequest.getCompanyCode();
        String name = attendanceAddRequest.getName();
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("companyCode", companyCode);
        Employee employee = employeeMapper.selectOne(wrapper);
        if (employee == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公司编号不存在");
        }
        QueryWrapper<Employee> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("employeeName", name);
        List<Employee> employees = employeeMapper.selectList(wrapper1);
        if (employees.size() < 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "没有该员工");
        }
        boolean result = attendanceService.save(attendance);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"签到失败");
        }
        long newAttendanceId = attendance.getId();
        return ResultUtils.success(newAttendanceId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAttendance(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        User user = userService.getSafetyUser(currentUser);
        long id = deleteRequest.getId();
        // 判断是否存在
        Attendance oldAttendance = attendanceService.getById(id);
        if (oldAttendance == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = attendanceService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param attendanceUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateAttendance(@RequestBody AttendanceUpdateRequest attendanceUpdateRequest,
                                            HttpServletRequest request) {
        if (attendanceUpdateRequest == null || attendanceUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceUpdateRequest, attendance);
        // 参数校验
        attendanceService.validAttendance(attendance, false);
        long id = attendanceUpdateRequest.getId();
        // 判断是否存在
        Attendance oldAttendance = attendanceService.getById(id);
        if (oldAttendance == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        String companyCode = attendanceUpdateRequest.getCompanyCode();
        String name = attendanceUpdateRequest.getName();
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("companyCode", companyCode);
        Employee employee = employeeMapper.selectOne(wrapper);
        if (employee == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "员工账号不存在");
        }
        QueryWrapper<Employee> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("employeeName", name);
        List<Employee> employees = employeeMapper.selectList(wrapper1);
        if (employees.size() < 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "没有该员工");
        }
        // 仅本人或管理员可修改
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean result = attendanceService.updateById(attendance);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Attendance> getAttendanceById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendance = attendanceService.getById(id);
        return ResultUtils.success(attendance);
    }

    @GetMapping("/getMyAttendance")
    public BaseResponse<List<Attendance>> getMyAttendance(AttendanceQueryRequest attendanceQueryRequest, HttpServletRequest request) {
        Attendance attendanceQuery = new Attendance();
        if (attendanceQueryRequest != null) {
            BeanUtils.copyProperties(attendanceQueryRequest, attendanceQuery);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        User user = userService.getSafetyUser(currentUser);
        String companyCode  = user.getCompanyCode();
        attendanceQuery.setCompanyCode(companyCode );
        QueryWrapper<Attendance> wrapper = new QueryWrapper<>(attendanceQuery);
        List<Attendance> attendances = attendanceMapper.selectList(wrapper);
        // 根据考勤日期排序
        attendances.sort(Comparator.comparing(Attendance::getAttendanceDate));
        return ResultUtils.success(attendances);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param attendanceQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<Attendance>> listAttendance(AttendanceQueryRequest attendanceQueryRequest) {
        Attendance attendanceQuery = new Attendance();
        if (attendanceQueryRequest != null) {
            BeanUtils.copyProperties(attendanceQueryRequest, attendanceQuery);
        }
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>(attendanceQuery);
        List<Attendance> attendanceList = attendanceService.list(queryWrapper);
        // 根据考勤日期排序
        attendanceList.sort(Comparator.comparing(Attendance::getAttendanceDate));
        return ResultUtils.success(attendanceList);
    }

    /**
     * 分页获取列表
     *
     * @param attendanceQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Attendance>> listAttendanceByPage(AttendanceQueryRequest attendanceQueryRequest, HttpServletRequest request) {
        if (attendanceQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Attendance attendanceQuery = new Attendance();
        BeanUtils.copyProperties(attendanceQueryRequest, attendanceQuery);
        long current = attendanceQueryRequest.getCurrent();
        long size = attendanceQueryRequest.getPageSize();
        String sortField = attendanceQueryRequest.getSortField();
        String sortOrder = attendanceQueryRequest.getSortOrder();

        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>(attendanceQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<Attendance> attendancePage = attendanceService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(attendancePage);
    }

    /**
     * 判断权限函数
     */
    private boolean isAdmin(HttpServletRequest request)
    {
        //仅管理员可删除
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return  user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
