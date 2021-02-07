package top.sanguohf.egg.param;

import com.alibaba.fastjson.JSONObject;
import top.sanguohf.egg.annotation.ViewTable;
import top.sanguohf.egg.base.*;
import top.sanguohf.egg.ops.EntityDeleteSql;
import top.sanguohf.egg.ops.EntityInsertSql;
import top.sanguohf.egg.ops.EntitySelectSql;
import top.sanguohf.egg.ops.EntityUpdateSql;
import top.sanguohf.egg.reflect.ReflectEntity;
import top.sanguohf.egg.util.EntityParseUtil;
import top.sanguohf.egg.util.StringUtils;

import java.util.*;

public class EntityParamParse {

    private EntityParams params;

    private Class classEntity;

    private EntitySelectSql selectSql;

    private boolean present =false;

    public EntityParamParse(EntityParams entityParams) throws ClassNotFoundException {
        this.params = entityParams;
        //获取到指定的class实体
        classEntity = Class.forName(entityParams.getTableClassName());
        present = classEntity.isAnnotationPresent(ViewTable.class);
    }



    public EntitySelectSql parseToEntitySelectSql() throws ClassNotFoundException, NoSuchFieldException {
        //1.构造实体条件
        selectSql = (EntitySelectSql) EntityParseUtil.parseViewEntityTable(classEntity);
        EntityCondition condition = collectAndParseCondition();
        selectSql.setWheres(condition);
        List<EntityOrderBy> orderBy=params.getOrderBy();
        List<EntityColumn> entityColumns = selectSql.getColumns();
        for(EntityOrderBy orderBy1: orderBy){
            for(EntityColumn column:entityColumns){
                String alias = column.getAliasColumn()==null?column.getOrignColumn():column.getAliasColumn();
                if(alias.equals(orderBy1.getColumn())){
                    EntityOrderBy order = new EntityOrderBy();
                    order.setDirect(orderBy1.getDirect());
                    order.setColumn(column.getOrignColumn());
                    order.setTableAlias(column.getTableAlias());
                    selectSql.getOrderBys().add(order);
                }
            }
        }
        //分组
        /*
        List<EntityGroupBy> groupBy = params.getGroupBy();
        for(EntityGroupBy groupBy1: groupBy){
            for(EntityColumn column:entityColumns){
                String alias = column.getAliasColumn()==null?column.getOrignColumn():column.getAliasColumn();
                if(alias.equals(groupBy1.getColumn())){
                    EntityGroupBy order = new EntityGroupBy();
                    order.setColumn(column.getOrignColumn());
                    order.setTableAlias(column.getTableAlias());
                    selectSql.getGroupBys().add(order);
                }
            }
        }*/
        return selectSql;
    }
    //解析出查询条件
    private EntityCondition collectAndParseCondition() throws NoSuchFieldException, ClassNotFoundException {
        EntityCondition result = null;
        JSONObject condition= params.getCondition();
        EntityCondition entityCondition = ReflectEntity.collectDefaultCondition(classEntity);
        if(condition !=null&&condition.keySet().size()>0){
            JSONObject selectCondition = EntityParseUtil.saveConditionToSelectCondition(condition);
            //EntityParseUtil.excludeNoExistColumn(selectCondition, selectSql.getColumns());
            if(selectCondition!=null)
                condition=selectCondition;
            EntityCondition condition1 = parserParamCondition(condition);
            if(entityCondition!=null&&condition1!=null){
                EntityConditionDom conditionDom = new EntityConditionDom();
                conditionDom.setLeft(entityCondition);
                conditionDom.setRight(condition1);
                conditionDom.setRelation("and");
                result = conditionDom;
                //selectSql.setWheres(conditionDom);
            }else {
                result = condition1==null?entityCondition:condition1;
                //selectSql.setWheres(condition1==null?entityCondition:condition1);
            }
        }
        return result;
    }
    private EntityCondition parserParamCondition(Map condition) throws NoSuchFieldException, ClassNotFoundException {
        Object os = condition.get("condition");
        if(os==null){
            EntityCondition dom = parseOneCondition(condition);
            return dom;
        }else {
            List ls = (List) os;
            EntityListCondition condition1=parseListCondition(ls);
            condition1.setCombine((String) condition.get("combine"));
            return condition1;
        }
    }
    //解析List
    private EntityListCondition parseListCondition(List ls) throws NoSuchFieldException, ClassNotFoundException {
        //有值的情况
        EntityListCondition condition1=new EntityListCondition();
        List lm=new LinkedList();
        for(Object one : ls){
            Map oo= (Map) one;
            lm.add(parseOneCondition(oo));
        }
        condition1.setCondition(lm);
        return condition1;
    }
    //解析一个基础DOM元素
    private EntityCondition parseOneCondition(Map map) throws NoSuchFieldException, ClassNotFoundException {
        Object relation=map.get("relation");
        Object left = map.get("left");
        if("____".equals(left)){
            EntityConditionSql sql = new EntityConditionSql();
            sql.setSql((String) relation);
            return sql;
        }
        if(relation!=null&&("is".equalsIgnoreCase(((String) relation).trim())||"is not".equalsIgnoreCase(((String) relation).trim()))){
            EntityConditionPre pre = new EntityConditionPre();
            pre.setRelation((String) relation);
            EntityColumn fieldAlias = getFieldAlias((String) left);
            if(fieldAlias!=null){
                pre.setTableAlias(fieldAlias.getTableAlias());
                pre.setColumn(fieldAlias.getOrignColumn());
            }
            pre.setValue(null);
            return pre;
        }else {
            EntityConditionDom dom = new EntityConditionDom();
            dom.setLeft(parseToCondition(left, false));
            dom.setRelation((String) relation);
            Object right = map.get("right");
            EntityCondition condition = parseToCondition(right, true);
            dom.setRight(condition);
            return dom;
        }
    }
    //解析left或者right元素
    private EntityCondition parseToCondition(Object os,boolean isRight) throws ClassNotFoundException, NoSuchFieldException {
        EntityCondition condition=null;
        if(os !=null && os instanceof Map) {
            condition = parserParamCondition((Map) os);
            return condition;
        }else if(!isRight&&os==null){
            throw new RuntimeException("列名不能为空,列："+os);
        } else{
            if(!isRight) {
                condition = new EntityConditionColumn();
                EntityColumn fieldAlias = getFieldAlias((String) os);
                if(fieldAlias!=null){
                    ((EntityConditionColumn) condition).setColumn(fieldAlias.getOrignColumn());
                    ((EntityConditionColumn) condition).setTableAlias(fieldAlias.getTableAlias());
                }
            } else {
                condition=new EntityConditionValue();
                ((EntityConditionValue) condition).setColumn(os);
            }

            return condition;
        }
    }

