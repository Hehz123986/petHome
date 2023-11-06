package org.nb.petHome.controller;

import org.nb.petHome.entity.CodeResBean;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.IRedisService;
import org.nb.petHome.service.IUserService;
import org.nb.petHome.service.impl.RedisService;
import org.nb.petHome.service.impl.UserService;
import org.nb.petHome.utils.RegexUtil;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
@RestController
public class LoginController {
    private RedisService RedisService;
    private UserService UserService;

    @Autowired
    public LoginController(RedisService RedisService, UserService UserService) {
        this.RedisService = RedisService;
        this.UserService = UserService;
    }

    @GetMapping("/getverifycode")
    public NetResult sendVerifyCode(String phone) {
        return UserService.sendRegisterCode(phone);
    }

    @GetMapping("/verifyCode")
    public NetResult verifyCode(String phone, String code) {
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        String expiredV = RedisService.getValue(phone + phone);
        if (StringUtil.isNullOrNullStr(expiredV)) {
            return ResultGenerator.genFailResult("验证码过期");
        } else {
            if (expiredV.equals(code)) {
                return ResultGenerator.genSuccessResult("验证码正常");
            } else {
                return ResultGenerator.genFailResult("验证码错误");
            }
        }
    }

    public void register() {

    }
}
