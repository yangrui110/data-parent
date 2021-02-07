package top.sanguohf.egg.test.entity;

import lombok.Data;
import top.sanguohf.egg.annotation.TableName;

/**
 * @auth 杨瑞
 * @date 2019/11/18 14:24
 */
@Data
@TableName("sys_menu")
public class Menu extends BaseEntity {
    //菜单的标识属性
    private String name;
    //排序
    private Integer sort;
    //上一级
    private String parentId;
    //菜单路径
    private String url;
    //打开方式
    private String target;
    //菜单名称
    private String title;
    //菜单图标
    private String icon;
    //是否显示
    private Integer show;
    //组件名称
    private String component;
    private String code;

}
