package com.yang.gateway.support.feign;

import com.yang.system.client.entity.ServiceInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @project data-parent
 * @Date 2021/2/10
 * @Auth yangrui
 **/
@FeignClient(name = "system", path = "system")
public interface ServiceInfoApi {

    @GetMapping("/serviceInfo/listServiceInfos")
    List<ServiceInfo> listServiceInfos();

}
