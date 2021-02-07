package top.sanguohf.top.bootcon.enums;

/**
 * @author  杨瑞
 **/
public enum  InitSqlType {

    INSERT(1,"插入语句")
    ,UPDATE(2,"更新语句")
    ,DELETE(3,"删除语句")
    ,DELETE_WITH_KEY(4,"通过主键删除")
    ,UPDATE_WITH_KEY(5,"通过主键更新");
    private int type;

    private String desc;

    InitSqlType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }


    public String getDesc() {
        return desc;
    }
}
