package com.yang.system.client.vo;

import com.yang.system.client.entity.MenuApi;
import lombok.Data;

/**
 * @project data-parent
 * @Date 2021/2/14
 * @Auth yangrui
 **/
@Data
public class MenuApiVo extends MenuApi {

    /**
     * api路径
     * */
    private String path;

    /**
     * 服务名
     * */
    private String serviceName;

    /**
     * 服务Id
     * */
    private Long serviceId;

    /**
     * 服务的访问路径
     * */
    private String servicePath;
}