    public EntityInsertSql parseToEntityInertSql() throws ClassNotFoundException, NoSuchFieldException {
        // TODO: implement
        //此种情况，主要是解析data的值
        EntityInsertSql insertSql=new EntityInsertSql();
        JSONObject os = params.getCondition();
        List<EntityInsert> inserts = new LinkedList<>();
        HashMap<String, Object> keys = new HashMap<>();
        for(String k:os.keySet()){
            EntityInsert insert = new EntityInsert();
            String field = ReflectEntity.getTableField(classEntity, k);
            insert.setColumn(field);
            insert.setValue(os.get(k));
            if(!keys.containsKey(field))
                inserts.add(insert);
            keys.put(field,1);
        }

        insertSql.setTableName(ReflectEntity.reflectTableName(classEntity));
        insertSql.setInsertList(inserts);
        return insertSql;
    }

    public EntityUpdateSql parseToEntityUpdateSqlWithKeys() throws NoSuchFieldException, ClassNotFoundException {
        // TODO: implement
        EntityUpdateSql updateSql = new EntityUpdateSql();
        JSONObject os = params.getCondition();
        Map<String, Object> primaryKeys = ReflectEntity.reflectPrimaryKeys(classEntity);
        List<EntityInsert> inserts = new LinkedList<>();
        HashMap<String, Object> keys = new HashMap<>();
        for(String k:os.keySet()){
            EntityInsert insert = new EntityInsert();
            String field = ReflectEntity.getTableField(classEntity, k);
            insert.setColumn(field);
            insert.setValue(os.get(k));
            if(!keys.containsKey(field)&&!primaryKeys.containsKey(field))
                inserts.add(insert);
            keys.put(field,1);
        }
        updateSql.setTableName(ReflectEntity.reflectTableName(classEntity));
        updateSql.setUpdates(inserts);
        EntityCondition condition = getkeysCondition(os);
        updateSql.setWheres(condition);
        return updateSql;
    }

