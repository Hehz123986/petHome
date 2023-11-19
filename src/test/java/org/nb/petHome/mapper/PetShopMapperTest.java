package org.nb.petHome.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.PetShop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/18
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PetShopMapperTest {
    @Autowired
    private PetShopMapper petShopMapper;

    @Test
    public void addTest() {
        PetShop petShop = new PetShop();
        petShop.setShop_id(1l);
        petShop.setName("j");
        petShop.setCostPrice(90.0);
        petShop.setSellPrice(100.0);
        petShop.setUser_id(1l);
        petShop.setEmployee_id(2l);
        petShop.setUserFindShop_id(1l);
        petShopMapper.add(petShop);
    }

}
