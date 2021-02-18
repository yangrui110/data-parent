package com.yang.gateway.support.feign;

import com.yang.system.client.entity.ServiceInfo;
import com.yang.system.client.entity.WhitePath;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@FeignClient(name = "system", path = "system")
public interface SystemFeignApi {

    @GetMapping("/serviceInfo/listServiceInfos")
    List<ServiceInfo> listServiceInfos();

    @GetMapping("/whitePath/listWhitePath")
    List<WhitePath> listWhitePath();
}
