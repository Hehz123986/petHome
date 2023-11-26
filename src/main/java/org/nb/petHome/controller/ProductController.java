package org.nb.petHome.controller;

import org.apache.ibatis.annotations.Param;
import org.nb.petHome.common.Constants;
import org.nb.petHome.common.ProductQuery;
import org.nb.petHome.common.ShopQuery;
import org.nb.petHome.common.Urls;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.Product;
import org.nb.petHome.entity.Shop;
import org.nb.petHome.entity.User;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.impl.PetShopService;
import org.nb.petHome.service.impl.ProductService;
import org.nb.petHome.service.impl.UserFindShopService;
import org.nb.petHome.service.impl.UserService;
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
 * @data: 2023/11/26
 **/
@RestController
public class ProductController {

    private ProductService productService;
    private RedisTemplate redisTemplate;
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(ProductController.class);


    public ProductController(ProductService productService, RedisTemplate redisTemplate, UserService userService) {
        this.productService = productService;
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }

    //上架
    @GetMapping("/onProduct")
    private NetResult onProduct(long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断employee的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        try {
            //上架，修改状态，修改上架时间
            Long onSaleTime = System.currentTimeMillis();
            productService.onProduct(id, onSaleTime);
            return ResultGenerator.genSuccessResult("上架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }

    //下架
    @GetMapping("/offProduct")
    private NetResult offProduct(long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断employee的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        try {
            //下架，修改状态，修改下架时间
            Long offSaleTime = System.currentTimeMillis();
            productService.offProduct(id, offSaleTime);
            return ResultGenerator.genSuccessResult("下架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }


    //上下架商品列表
    @GetMapping("/productList")
    public NetResult paging(HttpServletRequest request, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断employee的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        int count = productService.count();
        int offset = (page - 1) * pageSize;
        List<Product> products = productService.findProductByState(offset, pageSize);
        ProductQuery productQuery = new ProductQuery();
        productQuery.total = count;
        productQuery.products = products;
        return ResultGenerator.genSuccessResult(productQuery);
    }

    //购买
    @GetMapping("/buy")
    public NetResult buy(HttpServletRequest request, @Param("id") Long id, @Param("count") int count) {
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
        //通过id找到对应的服务商品
        Product product = productService.findProductById(id);
        int state = product.getState();
        if (state == 0) {
            return ResultGenerator.genErrorResult(NetCode.STATE_INVALID, "该服务已经下架");
        }
        //获取商品的售价
        double salePrice = product.getSalePrice();
        //获取用户余额
        double price = user.getPrice();
        //余额过少
        if (price < salePrice * count) {
            return ResultGenerator.genErrorResult(NetCode.Price_INVALID, "用户余额不足");
        }
        //获取商品的库存
        int saleCount = product.getSaleCount();
        if (count > saleCount) {
            return ResultGenerator.genErrorResult(NetCode.Count_INVALID, "库存不够");
        }
        //修改库存
        System.out.println(id);
        System.out.println(saleCount);
        System.out.println(saleCount - count);
        productService.updateCount(id, saleCount - count);
        //插入product_id，和修改余额
        System.out.println(id);
        System.out.println(user.getId());
        System.out.println(price - salePrice * count);
        double price1=price - salePrice * count;
        userService.update(price1, id, user.getId());
        return ResultGenerator.genSuccessResult("购买成功");
    }


}
