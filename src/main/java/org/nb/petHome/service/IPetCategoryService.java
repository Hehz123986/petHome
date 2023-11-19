package org.nb.petHome.service;

import org.apache.ibatis.annotations.Select;
import org.nb.petHome.entity.PetCategory;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
public interface IPetCategoryService {

    int add(PetCategory petCategory);

    List<PetCategory> list();

    PetCategory findById(Long id);
}
