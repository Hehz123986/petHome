package org.nb.petHome.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.PetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class PetCategoryMapperTest {
    @Autowired
    private PetCategoryMapper petCategoryMapper;

    @Test
    public void addTest() {
        PetCategory petCategory = new PetCategory();
        petCategory.setPetType("狗");
        petCategory.setDescription("身子圆滚滚,胖嘟嘟的");
        petCategoryMapper.add(petCategory);
    }

    @Test
    public void listTest() {
        System.out.println(petCategoryMapper.list());
    }
}
