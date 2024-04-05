package com.xzh.empmanagement_back.service;
import java.util.Date;

import com.xzh.empmanagement_back.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Resource
     private  UserService userService;
    @Test
    public void testAddUser()
    {
        User user = new User();
        user.setUsername("dogYupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://img1.baidu.com/it/u=1645832847,2375824523&fm=253&fmt=auto&app=138&f=JPEG?w=480&h=480");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setEmail("123");
        user.setPhone("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);


    }

    @Test
    void userRegister() {
        String userAccount = "xzh123";
        String userPassword = "";
        String checkPassword = "123456";
        String companyCode = "666";
        long result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);

        userAccount = "xu";
        result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);

        userAccount = "xzh123";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);

        userAccount = "x zh123";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);

        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);

        userAccount = "xzh123";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);

        userAccount = "xzh666";
        result = userService.userRegister(userAccount, userPassword, checkPassword,companyCode);
        Assertions.assertEquals(-1, result);
    }
}