package org.nb.petHome.net;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/11/1
 **/
public class NetCode {
    /**
     * 创建部门失败
     */
    public static final int CREATE_DEPARTMENT_ERROR = 0x10;

    /**
     * 移除部门失败
     */
    public static final int REMOVE_DEPARTMENT_ERROR = 0x11;

    /**
     * 更新部门失败
     */
    public static final int UPDATE_DEPARTMENT_ERROR = 0x12;
    /**
     * 手机号不能为空
     */

    public static final int PHONE_INVALID = 0x13;
    /**
     * 用户名不能为空
     */
    public static final int USERNAME_INVALID = 0x14;
    /**
     * 邮箱不能为空
     */
    public static final int EMAIL_INVALID = 0x15;
    public static final int DEPARTMENT_ID_INVALID = 0x16;
    /**
     * 无效的店铺名
     */
    public static final int SHOP_NAME_INVALID = 0x17;
    /**
     * 无效的地址
     */
    public static final int SHOP_ADDRESS_INVALID = 0x18;
    public static final int SHOP_LOGO_INVALID = 0x19;

    public static final int PASSWORD_INVALID =0x20 ;
    /**
     * token错误，非法请求
     */
    public static final int TOKEN_NOT_EXIST =0x21 ;

    /**
     * token失效，过期
     */
    public static final int TOKEN_INVALID =0x22 ;
    /**
     * 无效的年龄
     */
    public static final int AGE_INVALID = 0x23;
    /**
     * 无效的宠物名
     */
    public static final int PET_NAME_INVALID = 0x24;
    /**
     * 无效的地址
     */
    public static final int ADDRESS_INVALID =0x25 ;
    /**
     * 无效的年龄
     */
    public static final int BIRTH_INVALID =0x26 ;
    /**
     * 无效的ID
     */
    public static final int ID_INVALID =0x27 ;
    /**
     * 无效的性别
     */
    public static final int PET_SEX_INVALID =0x28 ;
    public static final int PET_IS_INOCULATION_INVALID =0x29 ;



    public static int PET_Process_ERROR=0x30;
    public static final int USERLIST_IS_NULL =0x31 ;
    public static final int SHOP_IS_NULL =0x32;
    public static final int SHOP_INVALID =0x33 ;
    public static final int SELL_PRICE_INVALID =0x34;
    public static final int STATE_NOT_EXIST =0x35 ;
}
