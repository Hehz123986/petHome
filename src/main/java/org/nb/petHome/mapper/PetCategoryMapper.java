package org.nb.petHome.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.nb.petHome.entity.PetCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
@Mapper
@Repository
public interface PetCategoryMapper {

    @Insert("insert into PetCategory(petType,description) values(#{petType},#{description})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(PetCategory petCategory);


    @Select("select * from PetCategory")
    List<PetCategory> list();

    @Select("select * from PetCategory where id=#{id}")
    PetCategory findById(Long id);
}
