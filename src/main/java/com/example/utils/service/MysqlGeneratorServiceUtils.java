package com.example.utils.service;

import com.example.constants.MybatisGeneratorConstants;
import com.example.utils.JavaClassConvertNameUtils;
import com.example.utils.MysqlCreateJavaClassUtils;
import com.example.utils.pojo.MysqlCreatePojoInfoUtils;
import java.io.File;


/**
 * @author zfl
 * @create 2022/1/29 15:45
 * @description
 */
public class MysqlGeneratorServiceUtils {

    public static String MYSQL_SERVICE_JAVA_CLASS_NAME;
    public static String MYSQL_SERVICE_JAVA_CLASS_FILE_NAME;
    public static final String MYSQL_TABLE_SERVICE_LOCATION = System.getProperty("mysqlServiceLocation");
    private static final String MYSQL_TABLE_SERVICE_CLASS_SUFFIX = "Service";

    public static void generatorServiceFile(String tableName){
        if(createEntityByMysqlTableName(tableName)){
            //创建文件初始化文件内容
            MysqlCreatePojoInfoUtils.createMysqlServiceClassInfo(MYSQL_SERVICE_JAVA_CLASS_FILE_NAME,MYSQL_SERVICE_JAVA_CLASS_NAME);
        }
    }

    /**
     * 根据mysql表名创建 dao java类
     * @param tableName 表名
     * @return 是否创建成功
     */
    private static boolean createEntityByMysqlTableName(String tableName){
        String tableEntityName = JavaClassConvertNameUtils.humpNamed(tableName);
        MYSQL_SERVICE_JAVA_CLASS_NAME = tableEntityName + MYSQL_TABLE_SERVICE_CLASS_SUFFIX;
        MYSQL_SERVICE_JAVA_CLASS_FILE_NAME = MYSQL_TABLE_SERVICE_LOCATION + MybatisGeneratorConstants.MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER
                                                    + MYSQL_SERVICE_JAVA_CLASS_NAME + MysqlCreateJavaClassUtils.MYSQL_TABLE_POJO_CLASS_SUFFIX;
        //在指定位置创建文
        File file = new File(MYSQL_SERVICE_JAVA_CLASS_FILE_NAME);
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
