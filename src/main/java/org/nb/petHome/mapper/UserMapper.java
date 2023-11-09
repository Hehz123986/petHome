package org.nb.petHome.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
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
    @Insert("insert into t_user (username, email, phone, salt, password, state, age, createtime, headImg)" +
            "values(#{username},#{email},#{phone},#{salt},#{password},#{state},#{age},#{createtime},#{headImg})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int register(User user);

    @Select("select * from t_user where phone=#{phone}")
    User findByPhone(String phone);

    @Select("select * from t_employee where phone=#{phone} and password=#{password}")
    User userLogin(User user);




}
