package org.nb.petHome.mapper;

import org.apache.ibatis.annotations.*;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/9
 **/
@Mapper
@Repository
public interface UserMapper {

    /*增加*/
    @Insert("insert into t_user (username, email, phone, password, age, createtime) values(#{username},#{email},#{phone},#{password},#{age},#{createtime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int register(User user);

    @Select("select * from t_user where phone=#{phone}")
    User findByPhone(String phone);

    @Select("select * from t_user where phone=#{phone} and password=#{password}")
    User userLogin(String phone,String password);

    @Select("select * from t_user where id=#{id}")
    User findById(Long id);

    @Update("update t_user set product_id=#{product_id} , price=#{price} where id=#{id} ")
    void update(double price,Long product_id,Long id);

}
