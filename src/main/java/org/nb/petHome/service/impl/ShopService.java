package org.nb.petHome.service.impl;

import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.Shop;
import org.nb.petHome.mapper.DepartmentMapper;
import org.nb.petHome.mapper.EmployeeMapper;
import org.nb.petHome.mapper.ShopMapper;
import org.nb.petHome.service.IShopService;
import org.nb.petHome.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/3
 **/
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class ShopService implements IShopService {
    private ShopMapper shopMapper;
    private EmployeeMapper employeeMapper;

    @Autowired
    public ShopService(ShopMapper shopMapper, EmployeeMapper employeeMapper) {
        this.shopMapper = shopMapper;
        this.employeeMapper = employeeMapper;

    }

    @Override
    public int add(Shop shop) {
        return shopMapper.add(shop);
    }

    @Override
    public List<Shop> list() {
        return shopMapper.list();
    }

    @Override
    public void remove(Long id) {
        shopMapper.remove(id);
    }

    @Override
    public void successfulAudit(Long id) {
       /* Employee employee = new Employee();
        Shop shop = shopMapper.findById(id);
        System.out.println(shop);
        System.out.println(shop.getTel());
        //添加手机号
        employee.setPhone(shop.getTel());
        //插入默认的密码123456
        employee.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        employeeMapper.add(employee);
        Employee e = employeeMapper.login(employee.getPhone(), employee.getPassword());
        shopMapper.addAdmin(shop, e.getId());*/
        shopMapper.successfulAudit(id);
    }

    @Override
    public void auditFailure(Long id) {
        shopMapper.auditFailure(id);
    }

    @Override
    public void update(Shop shop) {
        shopMapper.update(shop);
    }

    @Override
    public List<Shop> paging(int offset, int pageSize) {
        return shopMapper.paging(offset, pageSize);
    }

    @Override
    public int count() {
        return shopMapper.count();
    }

    @Override
    public Shop findByAddress(String address) {
        return shopMapper.findByAddress(address);
    }

    @Override
    public void addAdmin(Shop shop, long id) {
        shopMapper.addAdmin(shop, id);
    }

    @Override
    public Shop findById(Long id) {
        return shopMapper.findById(id);
    }
}
