package org.nb.petHome;

import org.junit.Test;
import org.nb.petHome.factory.Shape;
import org.nb.petHome.factory.ShapeFactory;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/20
 **/
public class FactoryPatternDemo {

    @Test
    public  void test() {
        ShapeFactory shapeFactory = new ShapeFactory();

        //获取 Circle 的对象，并调用它的 draw 方法
        Shape shape1 = shapeFactory.getShape("CIRCLE");

        //调用 Circle 的 draw 方法
        shape1.draw();

        //获取 Rectangle 的对象，并调用它的 draw 方法
        Shape shape2 = shapeFactory.getShape("RECTANGLE");

        //调用 Rectangle 的 draw 方法
        shape2.draw();

        //获取 Square 的对象，并调用它的 draw 方法
        Shape shape3 = shapeFactory.getShape("SQUARE");

        //调用 Square 的 draw 方法
        shape3.draw();
    }
}
