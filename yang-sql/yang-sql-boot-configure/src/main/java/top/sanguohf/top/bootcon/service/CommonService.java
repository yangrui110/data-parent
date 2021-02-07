package top.sanguohf.top.bootcon.service;

import com.alibaba.fastjson.JSONObject;
import top.sanguohf.egg.base.EntityOrderBy;
import top.sanguohf.egg.param.EntityParams;
import top.sanguohf.top.bootcon.page.Page;
import top.sanguohf.top.bootcon.resp.CommonPageResp;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CommonService {
    /**
     * @param params
     * @param page
     */
    CommonPageResp findPageList(EntityParams params, Page page);

    /**
     * @param params
     */
    List findList(EntityParams params);

    /**
     * @param params
     */
    long count(EntityParams params);

    /**
     * @param paramData
     */
    void insert(EntityParams paramData);

    /**
     * @param paramsData
     */
    void update(EntityParams paramsData);

    void updateByPrimaryKey(EntityParams paramsData);

    /**
     * @param paramsData
     **/
    void delete(EntityParams paramsData);

    void deleteByPrimaryKey(EntityParams paramsData);

    /**
     * @param params
     */
    void batchInsert(List<EntityParams> params);

    /**
     * @param params
     */
    void batchUpdate(List<EntityParams> params);

    void batchUpdateByPrimaryKey(List<EntityParams> params);

    /**
     * @param params
     */
    void batchDelete(List<EntityParams> params);

    void batchDeleteByPrimaryKey(List<EntityParams> params);

    /**
     * 批量保存方法，1、主键存在，则更新 2、主键不存在，则插入
     */
    void batchSave(List<EntityParams> params) throws Exception ;

    <T,E> CommonPageResp<List<E>> findByEntityPageList(T data, Class<E> toJavaBean, Page page);

    <T,E> CommonPageResp<List<E>> findByEntityPageList(T data, Class<E> toJavaBean,List<EntityOrderBy> orderBys, Page page);

    <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean,  Page page);

    /**
     * 根据condition查找数据
     */
    <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition, Page page);

    /**
     * 增加排序字段
     */
    <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition, List<EntityOrderBy> orderBys, Page page);

    <T,E> CommonPageResp<List<E>> findEntityPageList(Class<T> viewClass,Class<E> toJavaBean, List<EntityOrderBy> orderBys, Page page);

    <T,E> List<E> findListByEntity(T data,Class<E> toJavaBean) ;

    <T,E> List<E> findListByEntity(T data,Class<E> toJavaBean,  List<EntityOrderBy> orderBys) ;
    /**
     * 根据condition查找数据
     */
    <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean) ;
    /**
     * 查找指定页的数据
     * */
    <T,E> List<E> findEntityOnePageList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition,List<EntityOrderBy> orderBys,Page page) ;

    <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition);

    <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean, JSONObject condition, List<EntityOrderBy> orderBys);

    <T,E> List<E> findEntityList(Class<T> viewClass,Class<E> toJavaBean, List<EntityOrderBy> orderBys);

    <T> long countEntity(Class<T> viewClass,JSONObject condition);

    <T> long countXmlSql(String id,Map params);
    /**
     * @param params
     */
    <T> long countEntity(T params);

    /**
     * @param paramData
     */
    <T> void insertEntity(T paramData) ;

    /**
     * @param paramsData
     */
    <T> void updateEntity(T paramsData);
    /**
     * 根据指定条件进行更新
     * */
    <T> void updateEntity(T paramsData,JSONObject condition);

    <T> void updateEntityByPrimarykey(T paramsData);

    /**
     * @param paramsData
     **/
    <T> void deleteEntity(T paramsData);
    /**
     * 根据指定条件删除
     * */
    <T> void deleteEntity(Class<T> beanClass,JSONObject condition);

    <T> void deleteEntityByPrimaryKey(T paramsData);

    /**
     * @param params
     */
    <T> void batchEntityInsert(List<T> params);

    /**
     * @param params
     */
    <T> void batchEntityUpdate(List<T> params);

    <T> void batchEntityUpdateByPrimaryKey(List<T> params);

    <T> void batchEntityDelete(List<T> params);

    <T> void batchEntityDeleteByPrimaryKey(List<T> params);

    /**
     * 根据主键查找到对应的值
     * 适用于主键数目为单个
     */
    <T,E> E findByPrimaryKey(Class<T> viewClass,Class<E> toJavaBean , Object key);

    /**
     * 根据主键查找到对应的值
     * 适用于主键数目为单个
     */
    <T,E> List<E> findByPrimaryKeys(Class<T> viewClass,Class<E> toJavaBean, List<? extends Object> keys);

    <T> void batchSaveEntity(List<T> entitys) throws Exception ;

    List<? extends Map> findXmlSqlId(String id);
    /**
     * 查询具体的SQL语句
     * */
    <T> List<T> findXmlSqlId(String id,Class<T> toJavaBean);
    /**
     * @param param 参数，Map对象
     * */
    <T> List<T> findXmlSqlId(String id, Class<T> toJavaBean, Map param);

    CommonPageResp<? extends List<? extends Map>> findXmlPageId(String id,Page page);

    <T> CommonPageResp<List<T>> findXmlPageId(String id,Class<T> toJavaBean,Page page);
    /**
     * @param page 分页查询传递的分页参数
     * */
    <T> CommonPageResp<List<T>> findXmlPageId(String id,Class<T> toJavaBean,Map param,Page page);
    /**
     * 查找一页的参数
     * */
    <T> List<T> findXmlOnePageId(String id,Class<T> toJavaBean,Map param,Page page);

    <T> T executeXmlId(String id,Class<T> toJavaBean);

    <T> T executeXmlId(String id,Class<T> toJavaBean,Map param);
}
