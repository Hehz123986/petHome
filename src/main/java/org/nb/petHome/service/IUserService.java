package org.nb.petHome.service;


import org.nb.petHome.entity.User;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.net.param.LoginParam;
import org.nb.petHome.net.param.RegisterParam;

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

    NetResult register(RegisterParam registerParam);

    NetResult Login(LoginParam loginParam);

    User findById(Long id);

}
