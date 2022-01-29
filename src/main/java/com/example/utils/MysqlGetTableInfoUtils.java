package com.example.utils;

import com.example.enums.MysqlColumnTypeEnum;
import com.example.utils.mapper.MysqlGeneratorMapperUtils;
import com.example.utils.pool.ThreadPoolUtils;
import com.example.utils.service.MysqlGeneratorServiceUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zfl
 * @create 2022/1/29 11:46
 * @description
 */
public class MysqlGetTableInfoUtils {

    /**
     * mysql获取表名的字段
     */
    private final static String[] MYSQL_META_TABLE_KEY_ARRAY = {"TABLE"};
    /**
     * 需要忽略的表名集合
     */
    private final static List<String> MYSQL_IGNORE_CREATE_TABLE_NAME_ARRAY = Arrays.asList("sys_config","show_log");
    /**
     * mysql获取表名的key
     */
    private final static String MYSQL_META_TABLE_NAME_KEY = "TABLE_NAME";

    private static ResultSet resultSet = null;

    private MysqlGetTableInfoUtils(){}

    public static void getMysqlTableInfo(Connection connection){
        try {
            //获取数据库信息
            DatabaseMetaData metaData = connection.getMetaData();
            //获取所有的表
            ResultSet tables = metaData.getTables(null, null, null, MYSQL_META_TABLE_KEY_ARRAY);
            //遍历
            while (tables.next()){
                //获取表名
                String tableName = tables.getString(MYSQL_META_TABLE_NAME_KEY);
                if(MYSQL_IGNORE_CREATE_TABLE_NAME_ARRAY.contains(tableName)){
                    continue;
                }
                List<String> commentList = getMysqlCommentResult(tableName, connection);
                List<TableInfo> tableInfos = getMysqlTableAttrList(tableName, connection);
                //将新建mapper service文件的步骤移动至线程池里执行 TODO 线程池执行有问题
//                ThreadPoolUtils.instance.execute(() -> {
                    //生成pojo文件
                    MysqlCreateJavaClassUtils.createJavaClass(tableName,tableInfos,commentList);
                    //生成service文件，并封装对应的查询方法
                    MysqlGeneratorServiceUtils.generatorServiceFile();
                    //生成mapper文件，并封装固定的查询语句
                    MysqlGeneratorMapperUtils.generatorMapperFile(tableName, tableInfos);
//                });
            }
            System.out.println("-----end------");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeResultSet();
        }
    }

    /**
     * 获取mysql属性注释结果集
     * @param tableName 表名
     * @return 所有字段的注释
     * @throws Exception
     */
    private static List<String> getMysqlCommentResult(String tableName,Connection connection){
        //保证返回结果有顺序
        List<String> result = new LinkedList<>();
        String sql = "select * from " + tableName;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery("show full columns from " + tableName);
            while (resultSet.next()){
                result.add(resultSet.getString("Comment"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取musql 表的信息
     * @param tableName 表名
     * @return 结果集
     */
    private static List<TableInfo> getMysqlTableAttrList(String tableName, Connection connection){
        //保证有序性
        List<TableInfo> result = new LinkedList<>();
        String sql = "select * from " + tableName;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            ResultSetMetaData data = resultSet.getMetaData();
            int columnCount = data.getColumnCount();
            if(columnCount > 0){
                for (int i = 1 ; i <= columnCount ; i++){
                    MysqlColumnTypeEnum typeEnum;
                    try {
                        typeEnum = MysqlColumnTypeEnum.valueOf(data.getColumnTypeName(i));
                    }catch (Exception e){
                        System.out.println(data.getColumnName(i) + "--" + data.getColumnTypeName(i));
                        continue;
                    }
                    result.add(new TableInfo(data.getColumnName(i),JavaClassConvertNameUtils.humpNamedAttr(data.getColumnName(i)),typeEnum));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static void closeResultSet(){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
