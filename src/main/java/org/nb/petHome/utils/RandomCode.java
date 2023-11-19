package org.nb.petHome.utils;

import java.util.Random;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
public class RandomCode {
    public static String getCode() {
        Random random = new Random();
        int number = random.nextInt(1000000);
        String code = String.format("%06d", number);
        return "123456";
    }
}
