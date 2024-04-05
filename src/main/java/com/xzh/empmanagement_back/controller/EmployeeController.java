package com.xzh.empmanagement_back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzh.empmanagement_back.annotation.AuthCheck;
import com.xzh.empmanagement_back.common.BaseResponse;
import com.xzh.empmanagement_back.common.DeleteRequest;
import com.xzh.empmanagement_back.common.ErrorCode;
import com.xzh.empmanagement_back.constant.CommonConstant;
import com.xzh.empmanagement_back.exception.BusinessException;
import com.xzh.empmanagement_back.model.domain.Department;
import com.xzh.empmanagement_back.model.domain.Employee;
import com.xzh.empmanagement_back.model.domain.User;
import com.xzh.empmanagement_back.model.domain.request.employee.EmployeeAddRequest;
import com.xzh.empmanagement_back.model.domain.request.employee.EmployeeQueryRequest;
import com.xzh.empmanagement_back.model.domain.request.employee.EmployeeUpdateRequest;
import com.xzh.empmanagement_back.service.DepartmentService;
import com.xzh.empmanagement_back.service.EmployeeService;
import com.xzh.empmanagement_back.service.UserService;
import com.xzh.empmanagement_back.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.xzh.empmanagement_back.constant.UserConstant.ADMIN_ROLE;
import static com.xzh.empmanagement_back.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 员工接口
 *
 *
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @Resource
    private UserService userService;

    @Resource
    private DepartmentService departmentService;

    // region 增删改查

    /**
     * 创建
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addEmployee(@RequestBody EmployeeAddRequest employeeAddRequest, HttpServletRequest request) {
        if (employeeAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeAddRequest, employee);
        // 校验
        employeeService.validEmployee(employee, true);
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("departmentName", employee.getDepartment());
        Department department = departmentService.getOne(wrapper);
        if (department == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "部门不存在");
        }
        employee.setDepartmentId(department.getId());
        boolean result = employeeService.save(employee);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Long id = employee.getId();
        return ResultUtils.success(id);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteEmployee(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Employee oldEmployee = employeeService.getById(id);
        if (oldEmployee == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "员工不存在");
        }
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = employeeService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param employeeUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateEmployee(@RequestBody EmployeeUpdateRequest employeeUpdateRequest,
                                            HttpServletRequest request) {
        if (employeeUpdateRequest == null || employeeUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeUpdateRequest, employee);
        // 参数校验
        employeeService.validEmployee(employee, false);
        long id = employeeUpdateRequest.getId();
        // 判断是否存在
        Employee oldEmployee = employeeService.getById(id);
        if (oldEmployee == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "员工不存在");
        }
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("departmentName", employee.getDepartment());
        Department department = departmentService.getOne(wrapper);
        if (department == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "部门不存在");
        }
        employee.setDepartmentId(department.getId());
        boolean result = employeeService.updateById(employee);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Employee> getEmployeeById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employee = employeeService.getById(id);
        return ResultUtils.success(employee);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param employeeQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<Employee>> listEmployee(EmployeeQueryRequest employeeQueryRequest) {
        Employee employeeQuery = new Employee();
        if (employeeQueryRequest != null) {
            BeanUtils.copyProperties(employeeQueryRequest, employeeQuery);
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>(employeeQuery);
        // 如果查询条件中包含部门名称，则需要先查询部门表，再根据部门 id 查询员工表
        if (employeeQuery.getDepartment() != null) {
            QueryWrapper<Department> wrapper = new QueryWrapper<>();
            wrapper.eq("departmentName", employeeQuery.getDepartment());
            Department department = departmentService.getOne(wrapper);
            if (department == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "部门不存在");
            }
            queryWrapper.eq("departmentId", department.getId()); // 根据部门 id 查询员工表
        }
        List<Employee> employeeList = employeeService.list(queryWrapper);
        List<Employee> list = employeeList.stream().map(employee -> {
            Employee employeeVO = new Employee();
            QueryWrapper<Department> wrapper = new QueryWrapper<>();
            wrapper.eq("id", employee.getDepartmentId());
            Department department = departmentService.getOne(wrapper);
            if (department != null) {
                employee.setDepartment(department.getDepartmentName());
            }
            BeanUtils.copyProperties(employee, employeeVO);
            return employeeVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 分页获取列表
     *
     * @param employeeQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Employee>> listEmployeeByPage(EmployeeQueryRequest employeeQueryRequest, HttpServletRequest request) {
        if (employeeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Employee employeeQuery = new Employee();
        BeanUtils.copyProperties(employeeQueryRequest, employeeQuery);
        long current = employeeQueryRequest.getCurrent();
        long size = employeeQueryRequest.getPageSize();
        String sortField = employeeQueryRequest.getSortField();
        String sortOrder = employeeQueryRequest.getSortOrder();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>(employeeQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<Employee> employeePage = employeeService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(employeePage);
    }

    private boolean isAdmin(HttpServletRequest request)
    {
        //仅管理员可删除
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return  user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
