package org.nb.petHome.service;

import org.apache.ibatis.annotations.Param;
import org.nb.petHome.entity.*;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
public interface IUserFindShopService {
    int add(UserFindShop userFindShop);

    List<UserFindShop> findByState(int state);

    List<UserFindShop> findUserList(long userId);

    List<Long> findAllShopId();

    List<UserFindShop> getShopList(long shopId);

    int  updateState(int state,Long id);

    UserFindShop findById(long id);
  }
