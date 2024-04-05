package com.xzh.empmanagement_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzh.empmanagement_back.common.BaseResponse;
import com.xzh.empmanagement_back.mapper.AttendanceMapper;
import com.xzh.empmanagement_back.mapper.EmployeeMapper;
import com.xzh.empmanagement_back.model.domain.Attendance;
import com.xzh.empmanagement_back.model.domain.Department;
import com.xzh.empmanagement_back.model.domain.Employee;
import com.xzh.empmanagement_back.model.view.AttendanceChartVO;
import com.xzh.empmanagement_back.model.view.EmployeeChartVO;
import com.xzh.empmanagement_back.service.DepartmentService;
import com.xzh.empmanagement_back.service.EmployeeService;
import com.xzh.empmanagement_back.service.UserService;
import com.xzh.empmanagement_back.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图表接口
 *
 *
 */
@RestController
@RequestMapping("/charts")
@Slf4j
public class ChartsController {

    @Resource
    private EmployeeService employeeService;

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private DepartmentService departmentService;


    @Resource
    private AttendanceMapper attendanceMapper;

    @Resource
    private UserService userService;


    @GetMapping("/getChartsData")
    public BaseResponse<HashMap<String, Object>> getChartsData() {
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        // 调用service的count方法实现统计查询
        long employeeCount = employeeService.count(wrapper);
        map.put("employeeCount", employeeCount);

        QueryWrapper<Department> wrapper1 = new QueryWrapper<>();
        long deptCount = departmentService.count(wrapper1);
        map.put("deptCount", deptCount);

        QueryWrapper<Attendance> wrapper2 = new QueryWrapper<>();
        wrapper2.select("count(*) as attendanceCount, attendanceType");
        wrapper2.groupBy("attendanceType");
        // 查询当前月份
        wrapper2.apply("date_format(attendanceDate,'%Y-%m') = date_format(now(),'%Y-%m')");
        List<Attendance> attendanceList = attendanceMapper.selectList(wrapper2);
        List<AttendanceChartVO> attendanceChartVOList = attendanceList.stream().map(attendance -> {
            AttendanceChartVO attendanceChartVO = new AttendanceChartVO();
            BeanUtils.copyProperties(attendance, attendanceChartVO);
            return attendanceChartVO;
        }).collect(Collectors.toList());
        map.put("attendanceList", attendanceChartVOList);

        QueryWrapper<Employee> wrapper3 = new QueryWrapper<>();
        // 与部门表联合查询
        wrapper3.select("count(*) as employeeCount, departmentId");
        wrapper3.groupBy("departmentId");
        List<Employee> employees = employeeMapper.selectList(wrapper3);
        List<EmployeeChartVO> employeeChartVOS = employees.stream().map(employee -> {
            EmployeeChartVO employeeChartVO = new EmployeeChartVO();
            QueryWrapper<Department> wrapper4 = new QueryWrapper<>();
            wrapper4.eq("id", employee.getDepartmentId());
            Department department = departmentService.getOne(wrapper4);
            employee.setDepartment(department.getDepartmentName());
            BeanUtils.copyProperties(employee, employeeChartVO);
            return employeeChartVO;
        }).collect(Collectors.toList());
        map.put("employees", employeeChartVOS);
        return ResultUtils.success(map);
    }
}
