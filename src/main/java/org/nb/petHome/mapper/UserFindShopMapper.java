package org.nb.petHome.mapper;

import org.apache.ibatis.annotations.*;
import org.nb.petHome.entity.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/12
 **/
@Mapper
@Repository
public interface UserFindShopMapper {
    /*增加*/
    @Insert("insert into user_msg(name,sex,address, createTime,birth,state,price,isInoculate,user_id,employee_id,shop_id,petCategory_id)" +
            "values(#{name},#{sex},#{address},#{createTime},#{birth},#{state},#{price},#{isInoculate},#{user_id},#{employee_id},#{shop_id},#{petCategory_id})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(UserFindShop userFindShop);


    @Select("select * from user_msg where state=#{state}")
    List<UserFindShop> findByState(int state);

    @Select("select * from user_msg  where user_id=#{userId} ")
    List<UserFindShop> findUserList(long userId);

    @Select("select  shop_id from user_msg")
    List<Long> findAllShopId();

    @Select("select * from user_msg where shop_id=#{shopId}")
    List<UserFindShop> getShopList(long shopId);
    @Update("update user_msg set state=#{state} where id=#{id}")
   int  updateState(int state,Long id);

    @Select("select * from user_msg  where id=#{id} ")
    UserFindShop findById(long id);

}
