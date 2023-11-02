package org.nb.petHome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
@SpringBootApplication
@MapperScan("org.nb.petHome.mapper")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
