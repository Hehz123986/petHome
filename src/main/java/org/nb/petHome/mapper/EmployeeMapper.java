package org.nb.petHome.mapper;

import org.apache.ibatis.annotations.*;
import org.nb.petHome.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@Mapper
@Repository
public interface EmployeeMapper {

    /*增加*/
    @Insert("insert into t_employee(username,email,phone,password,age,state,did)" +
            "values(#{username},#{email},#{phone},#{password},#{age},#{state},#{did})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(Employee employee);

    /*假删除*/
    @Delete("update  t_employee set state=1 where id=#{id}")
    void remove(Long id);

    /*修改*/
    @Update("update t_employee set " +
            " username=#{username}, email=#{email},phone=#{phone},password=#{password},age=#{age}" +
            " where id=#{id}")
    void update(Employee employee);



    /*查询在职*/
    @Select("select * from t_employee where state=0 ")
    List<Employee> findEmployees();

    /*根据id查员工*/
    @Select("select * from t_employee where id=#{id}")
    Employee findById(Long id);
     /*登录 */
    @Select("select * from t_employee where phone=#{phone} and password=#{password}")
    Employee login(String phone,String password);

}
