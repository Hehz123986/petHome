package org.nb.petHome.service;

import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.User;
import org.nb.petHome.net.NetResult;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
public interface IUserService {
    /**
     * 发送二维码
     */
    NetResult sendRegisterCode(String phone);

    NetResult adminLogin(Employee employee);

    NetResult register(User user);

    NetResult userLogin(User user);
}