    private EntityCondition getkeysCondition(JSONObject os) throws NoSuchFieldException, ClassNotFoundException {
        List<EntityInsert> entityInserts = ReflectEntity.reflectPrimaryKeys(classEntity, os);
        EntityListCondition listCondition = new EntityListCondition();
        ArrayList<EntityCondition> conditions = new ArrayList<>();
        for(EntityInsert insert:entityInserts){
            EntityConditionColumn column = new EntityConditionColumn();
            column.setColumn(insert.getColumn());
            EntityConditionValue value = new EntityConditionValue();
            value.setColumn(insert.getValue());
            EntityConditionDom dom = new EntityConditionDom();
            dom.setLeft(column);
            dom.setRight(value);
            dom.setRelation("=");
            conditions.add(dom);
        }
        listCondition.setCondition(conditions);
        return listCondition;
    }

    public EntityUpdateSql parseToEntityUpdateSql() throws NoSuchFieldException, ClassNotFoundException {
        // TODO: implement
        selectSql = (EntitySelectSql) EntityParseUtil.parseViewEntityTable(classEntity);
        EntityCondition condition = collectAndParseCondition();
//        Map<String, Object> keys = ReflectEntity.reflectPrimaryKeys(classEntity);
        EntityUpdateSql updateSql = new EntityUpdateSql();
        JSONObject os = params.getUpdates();
        List<EntityInsert> inserts = new LinkedList<>();
        for(String k:os.keySet()){
            EntityInsert insert = new EntityInsert();
            insert.setColumn(ReflectEntity.getTableField(classEntity,k));
            insert.setValue(os.get(k));
//            if(!keys.containsKey(insert.getColumn()))
            inserts.add(insert);
        }
        updateSql.setTableName(ReflectEntity.reflectTableName(classEntity));
        updateSql.setUpdates(inserts);
        updateSql.setWheres(condition);
        return updateSql;
    }

    public EntityDeleteSql parseToEntityDeleteSqlWithKeys() throws NoSuchFieldException, ClassNotFoundException {
        // TODO: implement
        EntityDeleteSql deleteSql=new EntityDeleteSql();
        deleteSql.setTableName(ReflectEntity.reflectTableName(classEntity));
        JSONObject condition= params.getCondition();
        EntityCondition entityCondition = getkeysCondition(condition);
        deleteSql.setWheres(entityCondition);
        return deleteSql;
    }
    public EntityDeleteSql parseToEntityDeleteSql() throws NoSuchFieldException, ClassNotFoundException {
        // TODO: implement
        selectSql = (EntitySelectSql) EntityParseUtil.parseViewEntityTable(classEntity);
        EntityCondition condition = collectAndParseCondition();
        EntityDeleteSql deleteSql=new EntityDeleteSql();
        deleteSql.setTableName(ReflectEntity.reflectTableName(classEntity));
        deleteSql.setWheres(condition);
        return deleteSql;
    }

    /**
     * 获取到列的别名
     * */
    private EntityColumn getFieldAlias(String column){
        for(EntityColumn conditionColumn:selectSql.getColumns()){
            String alias = StringUtils.isEmpty(conditionColumn.getAliasColumn())?conditionColumn.getAliasColumn():conditionColumn.getFieldName();
            if(alias.equalsIgnoreCase(column)){
                return conditionColumn;
            }
        }
        return null;
    }
}
