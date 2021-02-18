package com.yang.system.client.vo;

import com.yang.system.client.entity.Api;
import com.yang.system.client.entity.Role;
import com.yang.system.client.entity.User;
import lombok.Data;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/18
 * @Auth yangrui
 **/
@Data
public class UserVo extends User {

    private List<Role> roles;

    private List<Api> apis;
}
