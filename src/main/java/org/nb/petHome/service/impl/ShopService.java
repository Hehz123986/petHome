package org.nb.petHome.service.impl;

import org.nb.petHome.entity.Shop;
import org.nb.petHome.mapper.DepartmentMapper;
import org.nb.petHome.mapper.EmployeeMapper;
import org.nb.petHome.mapper.ShopMapper;
import org.nb.petHome.service.IShopService;
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

    @Autowired
    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;

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
}
