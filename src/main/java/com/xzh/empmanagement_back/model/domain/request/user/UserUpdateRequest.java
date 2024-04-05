package com.xzh.empmanagement_back.model.domain.request.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 *
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户角色: user, admin
     */
    private Integer userRole;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 公司编号
     */
    private String companyCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}