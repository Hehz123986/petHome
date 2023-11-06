package org.nb.petHome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void setRedis() {
        //缓存中最常用的方法
        redisTemplate.opsForValue().set("first","siwei");
        //设置缓存过期时间为30   单位：秒　　　　
        redisTemplate.opsForValue().set("second","siweiWu",30, TimeUnit.SECONDS);
        System.out.println("存入缓存成功");
    }

    @Test
    public void getRedis(){
        String first = redisTemplate.opsForValue().get("first");
        String second = redisTemplate.opsForValue().get("second");
        System.out.println("取出缓存中first的数据是:"+first);
        System.out.println("取出缓存中second的数据是:"+second);
    }

    /**
     * 根据key 删除缓存
     */
    @Test
    public void delRedis() {
        //根据key删除缓存
        Boolean first = redisTemplate.delete("first");
        System.out.println("是否删除成功:"+first);
    }
}
