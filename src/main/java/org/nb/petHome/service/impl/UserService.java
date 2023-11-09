package org.nb.petHome.service.impl;


import org.nb.petHome.entity.CodeResBean;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.User;
import org.nb.petHome.mapper.EmployeeMapper;
import org.nb.petHome.mapper.UserMapper;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.IUserService;
import org.nb.petHome.utils.MD5Util;
import org.nb.petHome.utils.RegexUtil;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UserService implements IUserService {
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private RedisService redisService;
    private EmployeeMapper employeeMapper;
    private RedisTemplate redisTemplate;
    private UserMapper userMapper;

    @Autowired
    public UserService(RedisService redisService, EmployeeMapper employeeMapper, RedisTemplate redisTemplate,UserMapper userMapper) {
        this.redisService = redisService;
        this.employeeMapper = employeeMapper;
        this.redisTemplate = redisTemplate;
        this.userMapper=userMapper;
    }

    @Override
    public NetResult sendRegisterCode(String phone) {

        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        User u =userMapper.findByPhone(phone);
        if (u!=null) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "用户已经被注册");
        }
        Long lastSendTime = 0L;
        try {
            lastSendTime = Long.parseLong(redisService.getValue(phone));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            lastSendTime = 0l;
            redisService.cacheValue(phone, System.currentTimeMillis() + "", 60);
        }

        /**
         * 在一分钟之内
         */
        if (System.currentTimeMillis() - lastSendTime < 1 * 60 * 1000) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "调用频率过多");
        }
        //过来一分钟，发验证码
        String expiredV = redisService.getValue(phone + phone);
        if (StringUtil.isNullOrNullStr(expiredV)) {
            Random random=new Random();
            int number=random.nextInt(1000000);
            String code =String.format("%06d",number);
            redisService.cacheSet(phone + phone, code, 60);
            CodeResBean<String> resBean = new CodeResBean<>();
            resBean.code = code;
            return ResultGenerator.genSuccessResult(resBean);
        } else {
            return ResultGenerator.genSuccessResult(expiredV);
        }

    }

    @Override
    public NetResult adminLogin(Employee employee) {
        if (StringUtil.isEmpty(employee.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        if (StringUtil.isEmpty(employee.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        employee.setPassword(MD5Util.MD5Encode(employee.getPassword(), "utf-8"));
        Employee e = employeeMapper.login(employee);
        if (e == null) {
            return ResultGenerator.genFailResult("账号或密码错误");
        } else {
            //生成一个token
            String token = UUID.randomUUID().toString();
            logger.info("token__" + token);
            e.setToken(token);
            e.setPassword(null);
            //30分钟token过期
            redisTemplate.opsForValue().set(token, e, 30, TimeUnit.MINUTES);
            return ResultGenerator.genSuccessResult(e);
        }
    }

    @Override
    public NetResult register(User user) {
        if (StringUtil.isEmpty(user.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(user.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        if (StringUtil.isEmpty(user.getEmail())) {
            return ResultGenerator.genErrorResult(NetCode.EMAIL_INVALID, "邮箱不能为空");
        }
        if (StringUtil.isEmpty(user.getPassword())) {
            user.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        }
        if (user.getAge() < 0) {
            return ResultGenerator.genErrorResult(NetCode.AGE_INVALID, "年龄不能小于0");
        }
        User u = userMapper.findByPhone(user.getPhone());
        if (u != null) {
            return ResultGenerator.genFailResult("该用户已注册");
        }
        int rows = userMapper.register(user);
        if (rows == 1) {
            return ResultGenerator.genSuccessResult("注册成功");
        } else {
            return ResultGenerator.genFailResult("注册失败");
        }

    }

    @Override
    public NetResult userLogin(User user) {
        if (StringUtil.isEmpty(user.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(user.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8"));
        User u=userMapper.userLogin(user);
        if (u == null) {
            return ResultGenerator.genFailResult("账号或密码错误");
        } else {
            //生成一个token
            String token = UUID.randomUUID().toString();
            logger.info("token__" + token);
            u.setToken(token);
            u.setPassword(null);
            //30分钟token过期
            redisTemplate.opsForValue().set(token, u, 30, TimeUnit.MINUTES);
            return ResultGenerator.genSuccessResult(u);
        }
    }
}
