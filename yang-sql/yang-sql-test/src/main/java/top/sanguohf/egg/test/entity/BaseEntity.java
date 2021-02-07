package top.sanguohf.egg.test.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {

    //主键
    private String id;
    //创建时间
    private String createTime;
    //修改时间
    private String updateTime;
    //创建人
    private String createUserId;
    //修改人
    private String updateUserId;
    //删除标记(1:未删除，2：已删除)
    private Integer dr;

    //创建人的名字
    private transient String createUserName;

    //修改人的名字
    private transient String updateUserName;
}
