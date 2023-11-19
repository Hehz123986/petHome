package org.nb.petHome.entity;

import lombok.Data;
/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/3
 **/
@Data
public class Shop {
    private Long id;
    private String name;
    private String tel;
    private long registerTime;
    private int state=0;//state为0代表未审核
    private String address;
    private  String logo;
    private Employee admin;
    private Long admin_id;
    private Location location;



}
