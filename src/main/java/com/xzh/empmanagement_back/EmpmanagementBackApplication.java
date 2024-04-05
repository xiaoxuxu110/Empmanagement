package com.xzh.empmanagement_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xzh.empmanagement_back.mapper")
public class EmpmanagementBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmpmanagementBackApplication.class, args);
    }

}
