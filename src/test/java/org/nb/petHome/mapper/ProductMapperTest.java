package org.nb.petHome.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.Product;
import org.nb.petHome.entity.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/26
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void addTest() {
        Product product = new Product();
        product.setName("豪华洗澡");
        product.setSalePrice(88.9);
        product.setOffSaleTime(System.currentTimeMillis());
        product.setOnSaleTime(System.currentTimeMillis());
        product.setState(0);
        product.setCostPrice(22.1);
        product.setCreateTime(System.currentTimeMillis());
        product.setSaleCount(9);
        productMapper.add(product);
        System.out.println(product);
    }
}
