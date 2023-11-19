package org.nb.petHome.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.nb.petHome.entity.CodeResBean;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.User;
import org.nb.petHome.mapper.EmployeeMapper;
import org.nb.petHome.mapper.UserMapper;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.net.param.LoginParam;
import org.nb.petHome.net.param.RegisterParam;
import org.nb.petHome.service.IUserService;
import org.nb.petHome.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
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
    public UserService(RedisService redisService, EmployeeMapper employeeMapper, RedisTemplate redisTemplate, UserMapper userMapper) {
        this.redisService = redisService;
        this.employeeMapper = employeeMapper;
        this.redisTemplate = redisTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public NetResult sendRegisterCode(String phone) {

        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        User u = userMapper.findByPhone(phone);
        if (u != null) {
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
            Random random = new Random();
            int number = random.nextInt(1000000);
            String code = String.format("%06d", number);
            redisService.cacheSet(phone + phone, code, 60);
            CodeResBean<String> resBean = new CodeResBean<>();
            resBean.code = code;
            return ResultGenerator.genSuccessResult(resBean);
        } else {
            return ResultGenerator.genSuccessResult(expiredV);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NetResult register(RegisterParam registerParam) {
        //判断用户名是否为空
        if (StringUtil.isEmpty(registerParam.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        //判断邮箱是否为空
        if (StringUtil.isEmpty(registerParam.getEmail())) {
            return ResultGenerator.genErrorResult(NetCode.EMAIL_INVALID, "邮箱不能为空");
        }
        //判断密码是否为空
        if (StringUtil.isEmpty(registerParam.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        //检察一下年龄
        if (registerParam.getAge() < 0) {
            return ResultGenerator.genErrorResult(NetCode.AGE_INVALID, "年龄不能小于0");
        }
        String phone=registerParam.getPhone();
        //判断手机号是否为空
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        // 检查手机格式是否正确
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "非法的手机号");
        }
        //根据手机号，判断是否注册
        User u = userMapper.findByPhone(phone);
        if (u != null) {
            return ResultGenerator.genFailResult("该用户已注册");
        }
        //从redis读取验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(phone);
        //检查是否有验证码
        if (!StringUtil.isEmpty(cachedCode)) {
            //比对传入的验证码和读取的是否一样
            if (registerParam.getCode().equals(cachedCode)) {
                System.out.println(cachedCode);
                u = new User();
                //copy属性
                BeanUtils.copyProperties(registerParam,u);
                //密码进行MD5加密
                u.setPassword(MD5Util.MD5Encode(phone, "utf-8"));
                //设置当前时间为注册时间
                u.setCreatetime(System.currentTimeMillis());
                userMapper.register(u);
                return ResultGenerator.genSuccessResult("注册成功");
            } else {
                return ResultGenerator.genFailResult("注册失败");
            }
        } else {
            return ResultGenerator.genFailResult("验证码过期");
        }
    }

    //用户和管理员登录，用手机号和密码登录，类型代表不同的身份
    @Override
    public NetResult Login(LoginParam loginParam) {
        //检查一下手机号是否为空
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        // 检查手机格式是否正确
        if (!RegexUtil.isPhoneValid(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "非法的手机号");
        }
        //检查一下密码是否为空
        if (StringUtil.isEmpty(loginParam.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        //用户登录
        if (loginParam.getType() == 1) {
            //通过用户名和密码查找是否在数据库
            User u = userMapper.userLogin(loginParam.getPhone(), loginParam.getPassword());
            if (u == null) {
                return ResultGenerator.genFailResult("账号或密码错误");
            } else {
                //随机生成一个token
                String token = UUID.randomUUID().toString();
                logger.info("token__" + token);
                //给用户设置一个token
                u.setToken(token);
                //密码设置为空防止后台看到
                u.setPassword(null);
                //30分钟token过期
                redisTemplate.opsForValue().set(token, u, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(u);
            }
        }
        //管理员登录
        if (loginParam.getType() == 0) {
            Employee e = employeeMapper.login(loginParam.getPhone(), loginParam.getPassword());
            if (e == null) {
                return ResultGenerator.genFailResult("账号或密码错误");
            } else {
                //生成一个token
                String token = UUID.randomUUID().toString();
                logger.info("token__" + token);
                //给员工设置一个token
                e.setToken(token);
                //密码设置为空防止后台看到
                e.setPassword(null);
                //30分钟token过期
                redisTemplate.opsForValue().set(token, e, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(e);
            }
        } else {
            return ResultGenerator.genFailResult("用户类型错误");
        }
    }



    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }


   /* private String convertStreamToString(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            // 处理异常
            return null;
        }
    }*/
}
