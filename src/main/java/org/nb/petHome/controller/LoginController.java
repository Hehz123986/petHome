package org.nb.petHome.controller;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.net.Result;
import org.nb.petHome.service.impl.RedisService;
import org.nb.petHome.service.impl.UserService;
import org.nb.petHome.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
@RestController
public class LoginController {
    private RedisService redisService;
    private UserService userService;
    private RedisTemplate redisTemplate;

    @Autowired
    public LoginController(RedisService redisService, UserService userService, RedisTemplate redisTemplate) {
        this.redisService = redisService;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    //发送验证码
    @GetMapping("/getVerifyCode")
    public NetResult sendVerifyCode(@RequestParam String phone) {
        //检查手机号是否为空
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        //检查手机号是否符合手机号的规范
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        String code = RandomCode.getCode();
        String ssmResult = AliSendSMSUtil.sendSms(code, phone);
        if (ssmResult == null) {
            return ResultGenerator.genFailResult("发送验证码失败");
        }
        //把手机号和验证码存进redis,设置1个小时过期，用于测试，防止时间短了过期
        redisTemplate.opsForValue().set(phone, code, 60, TimeUnit.MINUTES);
        return ResultGenerator.genSuccessResult(Result.StringToJson(ssmResult));
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
        if (expiredV == null) {
            return ResultGenerator.genFailResult("验证码过期");
        } else {
            List<String> expiredList = new ArrayList<String>(expiredV);
            if (expiredList.isEmpty()) {
                return ResultGenerator.genFailResult("验证码过期");
            } else {
                String expiredValue = expiredList.get(0);
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


}
