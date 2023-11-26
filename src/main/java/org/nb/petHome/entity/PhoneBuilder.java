package org.nb.petHome.entity;

/**
 * @description:建造者模式
 * 使用多个对象一步一步构建成一个复杂的对象
 * 使用场景
 * 1.需要生成的对象具有复杂的内部结构
 * 2.需要生成的内部属性需要相互依赖
 *   建造者模式在复杂对象时非常有用，特别是当对象的建造过程设计多个步骤和参数时
 *   他可以提供更好的灵活性和可维护性，同时使得代码更加清晰可读
 * 优点
 * 1.分离构建过程和表示，使得构建过程更加灵活，可以构建不同的表示
 * 2.可以更好的控制构建过程，隐藏具体构建细节
 * 3.代码复用性高，可以在不同构建过程中使用相同的构建者
 * 缺点：
 * 1.如果产品的属性较少，构建者模式可能会导致代码沉余
 * 2.建造者模式增加了系统的类和对象的数量
 * @author: hzh
 * @data: 2023/11/20
 **/

public class PhoneBuilder {
    private String os;
    private String processor;
    private double screenSize;
    private int battery;
    private String brand;

    public PhoneBuilder setOs(String os) {
        this.os = os;
        return this;  //这里的return this，按我的理解就是返回设置了Os属性的PhoneBuilder对象。
    }

    public PhoneBuilder setProcessor(String processor) {
        this.processor = processor;
        return this;
    }

    public PhoneBuilder setScreenSize(double screenSize) {
        this.screenSize = screenSize;
        return this;
    }

    public PhoneBuilder setBattery(int battery) {
        this.battery = battery;
        return this;
    }

    public PhoneBuilder setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public Phone build(){
        //括号里面的参数os,processor,screenSize,battery,brand，其实对应的就是最上面的属性，
        // 即把PhoneBuilder的属性当作参数穿给Phone的构造器
        return new Phone(os,processor,screenSize,battery,brand);
    }
}
