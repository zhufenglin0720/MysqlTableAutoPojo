package com.example.utils;

import com.example.enums.MysqlColumnTypeEnum;

/**
 * @author zfl
 * @create 2022/1/29 16:01
 * @description
 */
public class TableInfo {
    /**
     * 字段名
     */
    private String columnName;
    /**
     * java属性名
     */
    private String attributeName;
    /**
     * 字段java枚举类
     */
    private MysqlColumnTypeEnum typeEnum;

    public TableInfo(){}

    public TableInfo(String columnName,String attributeName,MysqlColumnTypeEnum typeEnum){
        this.columnName = columnName;
        this.attributeName = attributeName;
        this.typeEnum = typeEnum;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public MysqlColumnTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(MysqlColumnTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
}
