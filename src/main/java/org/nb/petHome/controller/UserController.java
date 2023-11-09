package org.nb.petHome.controller;

import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.User;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.impl.UserService;
import org.nb.petHome.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/9
 **/
@RestController
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/register")
    public NetResult register(@RequestBody User user) {
        try {
            NetResult netResult = userService.register(user);
            return netResult;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
        }
    }

    @PostMapping("/userLogin")
    public NetResult userLogin(@RequestBody User user) {
        try {
            NetResult netResult = userService.userLogin(user);
            return netResult;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
        }
    }

}
