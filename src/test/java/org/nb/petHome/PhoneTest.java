package org.nb.petHome;

import org.junit.Test;
import org.nb.petHome.entity.Phone;
import org.nb.petHome.entity.PhoneBuilder;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/20
 **/
public class PhoneTest {
    @Test
    public void test(){
        Phone phone = new PhoneBuilder()
                .setScreenSize(1080)
                //这里的setScreenSize等方法，其实是在给PhoneBuilder对象设置属性。
                // 只不过在最后的build方法里面，将PhoneBuilder对象的这些属性值，
                // 当作参数传递给Phone对象的构造器，以此初始化Phone对象。
                .setBrand("HuaWei")
                .build();
        System.out.println(phone);
    }
}
