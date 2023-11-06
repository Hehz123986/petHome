package org.nb.petHome.utils;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/6
 **/
public class RegexUtil {
    public static boolean isPhoneValid(String phone){
        String regex = "^1[3456789]\\d{9}$";
        return phone.matches(regex);
    }
}
