package org.nb.petHome.controller;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.nb.petHome.common.Constants;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.Shop;
import org.nb.petHome.entity.User;
import org.nb.petHome.entity.UserFindShop;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.impl.ShopService;
import org.nb.petHome.service.impl.UserFindShopService;
import org.nb.petHome.service.impl.UserService;
import org.nb.petHome.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/14
 **/
@RestController
public class UserFindShopController {
    private UserFindShopService userFindShopService;
    private RedisTemplate redisTemplate;
    private ShopService shopService;
    private UserService userService;

    @Autowired
    public UserFindShopController(UserFindShopService userFindShopService, RedisTemplate redisTemplate, ShopService shopService,UserService userService) {
        this.userFindShopService=userFindShopService;
        this.redisTemplate = redisTemplate;
        this.shopService = shopService;
        this.userService=userService;
    }

    //待处理和已处理的寻主列表（商铺后台）
    @GetMapping("/ProcessingList")
    public NetResult PetList(int state, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        System.out.println(token);
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        //登陆后的商铺，从redis里面读取token
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //待处理
        if (state == 0) {
            //根据state判断是否处理
            List<UserFindShop> list = userFindShopService.findByState(state);
            return ResultGenerator.genSuccessResult(list);
        } else if (state == 1) {
            //已处理
            List<UserFindShop> list = userFindShopService.findByState(state);
            return ResultGenerator.genSuccessResult(list);
        } else {
            return ResultGenerator.genErrorResult(NetCode.PET_Process_ERROR, "列表错误");
        }
    }

    //用户查看自己寻主的列表（用户端）
    @GetMapping("/userList")
    public NetResult UserList(HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        System.out.println(token);
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过期
        if (user == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //根据自己的id查看自己的寻主记录
        List<UserFindShop> list = userFindShopService.findUserList(user.getId());
        try {
            //判断用户是否有发布过寻主任务
            if (list != null)
                return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            return ResultGenerator.genErrorResult(NetCode.USERLIST_IS_NULL, "寻主列表为空");
        }
        return ResultGenerator.genErrorResult(NetCode.USERLIST_IS_NULL, "寻主列表为空");
    }

    //商铺列表（用户端）
    @GetMapping("/shopList")
    public NetResult ShopList(HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        System.out.println(token);
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过期
        if (user == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //查看商铺列表
        List<Shop> list = shopService.list();
        return ResultGenerator.genSuccessResult(list);
    }

    //某个商户上架宠物列表（用户端）
    @GetMapping("/petList")
    public NetResult ShopPetList(long shopId, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        System.out.println(token);
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断token是否过期
        if (user == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //查询所有的商铺id
        List<Long> allShopId = userFindShopService.findAllShopId();
        // 使用distinct方法去重
        List<Long> distinctList = allShopId.stream().distinct().collect(Collectors.toList());
        //判断该店铺是否上架宠物
        if (!distinctList.contains(shopId)) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_IS_NULL, "该店铺未上架宠物");
        }
        List<UserFindShop> shopList = userFindShopService.getShopList(shopId);
        return ResultGenerator.genSuccessResult(shopList);
    }


    //寻主完毕（用户端）
    @GetMapping("/finish")
    public NetResult ShopPetFinish(int state, Long id) {
        //修改寻主的状态
        int rows = userFindShopService.updateState(state, id);
        //修改失败
        if (rows != 1) {
            return ResultGenerator.genFailResult("确认失败");
        }
        //在user_msg表中找到user_id
        long user_id = userFindShopService.findById(id).getUser_id();
        //根据user_id找到手机号
        String phone= userService.findById(user_id).getPhone();
        System.out.println(phone);
        //发送验证码
        sendShopCode(phone);
        //根据id查找返回数据
        UserFindShop userFindShop=userFindShopService.findById(id);
        return ResultGenerator.genSuccessResult(userFindShop);
    }

    public NetResult sendShopCode(@RequestParam String phone) {
        //检查手机号是否为空
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        //检查手机号是否符合手机号的规范
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        System.out.println(phone);
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "GET";
        String appcode = "299304e5c4f84e3ea92914b52a5ece99";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String code = RandomCode.getCode();
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "CST_ptdie100");
        bodys.put("phone_number", phone);
        System.out.println(code);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //  System.out.println(response.toString());
            //把手机号和验证码存进redis,设置1个小时过期，用于测试，防止时间短了过期
            redisTemplate.opsForValue().set(phone, code, 60, TimeUnit.MINUTES);
            String result = EntityUtils.toString(response.getEntity());
            //  System.out.println(result);
            return ResultGenerator.genFailResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("发送验证码失败！");
    }


}
