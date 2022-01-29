package com.example.utils;

import com.example.constants.MybatisGeneratorConstants;
import com.sun.istack.internal.NotNull;

import java.util.Locale;

/**
 * @author zfl
 * @create 2022/1/29 12:51
 * @description
 */
public class JavaClassConvertNameUtils {

    /**
     * 空字符串
     */
    private final static String EMPTY_STRING = "";
    /**
     * mysql表名下划线
     */
    private final static String MYSQL_SPECIAL_INTERVAL_LETTER = "_";

    /**
     * 驼峰命名类
     * @param tableName 需要转化的单词
     * @return 返回的结果
     */
    public static String humpNamed(@NotNull String tableName){
        StringBuilder tableEntityName = new StringBuilder(EMPTY_STRING);
        //如果包含下划线，则以下划线为分割符，进行驼峰命名
        if(tableName.contains(MYSQL_SPECIAL_INTERVAL_LETTER)){
            String[] split = tableName.split(MYSQL_SPECIAL_INTERVAL_LETTER);
            for (String s : split) {
                tableEntityName.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
            }
        }else{
            //如果不包含下划线,将首字母变成大写即可
            tableEntityName = new StringBuilder(tableName.substring(0,1).toUpperCase() + tableName.substring(1));
        }
        return tableEntityName.toString();
    }

    /**
     * 驼峰命名属性
     * @param attrName 需要转化的单词
     * @return 返回的结果
     */
    public static String humpNamedAttr(@NotNull String attrName){
        StringBuilder tableEntityName = new StringBuilder(EMPTY_STRING);
        //如果包含下划线，则以下划线为分割符，进行驼峰命名
        if(attrName.contains(MYSQL_SPECIAL_INTERVAL_LETTER)){
            String[] split = attrName.split(MYSQL_SPECIAL_INTERVAL_LETTER);
            for (int i = 0 ; i < split.length ; i++) {
                if(i == 0){
                    tableEntityName.append(split[i].substring(0, 1).toLowerCase()).append(split[i].substring(1));
                }else{
                    tableEntityName.append(split[i].substring(0, 1).toUpperCase()).append(split[i].substring(1));
                }

            }
        }else{
            //如果不包含下划线,将首字母变成大写即可
            tableEntityName = new StringBuilder(attrName.substring(0,1).toLowerCase() + attrName.substring(1));
        }
        return tableEntityName.toString();
    }

    public static String humpNamedMapper(@NotNull String tableName){
        return humpNamed(tableName) + MybatisGeneratorConstants.MYSQL_MAPPER_JAVA_CLASS_END;
    }

    /**
     * 首字母大写
     * @param key 需要转化的单词
     * @return 返回的结果
     */
    public static String firstLetterUpper(@NotNull String key){
        return key.substring(0,1).toUpperCase() + key.substring(1);
    }

    public static String packageName(@NotNull String fileLocation){
        return fileLocation.substring(14).replaceAll(MybatisGeneratorConstants.MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER, MybatisGeneratorConstants.MYSQL_TABLE_POJO_CLASS_NAME_INTERVAL_LETTER);
    }

    public static String mapperTypeName(@NotNull String javaClassName, @NotNull String mapperFileLocation){
        return packageName(mapperFileLocation) + MybatisGeneratorConstants.MYSQL_TABLE_POJO_CLASS_NAME_INTERVAL_LETTER + javaClassName;
    }

    public static String mapperFileRelationName(@NotNull String tableName , @NotNull String mapperFileLocation){
        return mapperFileLocation + MybatisGeneratorConstants.MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER + humpNamed(tableName) + MybatisGeneratorConstants.MAPPER_SUFFIX;
    }
}
