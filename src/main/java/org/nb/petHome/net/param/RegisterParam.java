package org.nb.petHome.net.param;

import lombok.Data;
import org.nb.petHome.entity.User;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/12
 **/
@Data
public class RegisterParam {
    public String username;
    public String email;
    public String phone;
    public String password;
    public int age;
    public String code;
}
