package org.nb.petHome.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeMapperTest {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void addTest() {
        Employee employee = new Employee();
        employee.setAge(12);
        employee.setEmail("123@qq.com");
        employee.setUsername("hhh");
        employee.setPhone("15678954578");
        employee.setPassword("123456");
        employee.setDid(1l);
        employee.setState(0);
        employeeMapper.add(employee);
        System.out.println(employee);
    }


    @Test
    public void deleteTest() {
        employeeMapper.remove(316l);
    }


    @Test
    public void updateTest() {
        Employee e = new Employee();
        e.setId(2L);
        e.setUsername("wu");
        e.setEmail("908@qq.com");
        e.setPhone("123456");
        e.setPassword("123456");
        e.setAge(27);
        e.setDid(13L);
        System.out.println(1);
    }





}
