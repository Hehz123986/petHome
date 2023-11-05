package org.nb.petHome.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.Employee;
import org.nb.petHome.service.impl.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void updateTest() {
     Employee employee=  employeeService.findById(323l);
        System.out.println(employee);
     employee.setUsername("hhhhh");
     employeeService.update(employee);
    }

}
