package org.nb.petHome.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nb.petHome.entity.User;
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
    public void testFindByPhone() {
        userMapper.findByPhone("1");
    }


    @Test
    public void testAdd() {
        User user = new User();
        user.setPassword("0");
        user.setUsername("李四");
        user.setPhone("123");
        //user.setState(0);
        user.setAge(11);
        user.setEmail("123ggg");
        user.setCreatetime(System.currentTimeMillis());
        userMapper.register(user);
    }

    @Test
    public void test() {
        User user=new User();
        user.setPhone("123");
        user.setPassword("2");
        System.out.println(userMapper.userLogin("123","2"));
    }


}
