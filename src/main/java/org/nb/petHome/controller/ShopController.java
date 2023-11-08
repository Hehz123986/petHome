package org.nb.petHome.controller;

import org.nb.petHome.common.ShopQuery;
import org.nb.petHome.common.Urls;
import org.nb.petHome.entity.Department;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.Shop;
import org.nb.petHome.net.NetCode;
import org.nb.petHome.net.NetResult;
import org.nb.petHome.service.IDepartmentService;
import org.nb.petHome.service.IEmployeeService;
import org.nb.petHome.service.IShopService;
import org.nb.petHome.utils.MD5Util;
import org.nb.petHome.utils.ResultGenerator;
import org.nb.petHome.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/3
 **/
@RestController
public class ShopController {

    private IShopService iShopService;

@Autowired
    public ShopController(IShopService iShopService) {
        this.iShopService = iShopService;
    }

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
        if (shop.getAdmin() == null) {
            Employee employee = new Employee();
            employee.setId(0l);
            shop.setAdmin(employee);
        }
        shop.setRegisterTime(System.currentTimeMillis());
        int count = iShopService.add(shop);
        if (count != 1) {
            return ResultGenerator.genFailResult("添加shop失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = Urls.SHOP_LIST_URL)
    public NetResult list() {
        return ResultGenerator.genSuccessResult(iShopService.list());
    }

    @PostMapping(value = Urls.SHOP_DELETE_URL)
    public NetResult delete(Long id) {
        try {
            iShopService.remove(id);
            return ResultGenerator.genSuccessResult("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "删除失败！" + e.getMessage());
        }
    }

    @PostMapping(value = Urls.SHOP_AUDIT_FAILURE_URL)
    public NetResult auditFailure(Long id) {
        try {
            iShopService.auditFailure(id);
            return ResultGenerator.genSuccessResult("审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "error" + e.getMessage());
        }
    }

    @PostMapping(value = Urls.SHOP_SUCCESS_AUDIT_URL)
    public NetResult successfulAudit(Long id) {
        try {
            iShopService.successfulAudit(id);
            return ResultGenerator.genSuccessResult("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "error！" + e.getMessage());
        }
    }


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
            iShopService.update(shop);
            return ResultGenerator.genSuccessResult(shop);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR, "修改失败！" + e.getMessage());
        }
    }
    @PostMapping(value = Urls.SHOP_PAGING_URL)
    public NetResult paging(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        int count = iShopService.count();
        int offset = (page-1) * pageSize;
        List<Shop> shops = iShopService.paging(offset,pageSize);
        ShopQuery shopQuery=new ShopQuery();
        shopQuery.total=count;
        shopQuery.shops=shops;
        return ResultGenerator.genSuccessResult(shopQuery);
    }
}
