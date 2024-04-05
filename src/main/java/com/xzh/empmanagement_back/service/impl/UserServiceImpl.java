package com.xzh.empmanagement_back.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzh.empmanagement_back.common.ErrorCode;
import com.xzh.empmanagement_back.exception.BusinessException;
import com.xzh.empmanagement_back.exception.ThrowUtils;
import com.xzh.empmanagement_back.mapper.EmployeeMapper;
import com.xzh.empmanagement_back.model.domain.Employee;
import com.xzh.empmanagement_back.model.domain.User;
import com.xzh.empmanagement_back.model.domain.request.user.UserUpdatePasswordRequest;
import com.xzh.empmanagement_back.service.UserService;
import com.xzh.empmanagement_back.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xzh.empmanagement_back.constant.UserConstant.USER_LOGIN_STATE;

/**
* 用户服务实现类
*
* @author xzh
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    @Resource
    private EmployeeMapper employeeMapper;
    final String SALT = "abcd";


    @Override
    public long userRegister(String userAccount, String userpassword, String checkPassword ,String companyCode) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,userpassword,checkPassword,companyCode))
        {
            //  登录失败
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if(userAccount.length() < 4)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR ,"用户账号过短");
        }
        if(userpassword.length() < 8 || checkPassword.length() < 8)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        if(companyCode.length() >  5)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"公司编号太长");
        }

        //  账户不能包括特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能含有特殊字符");
        }
        //密码和校验密码不相同 注册失败
        if  (!userpassword.equals(checkPassword))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码和校验密码不相同");
        }
        //  防止账户重复,进行校验
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在","");
        }
        //  防止工号重复,进行校验
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyCode",companyCode);
        count = userMapper.selectCount(queryWrapper);
        if(count > 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"工号已存在");
        }
        //  2.加密密码
       String encryptuserpassword = DigestUtils.md5DigestAsHex((SALT + userpassword).getBytes());
       //   3.插入数据进入到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptuserpassword);
        user.setCompanyCode(companyCode);
        boolean saveResult = this.save(user);
       //    如果保存失败 则返回-1
        if(!saveResult)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userpassword, HttpServletRequest request) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount,userpassword))
        {
            //  登录失败
            return null;
        }
        if(userAccount.length() < 4)
        {
            return null;
        }
        if(userpassword.length() < 8 )
        {
            return null;
        }

        //  账户不能包括特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        //  2.加密
        String encryptuserpassword = DigestUtils.md5DigestAsHex((SALT + userpassword).getBytes());
        //查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptuserpassword);
        User user = userMapper.selectOne(queryWrapper);
        //  用户不存在
        if (user == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"错误的用户名或密码");
        }
        //  3.对用户进行脱敏 防止传回密码给前端 保证安全性
        User safetyUser = getSafetyUser(user);

        //  4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE ,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originuser
     * @return
     */
    @Override
    public User getSafetyUser(User originuser)
    {
        if(originuser == null)
        {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originuser.getId());
        safetyUser.setUsername(originuser.getUsername());
        safetyUser.setUserPassword(originuser.getUserPassword());
        safetyUser.setUserAccount(originuser.getUserAccount());
        safetyUser.setAvatarUrl(originuser.getAvatarUrl());
        safetyUser.setGender(originuser.getGender());
        safetyUser.setEmail(originuser.getEmail());
        safetyUser.setUserStatus(originuser.getUserStatus());
        safetyUser.setCompanyCode(originuser.getCompanyCode());
        safetyUser.setUserRole(originuser.getUserRole());
        safetyUser.setPhone(originuser.getPhone());
        safetyUser.setCreateTime(originuser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 用户更新
     * @param user
     * @return
     */
    @Override
    public boolean updateUser(User user) {
        User userToUpdate = new User();
        // 没有输入密码则不用修改
        if (StringUtils.isNotBlank(user.getUserPassword())) {
            userToUpdate.setUserPassword(DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes()));
        }
        if (user.getUserRole() != null) {
            userToUpdate.setUserRole(user.getUserRole());
        }
        if (StringUtils.isNotBlank(user.getUserAccount())) {
            String userAccount = user.getUserAccount();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已经被注册");
            }
            userToUpdate.setUserAccount(userAccount);
        }
        if (StringUtils.isNotBlank(user.getUsername())) {
            userToUpdate.setUsername(user.getUsername());
        }
        if (user.getGender() != null) {
            userToUpdate.setGender(user.getGender());
        }
        if (user.getAvatarUrl() != null) {
            userToUpdate.setAvatarUrl(user.getAvatarUrl());
        }
        userToUpdate.setId(user.getId());
        return userMapper.updateById(userToUpdate) > 0;
    }

    /**
     * 修改密码
     *
     * @param updatePasswordRequest
     * @param request
     * @return
     */
    @Override
    public boolean updateUserPassword(UserUpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {
        if (updatePasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        // 原密码校验
        if(currentUser == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误,找不到该用户");
        }
        User loginUser = getSafetyUser(currentUser);
        Long userId = loginUser.getId();
        if (userId < 0 || userId == null) {
            System.out.println("lol");
            throw new BusinessException(ErrorCode.NULL_ERROR, "不存在该用户");
        }

        // 加入原密码校验
        String oldEncryptedPassword = DigestUtils.md5DigestAsHex((SALT + updatePasswordRequest.getUserPassword()).getBytes());
        if (!oldEncryptedPassword.equals(loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误","原密码错误,找不到该用户");
        }
        User user = new User();
        BeanUtils.copyProperties(updatePasswordRequest, user);
        user.setId(loginUser.getId());

        // 使用 MD5 加密新密码
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + updatePasswordRequest.getNewPassword()).getBytes());
        user.setUserPassword(encryptedPassword);
        if (encryptedPassword.equals(updatePasswordRequest.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "修改密码不能相同");
        }
        boolean result = updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }


    /**
     * 新增用户
     * @param user
     * @return
     */
    @Override
    public boolean addUser(User user) {
        synchronized (user.getUserAccount().intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", user.getUserAccount());
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }

            QueryWrapper<Employee> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("companyCode", user.getCompanyCode());
            Employee employee = employeeMapper.selectOne(queryWrapper1);
            if (employee == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司编号不存在");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes());
            // 3. 插入数据
            User userToAdd = new User();
            userToAdd.setUserAccount(user.getUserAccount());
            userToAdd.setCompanyCode(user.getCompanyCode());
            userToAdd.setUsername(user.getUsername());
            userToAdd.setUserPassword(encryptPassword);
            userToAdd.setGender(user.getGender());
            userToAdd.setUserRole(user.getUserRole());
            boolean saveResult = this.save(userToAdd);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return true;
        }
    }
}




