package com.yang.system.client.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务表，统筹整个系统的服务列表
 * </p>
 *
 * @author yangrui
 * @since 2021-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ServiceInfo对象", description="服务表，统筹整个系统的服务列表")
public class ServiceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "修改人")
    private Long updateUserId;

    @ApiModelProperty(value = "删除标记，1：已删除，0未删除")
    private Integer dr;

    @ApiModelProperty(value = "服务的路径，例: lb://system")
    private String uri;

    @ApiModelProperty(value = "访问路径,例：/system/**")
    private String path;

    @ApiModelProperty(value = "服务名")
    private String serviceName;


}
