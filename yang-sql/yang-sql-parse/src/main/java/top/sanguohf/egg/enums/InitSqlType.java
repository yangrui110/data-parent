package top.sanguohf.egg.enums;

/**
 * @author 杨瑞
 **/
public enum InitSqlType {

    INSERT(1,"插入语句")
    ,SELECT(6,"查询语句")
    ,UPDATE(2,"更新语句")
    ,DELETE(3,"删除语句")
    ,BATCH_UPDATE(4,"批量更新"),
    OTHER(5,"其它");
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
