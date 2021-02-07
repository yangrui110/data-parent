package top.sanguohf.top.bootcon.config;

import org.springframework.jdbc.datasource.DataSourceUtils;
import top.sanguohf.egg.constant.DbType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

//@Component
//@Data
public class DataBaseTypeInit {

//    private DbType dbType;

    private DataSource dataSource;

    public DataBaseTypeInit(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DbType getDbType() throws SQLException {
        Connection connection =null ;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            String dataBaseType = metaData.getDatabaseProductName();                  //获取数据库类型
            if(("Microsoft SQL Server").equals(dataBaseType)){
                return DbType.SQL;
            }else if(("HSQL Database Engine").equals(dataBaseType)){
                return DbType.HSQL;
            }else if(("MySQL").equals(dataBaseType)){
                return DbType.MYSQL;
            }else if(("Oracle").equals(dataBaseType)){
                return DbType.ORACLE;
            }
        }finally {
            if(connection !=null) {
                DataSourceUtils.doCloseConnection(connection, dataSource);
            }
        }
//        SqlConfigProperties.getInstance().setDbType(this.dbType);
        return DbType.MYSQL;
    }

}
