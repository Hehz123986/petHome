package org.nb.petHome.entity;

import lombok.Data;

import java.util.Date;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/9
 **/
@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String salt;
    private String password;
    private int state;
    private int age;
    private Date createtime =new Date();
    private String headImg;
    private String token;
}
