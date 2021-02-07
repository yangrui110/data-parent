package top.sanguohf.top.bootcon.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;
import top.sanguohf.egg.base.EntityColumn;
import top.sanguohf.egg.base.EntityInsert;
import top.sanguohf.egg.base.EntityOrderBy;
import top.sanguohf.egg.enums.InitSqlType;
import top.sanguohf.egg.ops.*;
import top.sanguohf.egg.param.EntityParamParse;
import top.sanguohf.egg.param.EntityParams;
import top.sanguohf.egg.reflect.ReflectEntity;
import top.sanguohf.egg.util.ConsoleSqlUtil;
import top.sanguohf.egg.util.EntityConditionBuilder;
import top.sanguohf.egg.xml.SqlXmlExpresession;
import top.sanguohf.top.bootcon.cache.EntityCache;
import top.sanguohf.top.bootcon.config.DataBaseTypeInit;
import top.sanguohf.top.bootcon.page.Page;
import top.sanguohf.top.bootcon.resp.CommonPageResp;
import top.sanguohf.top.bootcon.service.CommonService;
import top.sanguohf.top.bootcon.util.BatchSupport;
import top.sanguohf.top.bootcon.util.ObjectUtil;
import top.sanguohf.top.bootcon.util.ParamEntityParseUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基类，通用的增删改查
 * */
public class CommonServiceImpl implements CommonService {

    JdbcTemplate jdbcTemplate;

    DataBaseTypeInit dbType;

    private int maxBatch = 1000;

    public CommonServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        dbType = new DataBaseTypeInit(jdbcTemplate.getDataSource());
    }

    @Override
    public CommonPageResp findPageList(EntityParams params, Page page)  {
        try {
            EntityParams params1 = inteceptor(params);
            EntitySelectSql selectSql = new EntityParamParse(params1).parseToEntitySelectSql();
            long count = count(params);
            CommonPageResp resp = new CommonPageResp<>();
            if (count != 0) {
                //获取到数据集
                EntityPageSql entityPageSql = new EntityPageSql(selectSql);
                String toPageSql = entityPageSql.toPageSql(page.getPage(), page.getSize(), true, dbType.getDbType());
                LinkedList objects = new LinkedList<>();
                entityPageSql.addValue(page.getPage(), page.getSize(), dbType.getDbType(), objects);
                List<Map<String, Object>> querys = jdbcTemplate.queryForList(toPageSql, objects.toArray());
                ConsoleSqlUtil.console(toPageSql, InitSqlType.SELECT);
                ConsoleSqlUtil.consoleParam(objects);
                // 获取到需要查询的列
                List<String> collect = entityPageSql.getSelectSql().getColumns().stream().map(EntityColumn::getOrignColumn).collect(Collectors.toList());
                collect.add(0, "__row_number__");
                ConsoleSqlUtil.consoleColumns(collect);
                ConsoleSqlUtil.consoleResult(querys);
                resp.setData(querys);
            }
            resp.setCount(count);
            return resp;
        }catch (Exception e){
            throw new RuntimeException("分页查询出现异常：",e);
        }
    }


