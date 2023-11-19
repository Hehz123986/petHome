package org.nb.petHome.controller;

import org.nb.petHome.common.ShopQuery;
import org.nb.petHome.common.Urls;

import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.Location;
import org.nb.petHome.entity.Shop;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.IShopService;
import org.nb.petHome.service.impl.EmployeeService;
import org.nb.petHome.service.impl.ShopService;
import org.nb.petHome.utils.GaoDeMapUtil;
import org.nb.petHome.utils.MD5Util;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/3
 **/
@RestController
public class ShopController {

    private ShopService shopService;
    private EmployeeService employeeService;

    @Autowired
    public ShopController(ShopService shopService, EmployeeService employeeService) {
        this.shopService = shopService;
        this.employeeService = employeeService;
    }

    //注册
    @PostMapping(value = Urls.SHOP_REGISTER_URL)
    public NetResult shopRegister(@RequestBody Shop shop) {
        if (StringUtil.isEmpty(shop.getName())) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_NAME_INVALID, "店铺名不能为空");
        }
        if (StringUtil.isEmpty(shop.getTel())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(shop.getAddress())) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_ADDRESS_INVALID, "地址名不能为空");
        }
        if (StringUtil.isEmpty(shop.getLogo())) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_LOGO_INVALID, "logo不能为空");
        }
        try {
            Location userLocation = GaoDeMapUtil.getLngAndLag(shop.getAddress());
            shop.setLocation(userLocation);
        } catch (UnsupportedEncodingException e) {
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, "非法地址");
        }
        if (shop.getAdmin() == null) {
            Employee employee = new Employee();
            employee.setId(0l);
            shop.setAdmin(employee);
        }
        shop.setRegisterTime(System.currentTimeMillis());
        int count = shopService.add(shop);
        if (count != 1) {
            return ResultGenerator.genFailResult("添加shop失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = Urls.SHOP_LIST_URL)
    public NetResult list() {
        return ResultGenerator.genSuccessResult(shopService.list());
    }

    //删除
    @PostMapping(value = Urls.SHOP_DELETE_URL)
    public NetResult delete(Long id) {
        try {
            shopService.remove(id);
            return ResultGenerator.genSuccessResult("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "删除失败！" + e.getMessage());
        }
    }

    //审核失败
    @PostMapping(value = Urls.SHOP_AUDIT_FAILURE_URL)
    public NetResult auditFailure(Long id) {
        try {
            shopService.auditFailure(id);
            return ResultGenerator.genSuccessResult("审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "error" + e.getMessage());
        }
    }

    //通过审核
    @GetMapping(value = Urls.SHOP_SUCCESS_AUDIT_URL)
    public NetResult successfulAudit(Long id) {
        try{
            shopService.successfulAudit(id);
            Shop shop = shopService.findById(id);
            System.out.println(shop);
            System.out.println(shop.getTel());
            Employee employee = new Employee();
            //添加手机号
            employee.setPhone(shop.getTel());
            //插入默认的密码123456
            employee.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
            employeeService.add(employee);
            Employee e = employeeService.login(employee.getPhone(), employee.getPassword());
            shopService.addAdmin(shop, e.getId());
            return ResultGenerator.genSuccessResult("审核成功");
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR, "error" + e.getMessage());
        }

    }

    //修改
    @PostMapping(value = Urls.SHOP_UPDATE_URL)
    public NetResult shopUpdate(@RequestBody Shop shop) {
        try {
            if (StringUtil.isEmpty(shop.getName())) {
                return ResultGenerator.genErrorResult(NetCode.SHOP_NAME_INVALID, "店铺名不能为空");
            }
            if (StringUtil.isEmpty(shop.getTel())) {
                return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
            }
            if (StringUtil.isEmpty(shop.getAddress())) {
                return ResultGenerator.genErrorResult(NetCode.SHOP_ADDRESS_INVALID, "地址名不能为空");
            }
            shopService.update(shop);
            return ResultGenerator.genSuccessResult(shop);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR, "修改失败！" + e.getMessage());
        }
    }

    //分页
    @PostMapping(value = Urls.SHOP_PAGING_URL)
    public NetResult paging(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        int count = shopService.count();
        int offset = (page - 1) * pageSize;
        List<Shop> shops = shopService.paging(offset, pageSize);
        ShopQuery shopQuery = new ShopQuery();
        shopQuery.total = count;
        shopQuery.shops = shops;
        return ResultGenerator.genSuccessResult(shopQuery);
    }
}
