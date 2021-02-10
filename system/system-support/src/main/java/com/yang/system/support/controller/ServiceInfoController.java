package com.yang.system.support.controller;


import com.yang.system.client.entity.ServiceInfo;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.ServiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务表，统筹整个系统的服务列表 前端控制器
 * </p>
 *
 * @author yangrui
 * @since 2021-02-10
 */
@Controller
@RequestMapping("/serviceInfo")
public class ServiceInfoController {

    @Autowired
    private ServiceInfoService serviceInfoService;

    /**
     * 获取列表
     * */
    @ResponseBody
    @GetMapping("listServiceInfos")
    public List<ServiceInfo> listServiceInfos(){
        List<ServiceInfo> serviceInfos = serviceInfoService.listServiceInfos();
        return serviceInfos;
    }

    /**
     * 新增一个服务
     * */
    @ResponseBody
    @PostMapping("addServiceInfo")
    public ResponseResult addServiceInfo(@RequestBody ServiceInfo serviceInfo){
        serviceInfoService.addServiceInfo(serviceInfo);
        return ResponseResult.success();

    }

    /**
     * 新增一个服务
     * */
    @ResponseBody
    @PostMapping("updateServiceInfo")
    public ResponseResult updateServiceInfo(@RequestBody ServiceInfo serviceInfo){
        serviceInfoService.updateServiceInfo(serviceInfo);
        return ResponseResult.success();

    }

}
