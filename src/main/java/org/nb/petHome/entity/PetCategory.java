package org.nb.petHome.entity;

import lombok.Data;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/13
 **/
@Data
public class PetCategory {
    //id
    private Long id;
    //宠物类别
    private String petType;
    //描述
    private String description;
}
