package org.nb.petHome.controller;

import org.nb.petHome.common.Urls;
import org.nb.petHome.entity.CodeResBean;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.impl.RedisService;
import org.nb.petHome.service.impl.UserService;
import org.nb.petHome.utils.RegexUtil;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
@RestController
public class LoginController {
    private RedisService redisService;
    private UserService userService;

    @Autowired
    public LoginController(RedisService redisService, UserService userService) {
        this.redisService = redisService;
        this.userService = userService;
    }

    @GetMapping("/getverifycode")
    public NetResult sendVerifyCode( String phone) {
        return userService.sendRegisterCode(phone);
    }

    @GetMapping("/verifyCode")
    public NetResult verifyCode(String phone, String code) {
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        Set<String> expiredV = redisService.getSet(phone + phone);
        if(expiredV==null) {
            return ResultGenerator.genFailResult("验证码过期");
        }
        else {
            List<String> expiredList=new ArrayList<String>(expiredV);
            if(expiredList.isEmpty()){
                return ResultGenerator.genFailResult("验证码过期");
            }
            else {
                String expiredValue=expiredList.get(0);
                if (StringUtil.isNullOrNullStr(expiredValue)) {
                    return ResultGenerator.genFailResult("验证码过期");
                } else {
                    if (expiredV.equals(code)) {
                        return ResultGenerator.genSuccessResult("验证码正常");
                    } else {
                        return ResultGenerator.genFailResult("验证码错误");
                    }
                }
            }
        }

    }

    @PostMapping("/login")
    public NetResult login(@RequestBody Employee employee) {
        try {
            NetResult netResult = userService.adminLogin(employee);
            return netResult;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
        }

    }

    public void register() {

    }
}
