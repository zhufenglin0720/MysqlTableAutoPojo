package com.example.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * @author zfl
 * @create 2022/1/29 11:48
 * @description
 */
public class MysqlCreateJavaClassUtils {

    /**
     * mysql表生成实体模型的java文件后缀
     */
    private final static String MYSQL_TABLE_POJO_CLASS_SUFFIX = ".java";
    /**
     * mysql表生成实体模型的文件夹路径间隔符
     */
    private final static String MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER = "/";
    /**
     * mysql表生成实体模型的文件夹位置
     */
    public final static String MYSQL_TABLE_POJO_LOCATION = System.getProperty("mysqlTableLocation");
    /**
     * java的类名
     */
    private static String MYSQL_POJO_JAVA_CLASS_NAME;
    /**
     * 创建好的java文件名
     */
    private static String MYSQL_POJO_JAVA_CLASS_FILE_NAME;

    public static void createJavaClass(String tableName, ResultSet resultSet, List<String> commentList){
        if(createEntityByMysqlTableName(tableName)){
            //创建文件初始化文件内容
            MysqlCreatePojoInfoUtils.createMysqlPojoClassInfo(MYSQL_POJO_JAVA_CLASS_FILE_NAME,MYSQL_POJO_JAVA_CLASS_NAME,resultSet,commentList);
        }
    }

    /**
     * 根据mysql表名创建实体java类
     * @param tableName 表名
     * @return 是否创建成功
     */
    private static boolean createEntityByMysqlTableName(String tableName){
        assert StringUtils.isNotBlank(tableName);
        String tableEntityName = JavaClassConvertNameUtils.humpNamed(tableName);
        MYSQL_POJO_JAVA_CLASS_NAME = tableEntityName;
        MYSQL_POJO_JAVA_CLASS_FILE_NAME = MYSQL_TABLE_POJO_LOCATION + MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER + tableEntityName + MYSQL_TABLE_POJO_CLASS_SUFFIX;
        //在指定位置创建文
        File file = new File(MYSQL_POJO_JAVA_CLASS_FILE_NAME);
        if(file.exists()){
            return true;
        }else{
            try {
                return file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

}
