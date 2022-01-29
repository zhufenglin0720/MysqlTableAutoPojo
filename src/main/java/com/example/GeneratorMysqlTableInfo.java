package com.example;

import com.example.utils.MysqlConnectUtils;

/**
 * @author zfl
 * @create 2022/1/29 11:41
 * @description
 */
public class GeneratorMysqlTableInfo {

    /**
     * mysql用户名
     */
    private final static String MYSQL_USER = "root";
    /**
     * mysql连接密码
     */
    private final static String MYSQL_PASSWORD = "123456";
    /**
     * mysql连接IP
     */
    private final static String MYSQL_IP = "localhost";
    /**
     * mysql连接端口
     */
    private final static String MYSQL_PORT = "3306";
    /**
     * mysql数据库名
     */
    private final static String MYSQL_DATABASE = "fl";
    /**
     * 文档注释的作者
     */
    private final static String JAVA_CLASS_AUTHOR = "zfl";
    /**
     * mysql表生成实体模型的文件夹位置,相对路径
     */
    private final static String MYSQL_TABLE_POJO_LOCATION = "src/main/java/com/example/pojo";

    public static void main(String[] args) {
        System.getProperties().setProperty("author",JAVA_CLASS_AUTHOR);
        System.getProperties().setProperty("mysqlTableLocation",MYSQL_TABLE_POJO_LOCATION);
        MysqlConnectUtils.connect(MYSQL_USER,MYSQL_PASSWORD,MYSQL_IP,MYSQL_PORT,MYSQL_DATABASE);
    }

}
