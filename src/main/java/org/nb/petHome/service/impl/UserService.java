package org.nb.petHome.service.impl;


import org.nb.petHome.entity.CodeResBean;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.IUserService;
import org.nb.petHome.utils.RegexUtil;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
@Service
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
public class UserService implements IUserService {
    private Logger logger= (Logger) LoggerFactory.getLogger(this.getClass());

    private RedisService redisService;
    @Autowired
    public UserService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public NetResult sendRegisterCode(String phone) {

        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }

        if (true) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "用户已经被注册");
        }
        Long lastSendTime=0l;
        try {
           lastSendTime = Long.parseLong(redisService.getValue(phone));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
           lastSendTime=0l;
            redisService.cacheValue(phone, System.currentTimeMillis()+"", 60);
        }

        /**
         * 在一分钟之内
         */
        if (System.currentTimeMillis() - lastSendTime < 1 * 60 * 1000) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "调用频率过多");
        }
        //过来一分钟，发验证码
        String expiredV = redisService.getValue(phone+phone);
        if (StringUtil.isNullOrNullStr(expiredV)) {
            String code = "123456_" + System.currentTimeMillis();
           redisService.cacheSet(phone+phone, code,60);
            CodeResBean<String> resBean = new CodeResBean<>();
            resBean.code = code;
            return ResultGenerator.genSuccessResult(resBean);
        } else {
            return ResultGenerator.genSuccessResult(expiredV);
        }

    }
}
