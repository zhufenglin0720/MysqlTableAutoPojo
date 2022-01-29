package com.example.enums;

/**
 * @author zfl
 * @create 2022/1/28 14:58
 * @description
 */
public enum MysqlColumnTypeEnum {
    /**
     * int类型
     */
    INT("INT", "private Integer", "Integer",""),
    /**
     * VARCHAR
     */
    VARCHAR("VARCHAR", "private String","String",""),
    /**
     * DATETIME
     */
    DATETIME("DATETIME", "private Date", "Date","import java.util.Date;"),
    /**
     * DOUBLE
     */
    DOUBLE("DOUBLE","private Double","Double",""),
    /**
     * DATE
     */
    DATE("DATE","private Date","Date","import java.util.Date;"),

    ;

    /**
     * mysql类型名
     */
    private String typeName;
    /**
     * 申明的修饰符
     */
    private String statementModifier;
    /**
     * import语句
     */
    private String importLine;

    private String javaTypeName;

    MysqlColumnTypeEnum(String typeName, String statementModifier, String javaTypeName, String importLine){
        this.typeName = typeName;
        this.statementModifier = statementModifier;
        this.javaTypeName = javaTypeName;
        this.importLine = importLine;
    }

    public String getJavaTypeName() {
        return javaTypeName;
    }

    public void setJavaTypeName(String javaTypeName) {
        this.javaTypeName = javaTypeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatementModifier() {
        return statementModifier;
    }

    public void setStatementModifier(String statementModifier) {
        this.statementModifier = statementModifier;
    }

    public String getImportLine() {
        return importLine;
    }

    public void setImportLine(String importLine) {
        this.importLine = importLine;
    }
}
