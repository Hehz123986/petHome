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
    public static final int USERNAME_INVALID =0x14 ;
    /**
     * 邮箱不能为空
     */
    public static final int EMAIL_INVALID =0x15 ;
    public static final int DEPARTMENT_ID_INVALID =0x15 ;
}