//    @Override
    public List<Map<String, Object>> findOnePageList(EntityParams params, Page page){
        try {
            EntityParams params1 = inteceptor(params);
            EntitySelectSql selectSql = new EntityParamParse(params1).parseToEntitySelectSql();
            //获取到数据集
            EntityPageSql entityPageSql = new EntityPageSql(selectSql);
            String toPageSql = entityPageSql.toPageSql(page.getPage(), page.getSize(), true, dbType.getDbType());
            LinkedList objects = new LinkedList<>();
            entityPageSql.addValue(page.getPage(), page.getSize(), dbType.getDbType(), objects);
            List<Map<String, Object>> querys = jdbcTemplate.queryForList(toPageSql, objects.toArray());
            ConsoleSqlUtil.console(toPageSql, InitSqlType.SELECT);
            ConsoleSqlUtil.consoleParam(objects);
            // 获取到需要查询的列
            List<String> collect = entityPageSql.getSelectSql().getColumns().stream().map(EntityColumn::getOrignColumn).collect(Collectors.toList());
            collect.add(0, "__row_number__");
            ConsoleSqlUtil.consoleColumns(collect);
            ConsoleSqlUtil.consoleResult(querys);
            return querys;
        }catch (Exception e){
            throw new RuntimeException("分页查询出现异常：",e);
        }
    }

    @Override
    public List findList(EntityParams params) {
        try {
            EntityParams params1 = inteceptor(params);
            EntitySelectSql selectSql = new EntityParamParse(params1).parseToEntitySelectSql();
            String sqlOne = selectSql.sqlOne(true);
            LinkedList objects = new LinkedList<>();
            selectSql.addValue(objects);
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sqlOne, objects.toArray());
            ConsoleSqlUtil.console(sqlOne, InitSqlType.SELECT);
            ConsoleSqlUtil.consoleParam(objects);
            List<String> collect = selectSql.getColumns().stream().map(EntityColumn::getOrignColumn).collect(Collectors.toList());
            ConsoleSqlUtil.consoleColumns(collect);
            ConsoleSqlUtil.consoleResult(mapList);
            return mapList;
        }catch (Exception e){
            throw new RuntimeException("查询列表出现异常：",e);
        }
    }

    @Override
    public long count(EntityParams params){
        try {
            EntityParams params1 = inteceptor(params);
            EntitySelectSql selectSql = new EntityParamParse(params1).parseToEntitySelectSql();
            EntityPageSql entityPageSql = new EntityPageSql(selectSql);
            String countSql = entityPageSql.toCountSql(dbType.getDbType(), true);
            LinkedList objects = new LinkedList<>();
            entityPageSql.addCountValue(objects);
            Map<String, Object> counts = jdbcTemplate.queryForMap(countSql, objects.toArray());
            ConsoleSqlUtil.console(countSql, InitSqlType.SELECT);
            ConsoleSqlUtil.consoleParam(objects);
            ConsoleSqlUtil.consoleResult(counts);
            long count = Long.parseLong("" + counts.get("__total__"));
            return count;
        }catch (Exception e){
            throw new RuntimeException("统计查询出现异常",e);
        }
    }

    @Override
    public void insert(EntityParams paramData) {
        try {
            EntityParams params1 = inteceptor(paramData);
            EntityInsertSql insertSql = new EntityParamParse(params1).parseToEntityInertSql();
            String sqlOne = insertSql.sqlOne(true);
            LinkedList objects = new LinkedList<>();
            insertSql.addValue(objects);
            ConsoleSqlUtil.console(sqlOne, InitSqlType.INSERT);
            ConsoleSqlUtil.consoleParam(objects);
            jdbcTemplate.update(sqlOne, objects.toArray());
        }catch (Exception e){
            throw new RuntimeException("插入出错：",e);
        }
    }

    @Transactional
    @Override
    public void update(EntityParams paramsData){
        try {
            EntityParams params1 = inteceptor(paramsData);
            EntityUpdateSql updateSql = new EntityParamParse(params1).parseToEntityUpdateSql();
            String sqlOne = updateSql.sqlOne(true);
            LinkedList objects = new LinkedList<>();
            updateSql.addValue(objects);
            ConsoleSqlUtil.console(sqlOne, InitSqlType.UPDATE);
            ConsoleSqlUtil.consoleParam(objects);
            jdbcTemplate.update(sqlOne, objects.toArray());
        }catch (Exception e){
            throw new RuntimeException("更新出错：",e);
        }

    }

    @Override
    public void updateByPrimaryKey(EntityParams paramsData){
        try {
            EntityParams params1 = inteceptor(paramsData);
            EntityUpdateSql updateSql = new EntityParamParse(params1).parseToEntityUpdateSqlWithKeys();
            String sqlOne = updateSql.sqlOne(true);
            LinkedList objects = new LinkedList<>();
            updateSql.addValue(objects);
            ConsoleSqlUtil.console(sqlOne, InitSqlType.UPDATE);
            ConsoleSqlUtil.consoleParam(objects);
            jdbcTemplate.update(sqlOne, objects.toArray());
        }catch (Exception e){
            throw new RuntimeException("根据主键更新出错：",e);
        }
    }

    @Override
    public void delete(EntityParams paramsData) {
        try {
            EntityParams params1 = inteceptor(paramsData);
            EntityDeleteSql deleteSql = new EntityParamParse(params1).parseToEntityDeleteSql();
            String sqlOne = deleteSql.sqlOne(true);
            LinkedList objects = new LinkedList<>();
            deleteSql.addValue(objects);
            ConsoleSqlUtil.console(sqlOne, InitSqlType.DELETE);
            ConsoleSqlUtil.consoleParam(objects);
            jdbcTemplate.update(sqlOne, objects.toArray());
        }catch (Exception e){
            throw new RuntimeException("删除出错",e);
        }
    }

    @Override
    public void deleteByPrimaryKey(EntityParams paramsData){
        try {
            EntityParams params1 = inteceptor(paramsData);
            EntityDeleteSql deleteSql = new EntityParamParse(params1).parseToEntityDeleteSqlWithKeys();
            String sqlOne = deleteSql.sqlOne(true);
            LinkedList objects = new LinkedList<>();
            deleteSql.addValue(objects);
            ConsoleSqlUtil.console(sqlOne, InitSqlType.DELETE);
            ConsoleSqlUtil.consoleParam(objects);
            jdbcTemplate.update(sqlOne, objects.toArray());
        }catch (Exception e){
            throw new RuntimeException("根据主键删除出错:",e);
        }
    }

    @Transactional
    @Override
    public void batchInsert(List<EntityParams> params) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        Connection con = null;
        try {
            con=DataSourceUtils.getConnection(dataSource);
            List<EntityParams> entityParams = inteceptorList(params);
            con.setAutoCommit(false);
            ArrayList<EntityJoinTable> list = new ArrayList<>();
            for (EntityParams params1 : entityParams) {
                EntityInsertSql updateSql = new EntityParamParse(params1).parseToEntityInertSql();
                list.add(updateSql);
            }
            batchOperation(con,list,maxBatch);
        }catch (Exception e){
            throw new RuntimeException("批量插入出错", e);
        }finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    private Object[] collectSortValue(List<EntityInsert> list,List<EntityInsert> firstDomColumns){
        Object[] value = new Object[list.size()];
        int i= 0;
        for(EntityInsert insert:firstDomColumns) {
            boolean exist =false;
            for (EntityInsert cur:list) {
                if(cur.getColumn().equalsIgnoreCase(insert.getColumn())) {
                    value[i] = cur.getValue();
                    i++;
                    exist = true;
                }
            }
            if(!exist)
                throw new RuntimeException("当前列名："+insert.getColumn()+"不存在");
        }
        return value;
    }
    @Override
    public void batchUpdate(List<EntityParams> params){
        Connection con = null;
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
             con=DataSourceUtils.getConnection(dataSource);
            List<EntityParams> entityParams = inteceptorList(params);
            ArrayList<EntityJoinTable> list = new ArrayList<>();
            for (EntityParams params1 : entityParams) {
                EntityUpdateSql updateSql = new EntityParamParse(params1).parseToEntityUpdateSql();
                list.add(updateSql);
            }
            batchOperation(con,list,maxBatch);
        }catch (Exception e){
            throw new RuntimeException("批量更新出错", e);
        }finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * @param connection 数据库连接
     * @param joinTables 解析的SQL语句
     * @param maxBatch 分批保存的数据条数,防止数据量过大，导致崩溃
     * 批量操作
     * */
    private void batchOperation(Connection connection,List<? extends EntityJoinTable> joinTables,int maxBatch) throws SQLException {
        // 1、参数打印 首先收集到需要打印的参数
        // 2、分批打印
        boolean isTransactional = false;
        DataSource dataSource = jdbcTemplate.getDataSource();
        if(DataSourceUtils.isConnectionTransactional(connection,dataSource)){
            isTransactional = true;
        }
        if(BatchSupport.isSuppport(joinTables)){
            if(joinTables.size()>0) {
                EntityJoinTable joinTable = joinTables.get(0);
                String one = joinTable.sqlOne(true);
                ConsoleSqlUtil.console(one,joinTable); // 打印语句
            }
            int count = 0;
            PreparedStatement statement = initPrepareStatement(connection, joinTables);
            ArrayList arrayList = new ArrayList<>();
            for(EntityJoinTable joinTable:joinTables){
                List params = setUpdatePrepare(statement, joinTable);
                arrayList.add(params);
                count++;
                if(count%maxBatch==0){
                    statement.executeBatch();
                    if(!isTransactional) connection.commit();
                    statement = initPrepareStatement(connection,joinTables);
                }
            }
            if(count%maxBatch!=0) {
                statement.executeBatch();
                if(!isTransactional) connection.commit();
            }
            // 打印参数
            ConsoleSqlUtil.consoleParam(arrayList);
        }else {
            for(EntityJoinTable joinTable: joinTables){
                PreparedStatement statement = setUpdate(connection, joinTable);
                statement.executeUpdate();
            }
        }
    }

    /**
     * 初始化预加载的SQL语句
     * */
    private PreparedStatement initPrepareStatement(Connection connection,List<? extends EntityJoinTable> joinTables) throws SQLException {
        String sql = "";
        if(joinTables.size()>0) {
            sql = joinTables.get(0).sqlOne(true);
        }else {
            throw new RuntimeException("无更新语句！");
        }
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    /**
     * 批量更新语句设置参数
     * */
    private List setUpdatePrepare(PreparedStatement statement,EntityJoinTable updateSql) throws SQLException {
        LinkedList objects = new LinkedList<>();
        updateSql.addValue(objects);
        Object[] toArray = objects.toArray(); //提升访问效率
        for (int i = 0; i < toArray.length; i++) {
            Object o = toArray[i];
            if(o instanceof Date){
                java.sql.Date date = new java.sql.Date(((Date) o).getTime());
                statement.setObject(i + 1,date );
            }else statement.setObject(i + 1, o);
        }
        statement.addBatch();
        return objects;
    }

    /**
     * 单个更新设置参数
     * */
    private PreparedStatement setUpdate(Connection con,EntityJoinTable updateSql) throws SQLException {
        String sqlOne = updateSql.sqlOne(true);
        PreparedStatement statement = con.prepareStatement(sqlOne);
        LinkedList objects = new LinkedList<>();
        updateSql.addValue(objects);
        Object[] toArray = objects.toArray(); //提升访问效率
        for (int i = 0; i < toArray.length; i++) {
            Object o = toArray[i];
            if(o instanceof Date){
                java.sql.Date date = new java.sql.Date(((Date) o).getTime());
                statement.setObject(i + 1,date );
            }else statement.setObject(i + 1, o);
        }
        ConsoleSqlUtil.console(sqlOne,updateSql);
        ConsoleSqlUtil.consoleParam(objects);
        return statement;
    }

    @Override
    public void batchUpdateByPrimaryKey(List<EntityParams> params) {
        Connection con = null;
        DataSource dataSource = jdbcTemplate.getDataSource();
        try {
            con=DataSourceUtils.getConnection(dataSource);
            List<EntityParams> entityParams = inteceptorList(params);
            ArrayList<EntityJoinTable> list = new ArrayList<>();
            for (EntityParams params1 : entityParams) {
                EntityUpdateSql updateSql = new EntityParamParse(params1).parseToEntityUpdateSqlWithKeys();
                list.add(updateSql);
            }
            batchOperation(con,list,maxBatch);
        }catch (Exception e){
            throw new RuntimeException("批量根据主键更新", e);
        }finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    @Override
    public void batchDelete(List<EntityParams> params) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        Connection con = null;
        try {
            con=DataSourceUtils.getConnection(dataSource);
            List<EntityParams> entityParams = inteceptorList(params);
            ArrayList<EntityJoinTable> list = new ArrayList<>();
            for (EntityParams params1 : entityParams) {
                EntityDeleteSql updateSql = new EntityParamParse(params1).parseToEntityDeleteSql();
                list.add(updateSql);
            }
            batchOperation(con,list,maxBatch);
        }catch (Exception e){
            throw new RuntimeException("批量删除", e);
        }finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    @Override
    public void batchDeleteByPrimaryKey(List<EntityParams> params){
        DataSource dataSource = jdbcTemplate.getDataSource();
        Connection con = null;
        try {
            con=DataSourceUtils.getConnection(dataSource);
            List<EntityParams> entityParams = inteceptorList(params);
            ArrayList<EntityJoinTable> list = new ArrayList<>();
            for (EntityParams params1 : entityParams) {
                EntityDeleteSql updateSql = new EntityParamParse(params1).parseToEntityDeleteSqlWithKeys();
                list.add(updateSql);
            }
            batchOperation(con,list,maxBatch);
        }catch (Exception e){
            throw new RuntimeException("批量根据主键删除", e);
        }finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * 只适配于单个主键
     * 复合主键，暂时不适用
     * */
    @Override
    public void batchSave(List<EntityParams> params) throws Exception {
        DataSource dataSource = jdbcTemplate.getDataSource();
        List<EntityParams> params2 = inteceptorList(params);
        //1.首先根据主键集合找到所有符合条件的数据
        List<EntityInsert> arrayList = new LinkedList<>();
        Class t= null;
        String primaryKey = "";
        for(EntityParams params1:params2){
            t= Class.forName(params1.getTableClassName());
            List<EntityInsert> entityInserts = ReflectEntity.reflectPrimaryKeys(t, params1.getCondition());
            arrayList.addAll(entityInserts);
            primaryKey = entityInserts.get(0).getColumn();
        }
        List<Object> collect = arrayList.stream().map(item -> item.getValue()).collect(Collectors.toList());
        List byPrimaryKeys = findByPrimaryKeys(t,t, collect);
        //2.根据数据的存在与否，构造出不同的SQL语句
        Connection con = null;
        try {
            con=DataSourceUtils.getConnection(dataSource);
            ArrayList<EntityJoinTable> updates = new ArrayList<>();
            ArrayList<EntityJoinTable> inserts = new ArrayList<>();
            for (EntityParams params1 : params2) {
                boolean exist = false;
                for(Object os : byPrimaryKeys){
                    Map one = JSONObject.parseObject(JSONObject.toJSONString(os));
                    if(one.get(primaryKey).equals(params1.getCondition().getString(primaryKey))){
                        exist = true;
                    }
                }
                if(exist) {
                    EntityUpdateSql updateSql = new EntityParamParse(params1).parseToEntityUpdateSql();
                    updates.add(updateSql);
                }else {
                    EntityInsertSql updateSql = new EntityParamParse(params1).parseToEntityInertSql();
                    inserts.add(updateSql);
                }
            }
            if(updates.size()>0) batchOperation(con,updates,maxBatch);
            if(inserts.size()>0) batchOperation(con,inserts,maxBatch);
        }catch (Exception e){
            throw e;
        }finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * @param data 可以是实体对象，也可以是View对象
     * @param toJavaBean 查询结果转换成为的java对象
     * @param page 分页参数
     * */
    @Override
    public <T, E> CommonPageResp<List<E>> findByEntityPageList(T data, Class<E> toJavaBean, Page page) {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(data);
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }

    @Override
    public <T, E> CommonPageResp<List<E>> findByEntityPageList(T data, Class<E> toJavaBean, List<EntityOrderBy> orderBys, Page page) {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(data);
        entityParams.setOrderBy(orderBys);
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }

    @Override
    public <T, E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass, Class<E> toJavaBean, Page page)  {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }

    /*@Override
    public <T,E> CommonPageResp<List<E>> findEntityPageList(T data, Class<E> toJavaBean, Page page) throws Exception {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(data);
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }*/

    @Override
    public <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition, Page page) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setCondition(condition);
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }

    @Override
    public <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition, List<EntityOrderBy> orderBys, Page page){
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setCondition(condition);
        entityParams.setOrderBy(orderBys);
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }

    @Override
    public <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean, List<EntityOrderBy> orderBys, Page page){
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setOrderBy(orderBys);
        CommonPageResp pageList = findPageList(entityParams, page);
        List listData = (List) pageList.getData();
        if(listData==null) {
            CommonPageResp<List<E>> resp = new CommonPageResp<>();
            resp.setData(new ArrayList<>());
            resp.setCount(0);
            return resp;
        }
        List<E> parseList = ObjectUtil.parseList(listData, toJavaBean);
        pageList.setData(parseList);
        return pageList;
    }

    @Override
    public <T, E> List<E> findListByEntity(T data, Class<E> toJavaBean) {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(data);
        List list = findList(entityParams);
        if(list==null) {
            ArrayList<E> list1 = new ArrayList<>();
            return list1;
        }
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T, E> List<E> findListByEntity(T data, Class<E> toJavaBean, List<EntityOrderBy> orderBys){
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(data);
        entityParams.setOrderBy(orderBys);
        List list = findList(entityParams);
        if(list==null) {
            ArrayList<E> list1 = new ArrayList<>();
            return list1;
        }
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        List list = findList(entityParams);
        if(list==null) {
            ArrayList<E> list1 = new ArrayList<>();
            return list1;
        }
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T, E> List<E> findEntityOnePageList(Class<T> viewClass, Class<E> toJavaBean, JSONObject condition ,List<EntityOrderBy> orderBys,Page page) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setCondition(condition);
        entityParams.setOrderBy(orderBys);
        List<Map<String, Object>> onePageList = findOnePageList(entityParams, page);
        List<E> parseList = ObjectUtil.parseList(onePageList, toJavaBean);
        return parseList;
    }

    @Override
    public <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setCondition(condition);
        List list = findList(entityParams);
        if(list==null) {
            ArrayList<E> list1 = new ArrayList<>();
            return list1;
        }
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition, List<EntityOrderBy> orderBys) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setCondition(condition);
        entityParams.setOrderBy(orderBys);
        List list = findList(entityParams);
        if(list==null) {
            ArrayList<E> list1 = new ArrayList<>();
            return list1;
        }
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean, List<EntityOrderBy> orderBys){
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setOrderBy(orderBys);
        List list = findList(entityParams);
        if(list==null) {
            ArrayList<E> list1 = new ArrayList<>();
            return list1;
        }
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T> long countEntity(Class<T> viewClass, JSONObject condition){
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        entityParams.setCondition(condition);
        long count = count(entityParams);
        return count;
    }

    @Override
    public <T> long countXmlSql(String id, Map params){
        Element xml = EntityCache.getXml(id);
        SqlXmlExpresession expresession = new SqlXmlExpresession(params, xml);
        String sqlOne = expresession.sqlOne(true);
        AbsolutePageSql pageSql = new AbsolutePageSql(sqlOne);
        String countSql;
        try {
         countSql = pageSql.toCountSql(dbType.getDbType(), true);
        }catch (Exception e){
            throw new RuntimeException("CommonServiceImpl.countXmlSql 获取数据库类型错误",e);
        }
        LinkedList objects = new LinkedList<>();
        expresession.addValue(objects);
        Map<String,Object> counts = jdbcTemplate.queryForMap(countSql,objects.toArray());
        ConsoleSqlUtil.console(countSql,InitSqlType.SELECT);
        ConsoleSqlUtil.consoleParam(objects);
        ConsoleSqlUtil.consoleResult(counts);
        long count=Long.parseLong(""+counts.get("__total__"));
        return count;
    }

    @Override
    public <T> long countEntity(T params) {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(params);
        return count(entityParams);
    }

    @Override
    public <T> void insertEntity(T paramData){
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(paramData);
        insert(entityParams);
    }

    @Override
    public <T> void updateEntity(T paramsData) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(paramsData.getClass().getSimpleName());
        JSONObject map = EntityConditionBuilder.buildClass(paramsData);
        entityParams.setCondition(map);
        entityParams.setUpdates(map);
        update(entityParams);
    }

    @Override
    public <T> void updateEntity(T paramsData, JSONObject condition){
        EntityParams entityParams = new EntityParams();
        String simpleName = paramsData.getClass().getSimpleName();
        entityParams.setTableClassName(simpleName);
        JSONObject map = EntityConditionBuilder.buildClass(paramsData);
        entityParams.setCondition(condition);
        entityParams.setUpdates(map);
        update(entityParams);
    }

    @Override
    public <T> void updateEntityByPrimarykey(T paramsData) {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(paramsData);
        updateByPrimaryKey(entityParams);
    }

    @Override
    public <T> void deleteEntity(T paramsData){
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(paramsData);
        delete(entityParams);
    }

    @Override
    public <T> void deleteEntity(Class<T> beanClass, JSONObject condition) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(beanClass.getSimpleName());
        entityParams.setCondition(condition);
        delete(entityParams);
    }

    @Override
    public <T> void deleteEntityByPrimaryKey(T paramsData) {
        EntityParams entityParams = ParamEntityParseUtil.parseToParam(paramsData);
        deleteByPrimaryKey(entityParams);
    }

    @Override
    public <T> void batchEntityInsert(List<T> params) {
        ArrayList<EntityParams> entityParams = new ArrayList<>();
        for(T data:params){
            entityParams.add(ParamEntityParseUtil.parseToParam(data));
        }
        batchInsert(entityParams);
    }

    @Override
    public <T> void batchEntityUpdate(List<T> params){
        ArrayList<EntityParams> entityParams = new ArrayList<>();
        for(T data:params){
            entityParams.add(ParamEntityParseUtil.parseToParam(data));
        }
       batchUpdate(entityParams);
    }

    @Override
    public <T> void batchEntityUpdateByPrimaryKey(List<T> params){
        ArrayList<EntityParams> entityParams = new ArrayList<>();
        for(T data:params){
            entityParams.add(ParamEntityParseUtil.parseToParam(data));
        }
        batchUpdateByPrimaryKey(entityParams);
    }

    @Override
    public <T> void batchEntityDelete(List<T> params){
        ArrayList<EntityParams> entityParams = new ArrayList<>();
        for(T data:params){
            entityParams.add(ParamEntityParseUtil.parseToParam(data));
        }
        batchDelete(entityParams);
    }

    @Override
    public <T> void batchEntityDeleteByPrimaryKey(List<T> params){
        ArrayList<EntityParams> entityParams = new ArrayList<>();
        for(T data:params){
            entityParams.add(ParamEntityParseUtil.parseToParam(data));
        }
        batchDeleteByPrimaryKey(entityParams);
    }

    @Override
    public <T,E> E findByPrimaryKey(Class<T> viewClass,Class<E> toJavaBean, Object key){
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        Map<String, Object> primaryKeys = ReflectEntity.reflectPrimaryKeys(viewClass);
        EntityConditionBuilder builder = EntityConditionBuilder.getInstance();
        for(String pk:primaryKeys.keySet()){
            builder.eq((String) primaryKeys.get(pk),key);
        }
        entityParams.setCondition(builder.combineAnd());
        List list = findList(entityParams);
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        if(parseList.size()>1)
            throw new RuntimeException("结果数大于2条，最多只能返回一条");
        if(parseList.size()>0)
            return parseList.get(0);
        return null;
    }

    @Override
    public <T,E> List<E> findByPrimaryKeys(Class<T> viewClass,Class<E> toJavaBean, List<? extends Object> keys) {
        EntityParams entityParams = new EntityParams();
        entityParams.setTableClassName(viewClass.getSimpleName());
        Map<String, Object> primaryKeys = ReflectEntity.reflectPrimaryKeys(viewClass);
        EntityConditionBuilder builder = EntityConditionBuilder.getInstance();
        for(String pk:primaryKeys.keySet()){
            builder.in((String) primaryKeys.get(pk),keys.toArray());
        }
        entityParams.setCondition(builder.combineAnd());
        List list = findList(entityParams);
        List<E> parseList = ObjectUtil.parseList(list, toJavaBean);
        return parseList;
    }

    @Override
    public <T> void batchSaveEntity(List<T> entitys) throws Exception {
        ArrayList<EntityParams> entityParams = new ArrayList<>();
        for(T data:entitys){
            entityParams.add(ParamEntityParseUtil.parseToParam(data));
        }
        batchSave(entityParams);
    }

    @Override
    public List<? extends Map> findXmlSqlId(String id){
        List<HashMap> sqlId = findXmlSqlId(id, HashMap.class, null);
        return sqlId;
    }

    @Override
    public CommonPageResp<? extends List<? extends Map>> findXmlPageId(String id, Page page){
        CommonPageResp<List<HashMap>> pageId = findXmlPageId(id, HashMap.class, page);
        return pageId;
    }

    @Override
    public <T> CommonPageResp<List<T>> findXmlPageId(String id, Class<T> toJavaBean, Page page) {
        CommonPageResp<List<T>> pageId = findXmlPageId(id, toJavaBean, null, page);
        return pageId;
    }

    @Override
    public <T> CommonPageResp<List<T>> findXmlPageId(String id, Class<T> toJavaBean, Map param, Page page) {
        long xmlCount = countXmlSql(id, param);
        CommonPageResp resp=new CommonPageResp<>();
        resp.setCount(xmlCount);
        if(xmlCount>0) {
            List<T> parseList = findXmlOnePageId(id, toJavaBean, param, page);
            resp.setData(parseList);
        }
        return resp;
    }

    @Override
    public <T> List<T> findXmlOnePageId(String id, Class<T> toJavaBean, Map param, Page page){

        Element xml = EntityCache.getXml(id);
        SqlXmlExpresession expresession = new SqlXmlExpresession(param, xml);
        String sqlOne = expresession.sqlOne(true);
        ArrayList list1 = new ArrayList<>();
        expresession.addValue(list1);
        AbsolutePageSql pageSql = new AbsolutePageSql(sqlOne);
        String countSql= null;
        try {
            countSql = pageSql.toPageSql(page.getPage(), page.getSize(),true, dbType.getDbType());

            pageSql.addValue(page.getPage(), page.getSize(), dbType.getDbType(), list1);
        }catch (Exception e){
            throw new RuntimeException("CommonServiceImpl.findXmlOnePageId 获取数据库类型错误", e);
        }

//            List<T> querys = jdbcTemplate.query(countSql, list1.toArray(),new BeanPropertyRowMapper<>(toJavaBean));
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(countSql, list1.toArray());
        List<T> parseList = ObjectUtil.parseList(mapList, toJavaBean);
        ConsoleSqlUtil.console(countSql, InitSqlType.SELECT);
        ConsoleSqlUtil.consoleParam(list1);
        ConsoleSqlUtil.consoleResult(mapList);

        return parseList;
    }

    @Override
    public <T> T executeXmlId(String id, Class<T> toJavaBean) {
        T t = executeXmlId(id, toJavaBean, new HashMap());
        return t;
    }

    @Override
    public <T> T executeXmlId(String id, Class<T> toJavaBean, Map param) {
        Element xml = EntityCache.getXml(id);
        SqlXmlExpresession expresession = new SqlXmlExpresession(param, xml);
        String sqlOne = expresession.sqlOne(true);
        ArrayList list1 = new ArrayList<>();
        expresession.addValue(list1);
        T object = ObjectUtil.create(toJavaBean);
        if(object instanceof Map){
            Map<String, Object> objectMap = jdbcTemplate.queryForMap(sqlOne, list1.toArray());
            ((Map)object).putAll(objectMap);
        }else {
            object = jdbcTemplate.queryForObject(sqlOne, list1.toArray(), new BeanPropertyRowMapper<T>(toJavaBean){});
        }
        ConsoleSqlUtil.console(sqlOne, InitSqlType.OTHER);
        ConsoleSqlUtil.consoleParam(list1);
        ConsoleSqlUtil.consoleResult(object);
        return object;
    }

    @Override
    public <T> List<T> findXmlSqlId(String id, Class<T> toJavaBean){
        List list = findXmlSqlId(id, toJavaBean, null);
        return list;
    }


    @Override
    public <T> List<T> findXmlSqlId(String id, Class<T> toJavaBean, Map param){
        Element xml = EntityCache.getXml(id);
        SqlXmlExpresession expresession = new SqlXmlExpresession(param, xml);
        String sqlOne = expresession.sqlOne(true);
        ArrayList list1 = new ArrayList<>();
        expresession.addValue(list1);

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sqlOne, list1.toArray());
        ConsoleSqlUtil.console(sqlOne,InitSqlType.SELECT);
        ConsoleSqlUtil.consoleParam(list1);
        ConsoleSqlUtil.consoleResult(mapList);
        List<T> parseList = ObjectUtil.parseList(mapList, toJavaBean);
        return parseList;
    }

    private EntityParams inteceptor(EntityParams params) throws IOException, InvocationTargetException, IllegalAccessException {
        EntityParams params1 = new EntityParams();
        BeanUtils.copyProperties(params,params1);
//        String classPackage = ClassInfoUtil.getPackageByRelativeName(params.getTableClassName(),configure.getBasePackage());
        String simpleName = EntityCache.getClass(params.getTableClassName()).getName();
        params1.setTableClassName(simpleName);
        return params1;
    }

    private List<EntityParams> inteceptorList(List<EntityParams> prs) throws IOException, InvocationTargetException, IllegalAccessException {
        EntityParams params = prs.get(0);
        LinkedList<EntityParams> list = new LinkedList<>();
//        String classPackage = ClassInfoUtil.getPackageByRelativeName(params.getTableClassName(),configure.getBasePackage());
        String simpleName = EntityCache.getClass(params.getTableClassName()).getName();
        for(EntityParams pl: prs){
            EntityParams entityParams = new EntityParams();
            BeanUtils.copyProperties(pl,entityParams);
            entityParams.setTableClassName(simpleName);
            list.add(entityParams);
        }
        return list;
    }
}
