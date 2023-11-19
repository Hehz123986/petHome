package org.nb.petHome.service.impl;

import org.nb.petHome.entity.PetShop;
import org.nb.petHome.mapper.PetCategoryMapper;
import org.nb.petHome.mapper.PetShopMapper;
import org.nb.petHome.service.IPetShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/16
 **/
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Service
public class PetShopService implements IPetShopService {
    private PetShopMapper petShopMapper;

    @Autowired
    public PetShopService(PetShopMapper petShopMapper){
        this.petShopMapper = petShopMapper;
    }

    @Override
    public PetShop findByUserFindShop_id(Long userFindShop_id) {
        return petShopMapper.findByUserFindShop_id(userFindShop_id);
    }

    @Override
    public int add(PetShop petShop) {
        return petShopMapper.add(petShop);
    }

    @Override
    public void updateState(Long id,Long saleStartTime) {
         petShopMapper.updateState(id,saleStartTime);
    }

    @Override
    public PetShop findPetShopById(Long id) {
        return petShopMapper.findPetShopById(id);
    }

    @Override
    public void adoptPet(Long id, Long endTime,Long user_id) {
        petShopMapper.adoptPet(id,endTime,user_id);
    }

    @Override
    public void delistPet(Long id) {
        petShopMapper.delistPet(id);
    }

    @Override
    public PetShop findPetByUser(Long user_id) {
       return petShopMapper.findPetByUser(user_id);
    }

    @Override
    public List<PetShop> findPetShopByState(int state, long user_id) {
        return petShopMapper.findPetShopByState(state,user_id);
    }
}
