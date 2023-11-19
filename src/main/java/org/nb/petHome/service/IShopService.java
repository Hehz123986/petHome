package org.nb.petHome.service;

import org.apache.ibatis.annotations.Param;
import org.nb.petHome.entity.Shop;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/3
 **/
public interface IShopService {
    int add(Shop shop);
    List<Shop> list();
    void remove(Long id);
    void successfulAudit(Long id);
    void auditFailure(Long id);
    void update(Shop shop);
    List<Shop> paging( int offset, int pageSize);
    int count();
    Shop findByAddress(String address);
    void addAdmin(Shop shop,long id);
    Shop  findById(Long id);
}
