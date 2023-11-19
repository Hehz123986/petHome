package org.nb.petHome.service.impl;

import org.nb.petHome.entity.*;
import org.nb.petHome.mapper.UserFindShopMapper;

import org.nb.petHome.service.IUserFindShopService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class UserFindShopService implements IUserFindShopService {
    private UserFindShopMapper userFindShopMapper;

    @Autowired
    public UserFindShopService(UserFindShopMapper userFindShopMapper) {
        this.userFindShopMapper = userFindShopMapper;
    }

    @Override
    public int add(UserFindShop userFindShop) {
        return userFindShopMapper.add(userFindShop);
    }

    @Override
    public List<UserFindShop> findByState(int state) {
        return userFindShopMapper.findByState(state);
    }

    @Override
    public List<UserFindShop> findUserList(long userId) {
        return userFindShopMapper.findUserList(userId);
    }

    @Override
    public List<Long> findAllShopId() {
        return userFindShopMapper.findAllShopId();
    }

    @Override
    public List<UserFindShop> getShopList(long shopId) {
        return userFindShopMapper.getShopList(shopId);
    }

    @Override
    public int updateState(int state, Long id) {
        return userFindShopMapper.updateState(state, id);
    }

    @Override
    public UserFindShop findById(long id) {
        return userFindShopMapper.findById(id);
    }
}
