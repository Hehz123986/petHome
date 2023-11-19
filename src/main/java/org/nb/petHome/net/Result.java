package org.nb.petHome.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/18
 **/
public class Result {
    public static JSONObject StringToJson(String s){
        return JSON.parseObject(s);
    }
}
