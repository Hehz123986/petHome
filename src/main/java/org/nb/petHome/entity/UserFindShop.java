package org.nb.petHome.entity;

import lombok.Data;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/12
 **/
@Data
public class UserFindShop {
    private Long id;
    private String name;
    private int sex;//0是母的 1是公的
    private String address;
    private Long createTime;
    private double price;
    private int birth;
    //0是未接种 1接种
    private int isInoculate;
    private Long shop_id;
    private Long employee_id;
    private Long petCategory_id;
    //0是上架 1是未上架
    private int state;
    private Long user_id;

    private Shop shop;
    private Employee admin;
    private User user;
    private PetCategory petCategory;


}
