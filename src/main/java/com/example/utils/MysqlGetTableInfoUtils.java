package com.example.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                getMysqlTableAttrList(tableName,connection);
                MysqlCreateJavaClassUtils.createJavaClass(tableName,resultSet,commentList);
            }
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
    private static void getMysqlTableAttrList(String tableName,Connection connection){
        String sql = "select * from " + tableName;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
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
