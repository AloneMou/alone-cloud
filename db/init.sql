create database alone_cloud;

drop table if exists system_route;
create table system_route
(
    id          bigint primary key auto_increment,
    name        varchar(255) not null comment '路由名称',
    uri         varchar(255) not null comment '路由地址',
    predicates  longtext     not null comment '路由断言',
    filters     longtext     not null comment '路由过滤器',
    metadata    json         not null comment '路由元数据',
    description varchar(255) comment '路由描述',
    status      int(1)     default 0 comment '路由状态',
    sort        int(10)    default 0 comment '路由排序',
    create_time datetime   default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '更新时间',
    creator     bigint comment '创建人',
    updater     bigint comment '更新人',
    deleted     tinyint(1) default 0 comment '是否删除'
) comment '系统路由表';
