package com.yang.system.support.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.*;
import com.yang.system.client.po.ModifyPassword;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuTreeVo;
import com.yang.system.client.vo.UserVo;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.constant.PermissionType;
import com.yang.system.support.dao.UserDao;
import com.yang.system.support.exception.InterException;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.service.*;
import com.yang.system.support.util.IdUtils;
import com.yang.system.support.util.RandomNumAndChar;
import com.yang.system.support.util.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private ButtonService buttonService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<MenuTreeVo> getUserMenus(String userId) {
        // 1、获取用户对应的角色
        List<UserRole> userRoles = userRoleService.list(Wrappers.query(new UserRole()).eq("user_id", userId));
        if(userRoles.size()==0){
            log.info("用户没有对应的角色信息");
            return new ArrayList<>();
        }
        List<Menu> menus = null;
        Optional<UserRole> any = userRoles.stream().filter(item -> 888929999 == item.getRoleId()).findAny();
        if(any.isPresent()){
            menus = menuService.list(Wrappers.query(new Menu()).eq("dr", DrStatus.NORMAL));
        }else {
            // 2、获取到角色对应的菜单信息
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<RolePermission> rolePermissions = rolePermissionService.list(Wrappers.query(new RolePermission()).eq("type", PermissionType.MENU).in("role_id", roleIds));
            if (rolePermissions.size() == 0) {
                log.info("角色无对应的菜单");
                return new ArrayList<>();
            }
            // 3、获取到菜单对应的详细信息
            List<Long> menuIds = rolePermissions.stream().filter(item -> item.getType() == PermissionType.MENU).map(RolePermission::getPermissionId).collect(Collectors.toList());
            menus = menuService.list(Wrappers.query(new Menu()).eq("dr", DrStatus.NORMAL).in("id", menuIds));
        }
        ArrayList<MenuTreeVo> arrayList = new ArrayList<>();
        for(Menu menu: menus){
            MenuTreeVo vo = new MenuTreeVo();
            BeanUtils.copyProperties(menu,vo);
            vo.setKey(menu.getId());
            vo.setTitle(menu.getMenuName());
            arrayList.add(vo);
        }
        return arrayList;
    }

    @Override
    public List<Role> getUserRoles(String userId) {
        // 1、获取用户对应的角色
        List<UserRole> userRoles = userRoleService.list(Wrappers.query(new UserRole()).eq("user_id", userId));
        if(userRoles.size()==0){
            log.info("用户没有对应的角色信息");
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> list = roleService.list(Wrappers.query(new Role()).in("id", roleIds).eq("dr", DrStatus.NORMAL));
        return list;
    }

    @Override
    public List<Api> getUserApis(String userId) {
        // 1、获取到用户对应的角色
        List<Role> userRoles = getUserRoles(userId);
        // 判断是否具有超级角色
        boolean exitSuper = false;
        for(Role role: userRoles){
            if(role.getRoleCode().equals("SUPER_ROLE")){
                exitSuper=true;
            }
        }
        List<Api> apis= null;
        if(exitSuper) {
            apis = apiService.list(Wrappers.query(new Api()).eq("dr", DrStatus.NORMAL));
        }else {
            List<Long> roleIds = userRoles.stream().map(Role::getId).collect(Collectors.toList());
            if (roleIds == null || roleIds.size() == 0) return new ArrayList<>();
            QueryWrapper<RolePermission> queryWrapper = Wrappers.query(new RolePermission())
                    .eq("dr", DrStatus.NORMAL)
                    .in("role_id", roleIds)
                    .eq("type", PermissionType.API);
            List<RolePermission> list = rolePermissionService.list(queryWrapper);
            List<Long> perIds = list.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
            if (perIds == null || perIds.size() == 0) return new ArrayList<>();
            // 2、获取api
            apis = apiService.list(Wrappers.query(new Api()).in("id", perIds).eq("dr", DrStatus.NORMAL));
        }
        return apis;
    }

    @Override
    public List<Api> getUserApisByRedis() {
        // 1、获取到用户
        UserVo userInfo = UserInfoUtil.getUserInfo(request, redisTemplate);
        List<Api> apis = userInfo.getApis();
        return apis;
    }

    @Override
    public List<Button> getUserButtons(String userId) {
        // 1、获取用户对应的角色
        List<UserRole> userRoles = userRoleService.list(Wrappers.query(new UserRole()).eq("user_id", userId));
        if(userRoles.size()==0){
            log.info("用户没有对应的角色信息");
            return new ArrayList<>();
        }
        // 2、获取到角色对应的按钮信息
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rolePermissions = rolePermissionService.list(Wrappers.query(new RolePermission()).eq("type", PermissionType.BUTTON).in("role_id", roleIds));
        if(rolePermissions.size()==0){
            log.info("角色无对应的菜单");
            return new ArrayList<>();
        }
        // 3、获取到菜单对应的详细信息
        List<Button> buttons = buttonService.list(Wrappers.query(new Button()).eq("dr", DrStatus.NORMAL));
        return buttons;
    }

    @Override
    public void addUser(User user) {
        user.setId(IdUtils.nextId());
        user.setDr(DrStatus.NORMAL);
        String numAndChar = RandomNumAndChar.getNumAndChar(4);
        user.setSalt(numAndChar);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 设置密码为MD5加密
        byte[] bytes = (user.getPassword() + numAndChar).getBytes();
        String md5 = Md5Utils.getMD5(bytes);
        user.setPassword(md5);
        this.save(user);
    }

    @Override
    public PageResult<User> pageList(RequestPage<User> requestPage) {
        User user = requestPage.getData() == null ? new User() : requestPage.getData();
        QueryWrapper<User> queryWrapper = Wrappers.query(new User()).eq("dr", DrStatus.NORMAL);
        if(!StringUtils.isBlank(user.getUserName())){
            queryWrapper.like("user_name",user.getUserName());
        }
        Page<User> page = this.page(new Page<>(requestPage.getPage(), requestPage.getSize()), queryWrapper);
        PageResult<User> pageResult = new PageResult<>(page.getTotal(), page.getRecords());
        return pageResult;
    }

    @Override
    public void modifyPassword(ModifyPassword modifyPassword) {
        // 1、检测原始密码是否一致
        String oldPassword = modifyPassword.getOldPassword();
        User user = this.getOne(Wrappers.query(new User()).eq("id", modifyPassword.getUserId()));
        String salt = user.getSalt();
        String oldDatabsePassword = user.getPassword();
        String md5 = Md5Utils.getMD5((oldPassword + salt).getBytes());
        if(!md5.equalsIgnoreCase(oldDatabsePassword)){
            throw new InterException(5004,"原始密码不一致，请重新填写");
        }
        // 2、构建新密码
        String newsPassword = modifyPassword.getNewsPassword();
        String s = Md5Utils.getMD5((newsPassword + salt).getBytes());
        User newsUser = new User();
        newsUser.setId(modifyPassword.getUserId());
        newsUser.setPassword(s);
        this.updateById(newsUser);
    }
}
