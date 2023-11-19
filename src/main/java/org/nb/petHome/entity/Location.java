package org.nb.petHome.entity;

import lombok.Data;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
@Data
public class Location {
    private String formattedAddress;
    private double longitude;//经度
    private double latitude;//维度

    public Location(String formattedAddress, double longitude, double latitude) {
        this.formattedAddress = formattedAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

