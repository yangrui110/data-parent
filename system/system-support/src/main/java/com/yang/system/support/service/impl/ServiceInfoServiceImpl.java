package com.yang.system.support.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.system.client.entity.ServiceInfo;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.dao.ServiceInfoDao;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.service.ServiceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务表，统筹整个系统的服务列表 服务实现类
 * </p>
 *
 * @author yangrui
 * @since 2021-02-10
 */
@Service
@Slf4j
public class ServiceInfoServiceImpl extends ServiceImpl<ServiceInfoDao, ServiceInfo> implements ServiceInfoService {

    @Cacheable(cacheNames="gatewayRoutes",key="'gateway-routes'")
    @Override
    public List<ServiceInfo> listServiceInfos() {
        List<ServiceInfo> list = this.list(Wrappers.query(new ServiceInfo()).eq("dr", DrStatus.NORMAL));
        log.info("大小：{}",list.size());
        return list;
    }

    @Override
    public PageResult<ServiceInfo> pageList(RequestPage<ServiceInfo> serviceInfoRequestPage) {
        QueryWrapper<ServiceInfo> queryWrapper = Wrappers.query(new ServiceInfo()).eq("dr", DrStatus.NORMAL);
        Page<ServiceInfo> page = this.page(new Page<>(), queryWrapper);
        PageResult<ServiceInfo> pageResult = new PageResult<>(page.getTotal(), page.getRecords());
        return pageResult;
    }

    @CachePut(cacheNames="gatewayRoutes",key="'gateway-routes'")
    @Override
    public List<ServiceInfo> updateServiceInfo(ServiceInfo serviceInfo) {
        this.updateById(serviceInfo);
        List<ServiceInfo> list = this.list(Wrappers.query(new ServiceInfo()).eq("dr", DrStatus.NORMAL));
        return list;
    }

    @CachePut(cacheNames="gatewayRoutes",key="'gateway-routes'")
    @Override
    public List<ServiceInfo> batchUpdateServiceInfo(List<ServiceInfo> serviceInfos) {
        this.updateBatchById(serviceInfos);
        List<ServiceInfo> list = this.list(Wrappers.query(new ServiceInfo()).eq("dr", DrStatus.NORMAL));
        return list;
    }

    @CachePut(cacheNames="gatewayRoutes",key="'gateway-routes'")
    @Override
    public List<ServiceInfo> deleteServiceInfo(ServiceInfo serviceInfo) {
        this.removeById(serviceInfo);
        List<ServiceInfo> list = this.list(Wrappers.query(new ServiceInfo()).eq("dr", DrStatus.NORMAL));
        return list;
    }

    @CachePut(cacheNames="gatewayRoutes",key="'gateway-routes'")
    @Override
    public List<ServiceInfo> addServiceInfo(ServiceInfo serviceInfo) {
        serviceInfo.setId(new Sequence().nextId());
        serviceInfo.setDr(DrStatus.NORMAL);
        serviceInfo.setCreateTime(LocalDateTime.now());
        serviceInfo.setUpdateTime(LocalDateTime.now());
        this.save(serviceInfo);
        List<ServiceInfo> list = this.list(Wrappers.query(new ServiceInfo()).eq("dr", DrStatus.NORMAL));
        return list;
    }

}
