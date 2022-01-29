package com.example.utils;

import com.sun.istack.internal.NotNull;

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
     * 驼峰命名
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
     * 首字母大写
     * @param key 需要转化的单词
     * @return 返回的结果
     */
    public static String firstLetterUpper(@NotNull String key){
        return key.substring(0,1).toUpperCase() + key.substring(1);
    }
}
