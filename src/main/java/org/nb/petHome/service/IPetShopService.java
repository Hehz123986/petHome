package org.nb.petHome.service;

import org.nb.petHome.entity.PetShop;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/16
 **/
public interface IPetShopService {


    PetShop findByUserFindShop_id(Long userFindShop_id);

    int add(PetShop petShop);

    void updateState(Long id,Long saleStartTime);

    PetShop findPetShopById(Long id);

    void adoptPet(Long id,Long endTime,Long user_id);

    void delistPet(Long id);

    PetShop findPetByUser(Long user_id);

    List<PetShop> findPetShopByState(int state, long user_id);
}
