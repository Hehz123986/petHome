/*
package org.nb.petHome;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

*
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/2
 *

public class RedisTest {
    @Test
    public void test() {
        Jedis jedis = new Jedis("localhost");
        //获取redis中的所有键
        Set<String> keys = jedis.keys("*");
        for(String s : keys){
            System.out.println(s);
        }
        //删除redis中的所有键
     //   jedis.flushDB();
        //判断一个键是否存在
     //   System.out.println(jedis.exists("myKey"));
        //返回值得数据类型
      //  System.out.println(jedis.type("runoobkey"));
      //  System.out.println(jedis.type("myKey"));
    }


    @Test
    public void test1() {

        Map<String, String> hash = jedis.hgetAll("myHash");
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Test
    public void test2() {
        Jedis jedis = new Jedis("localhost");
        // 添加元素到List中
 jedis.lpush("mylist", "java");
        jedis.lpush("mylist", "python");
        jedis.lpush("mylist", "java");
        jedis.lpush("mylist", "golang");

        // 查询指令位置
     //   Long position = jedis.lpos("foo", "list1");
        // 输出查询结果
        System.out.println();

    }


}
*/
