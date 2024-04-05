-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)  default '1'                                                                 null comment '用户昵称',
    userAccount  varchar(256)                                                                              null comment '账号',
    avatarUrl    varchar(1024) default 'https://ww1.sinaimg.cn/mw690/4d5d8aa5ly1hng4s27c6ij20to18g7fx.jpg' null comment '用户头像',
    gender       tinyint                                                                                   null comment '性别',
    userPassword varchar(512)                                                                              not null comment '密码',
    email        varchar(512)                                                                              null comment '邮箱',
    userStatus   int           default 0                                                                   null comment '状态 0-正常',
    phone        varchar(128)                                                                              null comment '电话',
    createTime   datetime      default CURRENT_TIMESTAMP                                                   null comment '创建时间',
    updateTime   datetime      default CURRENT_TIMESTAMP                                                   null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint       default 0                                                                   not null comment '是否删除',
    userRole     int           default 0                                                                   not null comment '0-普通用户 1-管理员',
    companyCode  varchar(512)                                                                              null comment '公司编号'
)
    comment '用户表';


----------------------------------------------
-- auto-generated definition
create table employee
(
    id               bigint auto_increment comment 'id'
        primary key,
    departmentId     bigint                                                                                                              null comment '部门 id',
    employeeName     varchar(256) charset utf8mb4                                                                                        null comment '员工名',
    companyCode      varchar(128)                                                                                                        not null comment '公司编号',
    employeeAvatar   varchar(512) collate latin1_german2_ci default 'https://ww1.sinaimg.cn/mw690/4d5d8aa5ly1hng4s27c6ij20to18g7fx.jpg' null comment '员工头像',
    gender           tinyint                                                                                                             null comment '性别',
    employeePosition varchar(256) charset utf8mb4                                                                                        null comment '职位',
    createTime       datetime                                default CURRENT_TIMESTAMP                                                   not null comment '创建时间',
    updateTime       datetime                                default CURRENT_TIMESTAMP                                                   not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint                                 default 0                                                                   not null comment '是否删除',
    constraint uni_userAccount
        unique (companyCode),
    constraint fk_dept
        foreign key (departmentId) references department (id)
            on update cascade on delete cascade
)
    comment '员工表';

----------------------------------------------
-- auto-generated definition
create table department
(
    id             bigint auto_increment comment 'id'
        primary key,
    departmentName varchar(256) charset utf8mb4       null comment '部门',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
)
    comment '部门表';

----------------------------------------------
-- auto-generated definition
create table attendance
(
    id             bigint auto_increment comment 'id'
        primary key,
    companyCode    varchar(512)                       not null comment '公司编号',
    name           varchar(255) charset utf8mb4       null comment '姓名',
    attendanceDate date                               not null comment '考勤日期',
    attendanceType varchar(256) charset utf8mb4       not null comment '考勤类型（正常、迟到、缺勤、请假）',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint  default 0                 not null comment '是否删除'
)
    comment '考勤表';



