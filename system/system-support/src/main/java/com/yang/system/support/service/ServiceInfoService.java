package com.yang.system.support.service;

import com.yang.system.client.entity.ServiceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务表，统筹整个系统的服务列表 服务类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-10
 */
public interface ServiceInfoService extends IService<ServiceInfo> {

    /**
     * 获取所有路由
     * */
    List<ServiceInfo> listServiceInfos();

    /**
     * 更新某个路由
     * */
    List<ServiceInfo> updateServiceInfo(ServiceInfo serviceInfo);

    /**
     * 批量更新某个路由
     * */
    List<ServiceInfo> batchUpdateServiceInfo(List<ServiceInfo> serviceInfo);

    /**
     *删除某个路由
     * */
    List<ServiceInfo> deleteServiceInfo(ServiceInfo serviceInfo);

    /**
     *新增某个路由
     * */
    List<ServiceInfo> addServiceInfo(ServiceInfo serviceInfo);

}
