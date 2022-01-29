package com.example.utils;

import com.example.enums.MysqlColumnTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zfl
 * @create 2022/1/29 12:32
 * @description
 */
public class MysqlCreatePojoInfoUtils {

    private static String simpleDateStr;

    /**
     * java的类名
     */
    private static String MYSQL_POJO_JAVA_CLASS_NAME;

    private static final String JAVA_CLASS_AUTHOR = System.getProperty("author");

    /**
     * 创建mysql实体类的信息
     * @param fileName
     * @param javaClassName
     * @param resultSet
     * @param commentList
     */
    public static void createMysqlPojoClassInfo(String fileName,String javaClassName,ResultSet resultSet, List<String> commentList) {
        simpleDateStr = new SimpleDateFormat().format(new Date());
        MYSQL_POJO_JAVA_CLASS_NAME = javaClassName;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            createMysqlPojoInfo(writer, resultSet, commentList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建mysql pojo的java文件
     * @param writer
     * @param resultSet
     * @param commentList
     * @throws Exception
     */
    public static void createMysqlPojoInfo(BufferedWriter writer, ResultSet resultSet, List<String> commentList) throws Exception {
        String packageName = MysqlCreateJavaClassUtils.MYSQL_TABLE_POJO_LOCATION.substring(14).replaceAll("/", ".");
        StringBuilder sb = new StringBuilder();
        StringBuilder constructionStr = initConstructionStr();
        StringBuilder getSetStr = new StringBuilder();
        //创建包后，需要判断是否需要导包
        sb.append("package ");
        sb.append(packageName);
        sb.append(";\r\n\r\n");
        //获取表属性数据
        ResultSetMetaData data = resultSet.getMetaData();
        if (data != null) {
            //判断是否需要import
            checkAndAddImport(sb, data);
        }
        //添加java文件的文档注释
        addComments(sb);
        sb.append("\r\n");
        sb.append("public class ");
        sb.append(MYSQL_POJO_JAVA_CLASS_NAME);
        sb.append(" {");
        sb.append("\r\n\r\n");

        if (data != null) {
            createTableAttrs(sb, constructionStr, getSetStr, data, commentList);
        }
        sb.append("\r\n");
        sb.append(constructionStr);
        sb.append("\r\n");
        sb.append(getSetStr);
        sb.append("\r\n");
        sb.append("}");
        writer.write(sb.toString());
    }

    /**
     * 检查mysql字段类型是否需要import 并拼接语句
     * @param sb 字符串
     * @param data mysql表字段属性结果集
     * @throws Exception
     */
    private static void checkAndAddImport(StringBuilder sb,ResultSetMetaData data) throws Exception{
        List<String> alreadyImportLineList = new ArrayList<>();
        int columnCount = data.getColumnCount();
        for (int i = 1 ; i <= columnCount ; i++){
            MysqlColumnTypeEnum typeEnum;
            try {
                typeEnum = MysqlColumnTypeEnum.valueOf(data.getColumnTypeName(i));
            }catch (Exception e){
                System.out.println(data.getColumnName(i) + "--" + data.getColumnTypeName(i));
                continue;
            }
            String importLine = typeEnum.getImportLine();
            if(StringUtils.isNotBlank(importLine) && !alreadyImportLineList.contains(importLine)){
                alreadyImportLineList.add(importLine);
                sb.append(importLine);
                sb.append("\r\n");
            }
        }
        sb.append("\r\n");
    }

    private static StringBuilder initConstructionStr() {
        StringBuilder sb = new StringBuilder();
        sb.append("    public ");
        sb.append(MYSQL_POJO_JAVA_CLASS_NAME);
        sb.append("(){ }\r\n");
        sb.append("\r\n");
        sb.append("    public ");
        sb.append(MYSQL_POJO_JAVA_CLASS_NAME);
        sb.append("(");
        return sb;
    }

    /**
     * 添加文档注释
     * @param sb 字符串
     * @throws Exception
     */
    private static void addComments(StringBuilder sb){
        sb.append("/**\r\n");
        sb.append(" * @author ");
        sb.append(JAVA_CLASS_AUTHOR);
        sb.append(" \r\n");
        sb.append(" * @create ");
        sb.append(simpleDateStr);
        sb.append("\r\n");
        sb.append(" * @description \r\n");
        sb.append(" */");
    }

    /**
     * 拼接java类的所有属性
     * @param sb     字符串
     * @param data   mysql表属性的结果集
     * @param result mysql表注释的结果集
     */
    private static void createTableAttrs(StringBuilder sb,StringBuilder constructionStr,StringBuilder getSetStr,ResultSetMetaData data,List<String> result){
        //是否添加注释标识
        boolean isCreateComment = true;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int columnCount = data.getColumnCount();
            if(result.size() != columnCount){
                isCreateComment = false;
            }
            for (int i = 1 ; i <= columnCount ; i++){
                String columnName = data.getColumnName(i);
                MysqlColumnTypeEnum typeEnum;
                try {
                    typeEnum = MysqlColumnTypeEnum.valueOf(data.getColumnTypeName(i));
                }catch (Exception e){
                    System.out.println(columnName + "--" + data.getColumnTypeName(i));
                    return;
                }
                if(isCreateComment){
                    sb.append("    /**\r\n");
                    sb.append("     * ");
                    sb.append(result.get(i - 1));
                    sb.append("\r\n");
                    sb.append("     */\r\n");
                }
                sb.append("    ");
                sb.append(typeEnum.getStatementModifier());
                sb.append(" ");
                sb.append(columnName);
                sb.append(";\r\n");

                createConstructionStr(constructionStr,typeEnum,i,columnName,columnCount);

                createGetterAndSetterStr(getSetStr,columnName,typeEnum);

                stringBuilder.append("        ");
                stringBuilder.append("this.");
                stringBuilder.append(columnName);
                stringBuilder.append(" = ");
                stringBuilder.append(columnName);
                stringBuilder.append(";\r\n");
            }
            constructionStr.append(stringBuilder);
            constructionStr.append("    }\r\n");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createConstructionStr(StringBuilder constructionStr,MysqlColumnTypeEnum typeEnum,int i,String columnName,int columnCount){
        constructionStr.append(typeEnum.getJavaTypeName());
        constructionStr.append(" ");
        constructionStr.append(columnName);
        if(i == columnCount){
            constructionStr.append("){");
            constructionStr.append("\r\n");
        }else{
            constructionStr.append(",");
        }
    }

    private static void createGetterAndSetterStr(StringBuilder getSetStr,String columnName,MysqlColumnTypeEnum typeEnum){
        getSetStr.append("    public void set");
        getSetStr.append(JavaClassConvertNameUtils.firstLetterUpper(columnName));
        getSetStr.append("(");
        getSetStr.append(typeEnum.getJavaTypeName());
        getSetStr.append(" ");
        getSetStr.append(columnName);
        getSetStr.append("){\r\n");
        getSetStr.append("        this.");
        getSetStr.append(columnName);
        getSetStr.append(" = ");
        getSetStr.append(columnName);
        getSetStr.append(";\r\n");
        getSetStr.append("    }\r\n");
        getSetStr.append("\r\n");
        getSetStr.append("    public ");
        getSetStr.append(typeEnum.getJavaTypeName());
        getSetStr.append(" get");
        getSetStr.append(JavaClassConvertNameUtils.firstLetterUpper(columnName));
        getSetStr.append("(){\r\n");
        getSetStr.append("        return ");
        getSetStr.append(columnName);
        getSetStr.append(";\r\n");
        getSetStr.append("    }\r\n");
        getSetStr.append("\r\n");
    }
}
