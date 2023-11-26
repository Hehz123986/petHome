package org.nb.petHome.entity;
import lombok.Data;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/20
 **/
@Data
public class Phone {
    private String os;
    private String processor;
    private double screenSize;
    private int battery;
    private String brand;

    public Phone(String os, String processor, double screenSize, int battery, String brand) {
        this.os = os;
        this.processor = processor;
        this.screenSize = screenSize;
        this.battery = battery;
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "os='" + os + '\'' +
                ", processor='" + processor + '\'' +
                ", screenSize=" + screenSize +
                ", battery=" + battery +
                ", brand='" + brand + '\'' +
                '}';
    }
}
