package com.xzh.empmanagement_back.model.domain.request.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginrRequest implements Serializable {
    private static final long serialVersionUID = 7678767007843940769L;

    private String  userAccount;

    private String  userPassword;

}
