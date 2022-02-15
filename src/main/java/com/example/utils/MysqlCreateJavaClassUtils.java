package com.example.utils;

import com.example.constants.MybatisGeneratorConstants;
import com.example.utils.pojo.MysqlCreatePojoInfoUtils;
import com.example.utils.service.MysqlGeneratorServiceUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zfl
 * @create 2022/1/29 11:48
 * @description
 */
public class MysqlCreateJavaClassUtils {

    /**
     * 文档注释内容 时间格式字符串
     */
    private static final String SIMPLE_DATE_FORMAT_STR = new SimpleDateFormat().format(new Date());

    /**
     * mysql表生成实体模型的java文件后缀
     */
    public final static String MYSQL_TABLE_POJO_CLASS_SUFFIX = ".java";

    /**
     * mysql表生成实体模型的文件夹位置
     */
    public final static String MYSQL_TABLE_POJO_LOCATION = System.getProperty("mysqlTableLocation");
    /**
     * java的类名
     */
    public static String MYSQL_POJO_JAVA_CLASS_NAME;
    /**
     * 创建好的java文件名
     */
    private static String MYSQL_POJO_JAVA_CLASS_FILE_NAME;

    public static void createJavaClass(String tableName, List<TableInfo> tableInfos, List<String> commentList){
        if(createEntityByMysqlTableName(tableName)){
            //创建文件初始化文件内容
            MysqlCreatePojoInfoUtils.createMysqlPojoClassInfo(MYSQL_POJO_JAVA_CLASS_FILE_NAME,MYSQL_POJO_JAVA_CLASS_NAME,tableInfos,commentList,SIMPLE_DATE_FORMAT_STR);
        }
        //创建service java文件
        MysqlGeneratorServiceUtils.generatorServiceFile(tableName);
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
        MYSQL_POJO_JAVA_CLASS_FILE_NAME = MYSQL_TABLE_POJO_LOCATION + MybatisGeneratorConstants.MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER + tableEntityName + MYSQL_TABLE_POJO_CLASS_SUFFIX;
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
