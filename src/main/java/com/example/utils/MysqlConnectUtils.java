package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author zfl
 * @create 2022/1/29 11:39
 * @description
 */
public class MysqlConnectUtils {

    /**
     * mysql用户名
     */
    private static String MYSQL_USER;
    /**
     * mysql连接密码
     */
    private static String MYSQL_PASSWORD;
    /**
     * mysql驱动类
     */
    private final static String MYSQL_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    /**
     * mysql连接语句
     */
    private final static String MYSQL_CONNECT_URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT";


    private static Connection connection;

    private MysqlConnectUtils(){}

    public static void connect(String username,String password,String ip,String port,String database){
        MYSQL_USER = username;
        MYSQL_PASSWORD = password;
        try {
            initConnect(ip, port, database);
            MysqlGetTableInfoUtils.getMysqlTableInfo(connection);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            closeConnect();
        }

    }

    /**
     * 关闭连接
     */
    private static void closeConnect() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化mysql连接
     * @param ip            mysql ip
     * @param port          mysql port
     * @param databaseName  mysql 数据库名
     * @throws Exception
     */
    private static void initConnect(String ip, String port, String databaseName) throws Exception {
        Class.forName(MYSQL_DRIVER_CLASS);
        connection = DriverManager.getConnection(buildMysqlConnectUrl(ip, port, databaseName), MYSQL_USER, MYSQL_PASSWORD);
        if (connection.isClosed()) {
            throw new Exception("数据库连接状态关闭");
        }
    }

    /**
     * 构建mysql连接url
     *
     * @param ip           mysql服务器IP
     * @param port         mysql服务器地址
     * @param databaseName mysql数据库名
     * @return 连接url
     */
    private static String buildMysqlConnectUrl(String ip, String port, String databaseName) {
        return String.format(MYSQL_CONNECT_URL, ip, port, databaseName);
    }

}
