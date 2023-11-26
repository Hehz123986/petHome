package org.nb.petHome.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.nb.petHome.entity.Product;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/26
 **/
public interface IProductService {


    void offProduct(Long id, Long offSaleTime);

    void onProduct(Long id, Long onSaleTime);

    List<Product> findProductByState(@Param("offset") int offset, @Param("pageSize") int pageSize);

    int count();

    Product findProductById(Long id);

    void updateCount(@Param("id")Long id,@Param("saleCount")int saleCount);

}
