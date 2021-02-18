package com.yang.system.client.po;

import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/17
 * @Auth yangrui
 **/
@Data
public class ModifyPassword {

    private String oldPassword;
    private String newsPassword;
    private Long userId;
}
