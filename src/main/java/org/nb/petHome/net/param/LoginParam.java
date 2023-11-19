package org.nb.petHome.net.param;

import lombok.Data;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/12
 **/
@Data
public class LoginParam {
    private String phone;
    private String password;
    private int type;

}
