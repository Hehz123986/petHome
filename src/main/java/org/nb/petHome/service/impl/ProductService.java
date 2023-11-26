package org.nb.petHome.service.impl;

import org.apache.ibatis.annotations.Param;
import org.nb.petHome.entity.Product;
import org.nb.petHome.mapper.PetShopMapper;
import org.nb.petHome.mapper.ProductMapper;
import org.nb.petHome.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/26
 **/
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class ProductService implements IProductService {
    private ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }
    @Transactional
    @Override
    public void offProduct(Long id, Long offSaleTime) {
        productMapper.offProduct(id, offSaleTime);
    }

    @Transactional
    @Override
    public void onProduct(Long id, Long onSaleTime) {
        productMapper.onProduct(id, onSaleTime);
    }

    @Transactional
    @Override
    public List<Product> findProductByState(int offset, int pageSize) {
        return productMapper.findProductByState(offset, pageSize);
    }

    @Transactional
    @Override
    public int count() {
        return productMapper.count();
    }

    @Transactional
    @Override
    public Product findProductById(Long id) {
        return productMapper.findProductById(id);
    }

    @Transactional
    @Override
    public  void updateCount(@Param("id")Long id,@Param("saleCount")int saleCount) {
        productMapper.updateCount(id,saleCount);
    }
}
