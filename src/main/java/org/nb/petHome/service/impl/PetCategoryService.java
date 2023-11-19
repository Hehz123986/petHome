package org.nb.petHome.service.impl;

import org.nb.petHome.entity.PetCategory;
import org.nb.petHome.mapper.DepartmentMapper;
import org.nb.petHome.mapper.EmployeeMapper;
import org.nb.petHome.mapper.PetCategoryMapper;
import org.nb.petHome.service.IPetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Service
public class PetCategoryService implements IPetCategoryService {
    private PetCategoryMapper petCategoryMapper;

    @Autowired
    public PetCategoryService(PetCategoryMapper petCategoryMapper){
        this.petCategoryMapper = petCategoryMapper;
    }
    @Override
    public int add(PetCategory petCategory) {
        return petCategoryMapper.add(petCategory);
    }

    @Override
    public List<PetCategory> list() {
        return petCategoryMapper.list();
    }

    @Override
    public PetCategory findById(Long id) {
        return petCategoryMapper.findById(id);
    }
}
