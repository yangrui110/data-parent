package com.yang.system.support.service.impl;

import com.yang.system.client.entity.UserRole;
import com.yang.system.support.dao.UserRoleDao;
import com.yang.system.support.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色对应表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

}
