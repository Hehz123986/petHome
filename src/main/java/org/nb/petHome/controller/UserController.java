package org.nb.petHome.controller;

import org.nb.petHome.common.Constants;
import org.nb.petHome.entity.*;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.net.param.LoginParam;
import org.nb.petHome.net.param.RegisterParam;
import org.nb.petHome.service.impl.*;
import org.nb.petHome.utils.AddressDistanceComparator;
import org.nb.petHome.utils.GaoDeMapUtil;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/9
 **/
@RestController
public class UserController {



    private UserService userService;
    private ShopService shopService;
    private PetCategoryService petCategoryService;
    private RedisTemplate redisTemplate;
    private UserFindShopService userFindShopService;

    @Autowired
    public UserController(UserService userService, ShopService shopService, PetCategoryService petCategoryService, RedisTemplate redisTemplate, UserFindShopService userFindShopService) {
        this.userService = userService;
        this.shopService = shopService;
        this.petCategoryService = petCategoryService;
        this.redisTemplate = redisTemplate;
        this.userFindShopService=userFindShopService;
    }
    //注册
    @PostMapping("/register")
    public NetResult register(@RequestBody RegisterParam registerParam) {
        try {
            NetResult netResult = userService.register(registerParam);
            return netResult;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }
    //登录
    @PostMapping("/login")
    public NetResult Login(@RequestBody LoginParam loginParam) {
        try {
            NetResult netResult = userService.Login(loginParam);
            return netResult;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }

    //寻主
    @PostMapping("/homingTask")
    public NetResult HomingTask(@RequestBody UserFindShop userFindShop, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        User user = (User) redisTemplate.opsForValue().get(token);
        //判断user的token是否过期
        if (user == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //添加用户id
        userFindShop.setUser_id(user.getId());
        //判断宠物名是否为空
        if (StringUtil.isEmpty(userFindShop.getName())) {
            return ResultGenerator.genErrorResult(NetCode.PET_NAME_INVALID, "宠物名不能为空");
        }
        //判断性别是否为0和1
        if (userFindShop.getSex() !=0 && userFindShop.getSex()!=1) {
            return ResultGenerator.genErrorResult(NetCode.PET_SEX_INVALID, "性别错误");
        }
        //判断接种是否为0和1
        if (userFindShop.getIsInoculate() !=0 && userFindShop.getIsInoculate()!=1) {
            return ResultGenerator.genErrorResult(NetCode.PET_IS_INOCULATION_INVALID, "接种信息错误");
        }
        //判断地址是否为空
        if (StringUtil.isEmpty(userFindShop.getAddress())) {
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, "地址不能为空");
        }
        //判断年龄是否为负数
        if (userFindShop.getBirth() <= 0) {
            return ResultGenerator.genErrorResult(NetCode.BIRTH_INVALID, "年龄小于0");
        }
        //获取所有的商铺列表
        List<Shop> shopList = shopService.list();
        List<Location> locations = new ArrayList<>();
        for (Shop value : shopList) {
            try {
                locations.add(GaoDeMapUtil.getLngAndLag(value.getAddress()));
            } catch (UnsupportedEncodingException e) {
                LoggerFactory.getLogger(UserController.class).error(e.getMessage());
            }
        }
        //获取用户的地址
        Location userLocation = null;
        try {
            userLocation = GaoDeMapUtil.getLngAndLag(userFindShop.getAddress());
        } catch (UnsupportedEncodingException e) {
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, "地址非法");
        }
        //获取离用户最近的地址
        Location nearLatest = AddressDistanceComparator.findNearestAddress(userLocation, locations);
        //找到这个店铺并绑定
        Shop shop = shopService.findByAddress(nearLatest.getFormattedAddress());
        if(shop==null){
            return ResultGenerator.genErrorResult(NetCode.SHOP_INVALID, "无法找到商户");
        }
        //绑定商铺
        userFindShop.setShop_id(shop.getId());
        //绑定管理员
        userFindShop.setEmployee_id(shop.getAdmin_id());
        //设置当前的时间
        userFindShop.setCreateTime(System.currentTimeMillis());
        //寻主任务
        int rows =userFindShopService.add(userFindShop);
        if (rows != 1) {
            System.out.println(userFindShop);
            return ResultGenerator.genFailResult("添加失败");
        }
        return ResultGenerator.genSuccessResult(userFindShop);
    }
}



