package org.nb.petHome.entity;
import lombok.Data;
import java.io.Serializable;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/9
 **/
@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private int age;
    private Long createtime ;
    private String token;
}
