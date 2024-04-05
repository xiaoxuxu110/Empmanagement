package com.xzh.empmanagement_back.service;

import com.xzh.empmanagement_back.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xzh.empmanagement_back.model.domain.request.user.UserUpdatePasswordRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author xzh
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userpassword  用户密码
     * @param checkPassword 校验密码
     * @param companyCode   公司编号
     * @return 返回新用户的ID
     */
    long userRegister(String userAccount, String userpassword, String checkPassword , String companyCode);

    /**
     *
     * @param userAccount   用户账户
     * @param userpassword  用户密码
     * @return 用户信息（不包含密码）
     */
    User userLogin(String userAccount, String userpassword, HttpServletRequest request);


    /**
     * 修改密码
     *
     * @param updatePasswordRequest
     * @param request
     */
    boolean updateUserPassword(UserUpdatePasswordRequest updatePasswordRequest, HttpServletRequest request);
    /**
     * 用户脱敏
     * @param originuser
     * @return
     */
    User getSafetyUser(User originuser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    boolean updateUser(User user);


    boolean addUser(User user);
}
