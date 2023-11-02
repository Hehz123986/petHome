package org.nb.petHome.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.Department;
import org.nb.petHome.service.impl.DepartmentService;

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
public class DepartmentServiceTest {


    @Autowired
    private DepartmentService departmentService;

    @Test
    public void testTree() throws JsonProcessingException {
        List<Department> departmentList = departmentService.getDepartmentTreeData();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(departmentList);
        System.out.println(json);
    }

    @Test
    public void testOne(){
        Department department=departmentService.find(11l);
        System.out.println(department);
    }
}

