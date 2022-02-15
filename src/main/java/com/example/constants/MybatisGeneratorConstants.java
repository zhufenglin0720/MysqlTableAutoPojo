package com.example.constants;

/**
 * @author zfl
 * @create 2022/1/29 16:17
 * @description
 */
public class MybatisGeneratorConstants {

    public final static String MAPPER_START_LINE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">";

    public static String MAPPER_NAME_SPACE_LINE = "<mapper namespace=\"%s\">";

    public static String MAPPER_RESULT_MAP_START_LINE = "  <resultMap id=\"BaseResultMap\" type=\"%s\">";

    public static String MAPPER_RESULT_COLUMN_LINE = "    <result column=\"%s\" jdbcType=\"%s\" property=\"%s\" />";

    public static final String MAPPER_RESULT_MAP_END_LINE = "  </resultMap>";

    public static final String MAPPER_SELECT_START_LINE = "  <select id=\"selectAll\" resultMap=\"BaseResultMap\">";

    public static final String MAPPER_SELECT_END_LINE = "  </select>";

    public static final String MAPPER_NAME_SPACE_END_LINE = "</mapper>";

    public final static String MYSQL_MAPPER_JAVA_CLASS_END = "Mapper";

    public final static String MAPPER_SUFFIX = "Mapper.xml";
    /**
     * mysql表生成实体模型的文件夹路径间隔符
     */
    public final static String MYSQL_TABLE_POJO_LOCATION_INTERVAL_LETTER = "/";
    /**
     * mysql表生成实体模型的文件夹路径间隔符
     */
    public final static String MYSQL_TABLE_POJO_CLASS_NAME_INTERVAL_LETTER = ".";
    /**
     * spring service 注解
     */
    public final static String SPRING_SERVICE_ANNOTATIONS_IMPORTS = "import org.springframework.stereotype.Service;";
    public final static String SPRING_SERVICE_ANNOTATIONS = "@Service";
}
