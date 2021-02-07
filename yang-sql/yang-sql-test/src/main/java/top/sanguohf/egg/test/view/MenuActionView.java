package top.sanguohf.egg.test.view;

import top.sanguohf.egg.annotation.MainTable;
import top.sanguohf.egg.annotation.ReferTable;
import top.sanguohf.egg.annotation.ViewTable;
import top.sanguohf.egg.test.entity.Action;
import top.sanguohf.egg.test.entity.Menu;
import top.sanguohf.egg.test.entity.MenuAction;

@ViewTable
public class MenuActionView {
    @MainTable(tableAlias = "menuAction") //定义视图查询的主表信息（其也可是视图实体）
    private MenuAction menuAction;
    //定义join查询的关系
    @ReferTable(tableAlias = "menu",relation = "left join",condition = "menuAction.menu_id = menu.id",
            includeColumns = {"name as menuName","sort as menuSort","target as menuTarget","title as menuTitle","url as menuUrl",
                    "icon as menuIcon", "component as menuComponent","code as menuCode"})
    private Menu menu;

    @ReferTable(tableAlias = "action",relation = "left join",condition = "menuAction.action_id = action.id",
            includeColumns = {"code as actionCode","describe as actionDescribe","status as actionStatus","name as actionName",
            "btnPath as actionBtnPath","btnPos as actionBtnPos","btnEvent as actionBtnEvent","btnColor as actionBtnColor"})
    private Action action;
}
