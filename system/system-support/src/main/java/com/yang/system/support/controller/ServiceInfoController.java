package com.yang.system.support.controller;


import com.yang.system.client.entity.ServiceInfo;
import com.yang.system.client.resp.PageResult;
import com.yang.system.support.constant.DrStatus;
import com.yang.system.support.resp.RequestPage;
import com.yang.system.support.resp.ResponseResult;
import com.yang.system.support.service.ServiceInfoService;
import com.yang.system.support.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @ResponseBody
    @PostMapping("pageList")
    public ResponseResult pageList(@RequestBody RequestPage<ServiceInfo> serviceInfo){
        PageResult<ServiceInfo> serviceInfos = serviceInfoService.pageList(serviceInfo);
        return ResponseResult.success(serviceInfos);
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

    @ResponseBody
    @DeleteMapping("batchDelete")
    public ResponseResult batchDelete(String ids){
        if(StringUtils.isEmpty(ids)) return ResponseResult.error();
        String[] split = ids.split(",");
        ArrayList<ServiceInfo> list = new ArrayList<>();
        for(String one: split){
            ServiceInfo menu = new ServiceInfo();
            menu.setId(Long.parseLong(one));
            menu.setDr(DrStatus.DEL);
            list.add(menu);
        }
        serviceInfoService.batchUpdateServiceInfo(list);
        return ResponseResult.success();
    }

    @ResponseBody
    @DeleteMapping("delete")
    public ResponseResult delete(String id){
        if(StringUtils.isEmpty(id)) return ResponseResult.error();
        ServiceInfo menu = new ServiceInfo();
        menu.setId(Long.parseLong(id));
        menu.setDr(DrStatus.DEL);
        serviceInfoService.updateServiceInfo(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("update")
    public ResponseResult update(@RequestBody ServiceInfo menu){
        serviceInfoService.updateServiceInfo(menu);
        return ResponseResult.success();
    }

    @ResponseBody
    @PostMapping("add")
    public ResponseResult add(@RequestBody ServiceInfo menu){
        menu.setId(IdUtils.nextId());
        menu.setDr(DrStatus.NORMAL);
        serviceInfoService.addServiceInfo(menu);
        return ResponseResult.success();
    }

}
