package top.sanguohf.egg.test.entity;

import lombok.Data;
import top.sanguohf.egg.annotation.Condition;
import top.sanguohf.egg.annotation.Field;
import top.sanguohf.egg.annotation.Id;
import top.sanguohf.egg.annotation.TableName;
import top.sanguohf.egg.constant.ValueType;

import java.util.Date;

@Data
@TableName("xf_rMaterial")
public class XfRMaterial {

    @Id //定义此表的主键
    @Field("pk_material")
    private String pk_material;

    @Field("rMeCode")
    private String rMeCode;

    @Field("rMeCodeName")
    private String rMeCodeName;

    @Field("rMeSpecName")
    private String rMeSpecName;

    @Field("mMenicCode")
    private String mMenicCode;
    @Condition(value = "123",type = ValueType.INTEGER)
    @Field("cMeMemo")
    private String cMeMemo;
    @Field("cMaker")
    private String cMaker;
    //@OrderBy(direct = "desc")
    @Field("dnMaketime")
    private Date dnMaketime;
    @Field("cModifyPerson")
    private String cModifyPerson;
    @Field("dnModifytime")
    private Date dnModifytime;
    @Field("dr")
    private Integer dr;
    @Field("pk_matertype")
    private String pk_matertype;
    @Field("pk_corp")
    private String pk_corp;
    @Field("rMeModel")
    private String rMeModel;
    @Field("decrule")
    private Integer decrule;
}
