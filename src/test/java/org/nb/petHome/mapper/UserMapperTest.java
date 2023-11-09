package org.nb.petHome.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/9
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void testFindByPhone(){
        userMapper.findByPhone("1");
    }



}
