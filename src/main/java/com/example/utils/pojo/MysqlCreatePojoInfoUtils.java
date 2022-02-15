package com.example.utils.pojo;

import com.example.constants.MybatisGeneratorConstants;
import com.example.enums.MysqlColumnTypeEnum;
import com.example.utils.TableInfo;
import com.example.utils.JavaClassConvertNameUtils;
import com.example.utils.MysqlCreateJavaClassUtils;
import com.example.utils.service.MysqlGeneratorServiceUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zfl
 * @create 2022/1/29 12:32
 * @description
 */
public class MysqlCreatePojoInfoUtils {

    private static String simpleDateStr;

    /**
     * pojo java的类名
     */
    private static String MYSQL_POJO_JAVA_CLASS_NAME;
    /**
     * service java的类名
     */
    private static String MYSQL_SERVICE_JAVA_CLASS_NAME;

    private static final String JAVA_CLASS_AUTHOR = System.getProperty("author");

    /**
     * 创建mysql实体类的信息
     * @param fileName
     * @param javaClassName
     * @param tableInfos
     * @param commentList
     */
    public static void createMysqlPojoClassInfo(String fileName, String javaClassName, List<TableInfo> tableInfos, List<String> commentList, String dateStr) {
        simpleDateStr = dateStr;
        MYSQL_POJO_JAVA_CLASS_NAME = javaClassName;
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            createMysqlPojoInfo(writer, tableInfos, commentList);
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

    public static void createMysqlServiceClassInfo(String fileName, String javaClassName){
        MYSQL_SERVICE_JAVA_CLASS_NAME = javaClassName;
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            createMysqlServiceInfo(writer);
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

    public static void createMysqlServiceInfo(FileWriter writer) throws Exception{
        String packageName = JavaClassConvertNameUtils.packageName(MysqlGeneratorServiceUtils.MYSQL_TABLE_SERVICE_LOCATION);
        StringBuilder sb = new StringBuilder();
        //创建包后，需要判断是否需要导包
        sb.append("package ");
        sb.append(packageName);
        sb.append(";\r\n\r\n");
        //导入注解相关
        sb.append(MybatisGeneratorConstants.SPRING_SERVICE_ANNOTATIONS_IMPORTS);
        sb.append("\r\n\r\n");
        //添加java文件的文档注释
        addComments(sb);
        sb.append("\r\n");
        sb.append(MybatisGeneratorConstants.SPRING_SERVICE_ANNOTATIONS);
        sb.append("\r\n");
        sb.append("public interface ");
        sb.append(MYSQL_SERVICE_JAVA_CLASS_NAME);
        sb.append(" {");
        sb.append("\r\n\r\n");
        sb.append("}");
        writer.write(sb.toString());
    }

    /**
     * 创建mysql pojo的java文件
     * @param writer
     * @param tableInfos
     * @param commentList
     * @throws Exception
     */
    public static void createMysqlPojoInfo(FileWriter writer, List<TableInfo> tableInfos, List<String> commentList) throws Exception {
        String packageName = JavaClassConvertNameUtils.packageName(MysqlCreateJavaClassUtils.MYSQL_TABLE_POJO_LOCATION);
        StringBuilder sb = new StringBuilder();
        StringBuilder constructionStr = initConstructionStr();
        StringBuilder getSetStr = new StringBuilder();
        //创建包后，需要判断是否需要导包
        sb.append("package ");
        sb.append(packageName);
        sb.append(";\r\n\r\n");
        //获取表属性数据
        if (commentList != null && !commentList.isEmpty()) {
            //判断是否需要import
            checkAndAddImport(sb, tableInfos);
        }
        //添加java文件的文档注释
        addComments(sb);
        sb.append("\r\n");
        sb.append("public class ");
        sb.append(MYSQL_POJO_JAVA_CLASS_NAME);
        sb.append(" {");
        sb.append("\r\n\r\n");

        if (tableInfos != null && !tableInfos.isEmpty()) {
            createTableAttrs(sb, constructionStr, getSetStr, tableInfos, commentList);
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
     * @param tableInfos mysql表字段属性结果集
     * @throws Exception
     */
    private static void checkAndAddImport(StringBuilder sb,List<TableInfo> tableInfos) {
        List<String> alreadyImportLineList = new ArrayList<>();
        for (TableInfo tableInfo : tableInfos){
            MysqlColumnTypeEnum typeEnum = tableInfo.getTypeEnum();
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
     * @param constructionStr   无参/有参构造字符串
     * @param getSetStr    get/set字符串
     * @param tableInfos   mysql表属性的结果集
     * @param result       mysql表注释的结果集
     */
    private static void createTableAttrs(StringBuilder sb,StringBuilder constructionStr,StringBuilder getSetStr,List<TableInfo> tableInfos,List<String> result){
        //是否添加注释标识
        boolean isCreateComment = true;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if(result.size() != tableInfos.size()){
                isCreateComment = false;
            }
            for (int i = 0 ; i < tableInfos.size() ; i++){
                TableInfo tableInfo = tableInfos.get(i);
                String columnName = tableInfo.getAttributeName();
                MysqlColumnTypeEnum typeEnum = tableInfo.getTypeEnum();
                if(isCreateComment){
                    sb.append("    /**\r\n");
                    sb.append("     * ");
                    sb.append(result.get(i));
                    sb.append("\r\n");
                    sb.append("     */\r\n");
                }
                sb.append("    ");
                sb.append(typeEnum.getStatementModifier());
                sb.append(" ");
                sb.append(columnName);
                sb.append(";\r\n");

                createConstructionStr(constructionStr,typeEnum,i,columnName,tableInfos.size());
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
        if(i == columnCount - 1){
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
