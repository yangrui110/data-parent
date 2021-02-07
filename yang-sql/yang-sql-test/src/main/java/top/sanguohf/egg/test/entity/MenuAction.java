package top.sanguohf.egg.test.entity;

import lombok.Data;
import top.sanguohf.egg.annotation.TableName;

/**
 * @Author:Mr.XiongKF
 * @Date:2019/11/20 0:19
 * Study well and make progress every day.
 */
@Data
@TableName("sys_menu_action")
public class MenuAction extends BaseEntity {

    private String menuId;

    private String actionId;

}
