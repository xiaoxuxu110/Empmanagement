package com.xzh.empmanagement_back.model.domain.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xzh
 */
@Data
public class UserUpdatePasswordRequest implements Serializable {

    private static final long serialVersionUID = -5996345129538944393L;

    /**
     * 原密码
     */
    private String userPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
