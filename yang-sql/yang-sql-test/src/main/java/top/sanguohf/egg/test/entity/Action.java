package top.sanguohf.egg.test.entity;

import lombok.Data;
import top.sanguohf.egg.annotation.TableName;

/**
 * @auth 杨瑞
 * @date 2019/11/19 13:37
 */
@Data
@TableName("sys_action")
public class Action extends BaseEntity {
    private String code;
    private String describe;
    private String status;
    private String name;

    private String btnPath;
    private String btnPos;

    private String btnEvent;

    private String btnColor;

    private String btnIcon;

    private String btnMulti;

    private String confirmId;

    private Integer btnSort;

    private String associationPage;

    private Integer base;
}
