package org.nb.petHome.controller;

import org.nb.petHome.common.Constants;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.PetShop;
import org.nb.petHome.entity.User;
import org.nb.petHome.entity.UserFindShop;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.impl.PetShopService;
import org.nb.petHome.service.impl.UserFindShopService;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/16
 **/
@RestController
public class PetShopController {

    private PetShopService petShopService;
    private UserFindShopService userFindShopService;
    private RedisTemplate redisTemplate;

    private Logger logger = LoggerFactory.getLogger(PetShopController.class);


    public PetShopController(PetShopService petShopService, UserFindShopService userFindShopService, RedisTemplate redisTemplate) {
        this.petShopService = petShopService;
        this.userFindShopService = userFindShopService;
        this.redisTemplate = redisTemplate;
    }


    //添加宠物商品
    @PostMapping("/addPetShop")
    private NetResult addPetShop(@RequestBody PetShop petShop, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断user的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //寻主id
        long userFindShop_id = petShop.getUserFindShop_id();
        //判断是否寻主
        if (userFindShopService.findById(userFindShop_id) == null) {
            return ResultGenerator.genFailResult("没有寻主");
        }
        //判断是否已经上架过
        if (petShopService.findByUserFindShop_id(userFindShop_id) != null) {
            return ResultGenerator.genFailResult("已有该商品");
        }
        //找到寻主历史
        UserFindShop userFindShop = userFindShopService.findById(userFindShop_id);
        //从寻主历史中找到成本价
        double costPrice = userFindShop.getPrice();
        //售价
        double sellPrice = petShop.getSellPrice();
        //售价异常或售价低于成本
        if (sellPrice < 0 || sellPrice < costPrice) {
            return ResultGenerator.genErrorResult(NetCode.SELL_PRICE_INVALID, "售价小于0或者售价小于成本价");
        }
        //设置宠物名
        petShop.setName(userFindShop.getName());
        //设置商铺id
        petShop.setShop_id(userFindShop.getShop_id());
        //设置用户id
        petShop.setUser_id(userFindShop.getUser_id());
        //设置管理员id
        petShop.setEmployee_id(userFindShop.getEmployee_id());
        //设置类别id
        petShop.setUserFindShop_id(userFindShop.getPetCategory_id());
        //设置成本价格
        petShop.setCostPrice(costPrice);
        //设置售价
        petShop.setSellPrice(sellPrice);
        //添加商品
        int rows = petShopService.add(petShop);
        if (rows == 1) {
            return ResultGenerator.genSuccessResult(petShop);
        }
        return ResultGenerator.genFailResult("添加失败");
    }


    //上架宠物商品
    @GetMapping("/shelfGoods")
    private NetResult shelfGoods(long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断user的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        System.out.println(id);
        try {
            System.out.println(6);
            //上架商品，当前时间就是售卖时间，同时把state改为1,同时设置未被领养
            Long saleStartTime = System.currentTimeMillis();
            petShopService.updateState(id, saleStartTime);
            return ResultGenerator.genSuccessResult("上架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }

    }

    //领养宠物(用户登录)
    @GetMapping("/adoptPet")
    private NetResult adoptPet(long id, HttpServletRequest request) {
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
        //已经被领养
        if (petShopService.findPetShopById(id).getAdopt() == 0) {
            return ResultGenerator.genFailResult("该宠物已被领养");
        }
        //下架了或者还未上架
        if (petShopService.findPetShopById(id).getState() == 1) {
            return ResultGenerator.genFailResult("该宠物还没上架");
        }
        if (petShopService.findPetShopById(id) == null) {
            return ResultGenerator.genFailResult("没有该宠物");
        }
        try {
            //设置为已领养，然后当时时间就为结束售卖时间,同时讲user_id设置进去
            Long endTime = System.currentTimeMillis();
            petShopService.adoptPet(id, endTime, user.getId());
            return ResultGenerator.genSuccessResult("领养成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }

    }


    //下架宠物商品
    @GetMapping("/delist")
    private NetResult delist(long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断user的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        try {
            //下架商品，把state改为1。
            petShopService.delistPet(id);
            return ResultGenerator.genSuccessResult("下架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }

    //查询自己的宠物
    @GetMapping("/selectPetByUser")
    private NetResult selectPetByUser(HttpServletRequest request) {
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
        try {
            PetShop petShop = petShopService.findPetByUser(user.getId());
            return ResultGenerator.genSuccessResult(petShop);
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }


    //是否上架(用户)
    @GetMapping("/shelfStatusByUser")
    private NetResult ShelfStatusByUser(HttpServletRequest request,int state) {
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
        if(state==0){
            return ResultGenerator.genFailResult("未上架");
        }
        if(state==1){
            List<PetShop> petShopList=petShopService.findPetShopByState(state,user.getId());
            return ResultGenerator.genSuccessResult(petShopList);
        }
        return ResultGenerator.genFailResult("状态错误");
    }


}
