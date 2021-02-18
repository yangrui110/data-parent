package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.Api;
import com.yang.system.client.entity.MenuApi;
import com.yang.system.client.entity.RolePermission;
import com.yang.system.client.resp.PageResult;
import com.yang.system.client.vo.MenuApiSelect;
import com.yang.system.client.vo.MenuApiVo;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.constant.PermissionType;
import com.yang.system.support.dao.MenuApiDao;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.service.ApiService;
import com.yang.system.support.service.MenuApiService;
import com.yang.system.support.service.RolePermissionService;
import com.yang.system.support.service.ServiceInfoService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单对应的接口 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-14
 */
@Service
public class MenuApiServiceImpl extends ServiceImpl<MenuApiDao, MenuApi> implements MenuApiService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private MenuApiDao menuApiDao;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public PageResult<MenuApiVo> getMenuApis(RequestPage<MenuApiVo> menuApiVo) {
        MenuApiVo menuApi = menuApiVo.getData();
        Page<MenuApiVo> menuApis = menuApiDao.getMenuApis(new Page(menuApiVo.getPage(), menuApiVo.getSize()), menuApi);
        // 1、获取到菜单对应的api信息
        /*Page page = this.page(new Page(menuApiVo.getPage(), menuApiVo.getSize()), Wrappers.query(new MenuApi()).eq("menu_id", menuApi.getMenuId()));
        if(page.getTotal()<=0){
            return new PageResult<MenuApiVo>();
        }
        List<MenuApi> menuApis = page.getRecords();
        List<Long> apiIds = menuApis.stream().map(MenuApi::getApiId).collect(Collectors.toList());
        List<Api> apis = apiService.list(Wrappers.query(new Api()).in("id",apiIds));
        if(apis.size()==0) return new PageResult<MenuApiVo>();

        Map<Long, Api> apiMap = apis.stream().collect(Collectors.toMap(Api::getId, item -> item));
        // 2、获取到api对应的service信息
        List<Long> serviceIds = apis.stream().map(Api::getServiceId).distinct().collect(Collectors.toList());
        List<ServiceInfo> serviceInfos = serviceInfoService.list(Wrappers.query(new ServiceInfo()).in("id", serviceIds));
        Map<Long, ServiceInfo> serviceInfoMap = serviceInfos.stream().collect(Collectors.toMap(ServiceInfo::getId, item -> item));
        // 4、转换结果集
        ArrayList<MenuApiVo> list = new ArrayList<>();
        for(MenuApi one: menuApis){
            MenuApiVo apiVo = new MenuApiVo();
            BeanUtils.copyProperties(one,apiVo);
            Api api = apiMap.get(one.getApiId());
            if(api!=null) {
                apiVo.setPath(api.getPath());
                ServiceInfo serviceInfo = serviceInfoMap.get(api.getServiceId());
                if(serviceInfo!=null){
                    apiVo.setServiceName(serviceInfo.getServiceName());
                    apiVo.setServiceId(serviceInfo.getId());
                    apiVo.setServicePath(serviceInfo.getPath());
                }
            }
            list.add(apiVo);
        }*/
        PageResult<MenuApiVo> pageResult = new PageResult<>(menuApis.getTotal(), menuApis.getRecords());
        return pageResult;
    }

    @Transactional
    @Override
    public void add(MenuApiVo menuApiVo) {
        // 1、插入api表
        Api api = new Api();
        api.setApiPath(menuApiVo.getApiPath());
        api.setId(IdUtils.nextId());
        api.setCreateTime(LocalDateTime.now());
        api.setUpdateTime(LocalDateTime.now());
        api.setDr(DrStatus.NORMAL);
        apiService.save(api);
        // 2、插入menu_api表
        MenuApi menuApi = new MenuApi();
        menuApi.setApiId(api.getId());
        menuApi.setMenuId(menuApiVo.getMenuId());
        menuApi.setDr(DrStatus.NORMAL);
        menuApi.setCreateTime(LocalDateTime.now());
        menuApi.setId(IdUtils.nextId());
        menuApi.setUpdateTime(LocalDateTime.now());
        this.save(menuApi);
    }

    @Override
    public void update(MenuApiVo menuApiVo) {
        Api api = new Api();
        api.setDr(DrStatus.NORMAL);
        api.setApiPath(menuApiVo.getApiPath());
        api.setUpdateTime(LocalDateTime.now());
        api.setId(menuApiVo.getApiId());
        apiService.updateById(api);
    }

    @Transactional
    @Override
    public void delete(MenuApi menuApi) {
        this.removeById(menuApi.getId());
        // 删除
        apiService.remove(Wrappers.query(new Api()).eq("id",menuApi.getApiId()));
    }

    @Transactional
    @Override
    public void batchDelete(List<MenuApi> menuApis) {
        if(menuApis.size()==0) return;
        List<Long> menuApiIds = menuApis.stream().map(MenuApi::getId).collect(Collectors.toList());
        this.removeByIds(menuApiIds);

        List<Long> apiIds = menuApis.stream().map(MenuApi::getApiId).collect(Collectors.toList());
        apiService.removeByIds(apiIds);
    }

    @Override
    public MenuApiSelect listApiByRoleIdAndMenuId(Long menuId, Long roleId) {
        MenuApiSelect apiSelect = new MenuApiSelect();
        // 1、加载菜单对应的api
        List<MenuApi> menuApis = this.list(Wrappers.query(new MenuApi()).eq("menu_id", menuId).eq("dr", DrStatus.NORMAL));
        List<Long> apiIds = menuApis.stream().map(MenuApi::getApiId).collect(Collectors.toList());
        List<Api> apis = apiService.list(Wrappers.query(new Api()).in("id", apiIds).eq("dr", DrStatus.NORMAL));
        apiSelect.setMenuApis(apis);
        // 2、加载角色对应的api
        QueryWrapper<RolePermission> queryWrapper = Wrappers.query(new RolePermission())
                .eq("dr", DrStatus.NORMAL)
                .eq("role_id", roleId)
                .in("permission_id",apiIds)
                .eq("type", PermissionType.API);
        List<RolePermission> permissions = rolePermissionService.list(queryWrapper);
        List<Api> apiList = permissions.stream().map(item -> {
            Api api = new Api();
            api.setId(item.getPermissionId());
            return api;
        }).collect(Collectors.toList());
        apiSelect.setRoleApis(apiList);
        return apiSelect;
    }
}
