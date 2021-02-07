package top.sanguohf.egg.ops;

import top.sanguohf.egg.constant.DbType;

import java.util.List;

/**
 * @project platform
 * @Date 2020/9/1
 * @Auth 杨瑞
 **/
public class AbsolutePageSql {

    private String sql;
    private int indexOfSelect=-1;
    private int order=-1;

    public AbsolutePageSql(String sql) {
        this.sql = sql.trim();
        String defaultSql = this.sql.toUpperCase();
        indexOfSelect=defaultSql.indexOf("SELECT");
        order = defaultSql.lastIndexOf("ORDER BY");
    }

    /** @param page 默认最小值为1
     * @param size
     * */
    public String toPageSql(int page, int size,boolean isPrepare) {
        // TODO: implement
        int start = (page-1);
        StringBuilder builder = new StringBuilder();
        builder.append(this.sql).append(" limit ").append(isPrepare?"?":start).append(",").append(isPrepare?"?":size);
        return builder.toString();
    }


    /** @param page
     * @param size
     * @param dbType
     * */
    public String toPageSql(int page, int size,boolean isPrepare, DbType dbType) {
        // TODO: implement
        if(dbType.getValue().equals(DbType.SQL.getValue())){
            return toSQL(page,size,isPrepare);
        }else if(dbType.getValue().equals(DbType.MYSQL.getValue())){
            return toPageSql(page,size,isPrepare);
        }else if(dbType.getValue().equals(DbType.ORACLE.getValue())){
            return toOracleSql(page,size,isPrepare);
        }
        return toPageSql(page,size,isPrepare);
    }

    private String toSQL(int page, int size,boolean isPrepare){
        // 判断最后是否存在order by

        String select = this.sql.substring(indexOfSelect+6);
        String orderBy = "ORDER BY CURRENT_TIMESTAMP";
        if(order!=-1) orderBy = this.sql.substring(order);
        StringBuilder builder = new StringBuilder();
        builder.append("WITH selectTemp AS (SELECT TOP 100 PERCENT ROW_NUMBER ( ) OVER ( ");
        //定义排序字段
        builder.append(orderBy);

        builder.append(" ) AS __row_number__,").append(select);

        int start = (page-1)*size+1;
        int end = page*size;
        builder.append(") SELECT * FROM selectTemp  WHERE __row_number__ BETWEEN ").append(isPrepare?"?":start).append(" AND ").append(isPrepare?"?":end).append("  ORDER BY __row_number__");
        return builder.toString();
    }

    private String toOracleSql(int page,int size,boolean isPrepare){
        String select = this.sql.substring(indexOfSelect+5);
        StringBuilder builder = new StringBuilder();
        builder.append("with selectTemp as(select rownum AS __rownum__,").append(select);
        int start = (page-1)*size+1;
        int end = page*size;
        builder.append(") select * from selectTemp where __rownum__ >= ").append(isPrepare?"?":start);
        return builder.toString();
    }
    public void addValue(int page, int size, DbType dbType, List list){
        int start = (page-1)*size;
        int end = page*size;
        if(dbType.getValue().equals(DbType.ORACLE.getValue())){
            list.add(end);
            list.add(start);
        }else if(dbType.getValue().equals(DbType.SQL.getValue())){
            list.add(start+1);
            list.add(end);
        }else{
            list.add(start);
            list.add(end);
        }
    }
    /** @param dbType
     **/
    public String toCountSql(DbType dbType,boolean isPrepare) {
        // TODO: implement
        return toCountSql(isPrepare);
    }

    public String toCountSql(boolean isPrepare) {
        // TODO: implement
        String from = this.sql.substring(0,order==-1?this.sql.length():order);
        StringBuilder builder = new StringBuilder();
        builder.append("select count(1) as __total__").append(" from (");
        builder.append(from).append(") total");

        return builder.toString();
    }
}
