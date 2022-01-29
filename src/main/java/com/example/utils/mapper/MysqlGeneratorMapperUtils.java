package com.example.utils.mapper;

import com.example.constants.MybatisGeneratorConstants;
import com.example.enums.MysqlColumnTypeEnum;
import com.example.utils.TableInfo;
import com.example.utils.JavaClassConvertNameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author zfl
 * @create 2022/1/29 15:38
 * @description
 */
public class MysqlGeneratorMapperUtils {

    private static final String MAPPER_FILE_LOCATION = System.getProperty("mysqlMapperLocation");

    /**
     * 生成mapper文件
     * @param tableName      表名
     * @param tableInfoList  表数据信息
     */
    public static void generatorMapperFile(String tableName,List<TableInfo> tableInfoList){
        String mapperFilePath = JavaClassConvertNameUtils.mapperFileRelationName(tableName,MAPPER_FILE_LOCATION);
        if(checkFileExist(mapperFilePath)){
            StringBuilder sb = getMapperFileInfo(tableName,
                    JavaClassConvertNameUtils.mapperTypeName(JavaClassConvertNameUtils.humpNamedMapper(tableName),System.getProperty("mysqlServiceLocation")),
                    JavaClassConvertNameUtils.mapperTypeName(JavaClassConvertNameUtils.humpNamed(tableName),System.getProperty("mysqlTableLocation")),
                    tableInfoList);
            FileWriter writer = null;
            try {
                writer = new FileWriter(mapperFilePath);
                writer.write(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(writer != null){
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static StringBuilder getMapperFileInfo(String tableName,String mapperJavaClassRelationName,String javaClassRelationName,List<TableInfo> tableInfoList){
        //创建mapper文件具体类型
        StringBuilder sb = new StringBuilder(MybatisGeneratorConstants.MAPPER_START_LINE);
        sb.append("\r\n");
        sb.append(String.format(MybatisGeneratorConstants.MAPPER_NAME_SPACE_LINE,mapperJavaClassRelationName));
        sb.append("\r\n");
        sb.append(String.format(MybatisGeneratorConstants.MAPPER_RESULT_MAP_START_LINE,javaClassRelationName));
        sb.append("\r\n");
        StringBuilder selectSql = new StringBuilder("    select ");
        for (int i = 0 ; i < tableInfoList.size() ; i++){
            TableInfo info = tableInfoList.get(i);
            MysqlColumnTypeEnum typeEnum = info.getTypeEnum();
            sb.append(String.format(MybatisGeneratorConstants.MAPPER_RESULT_COLUMN_LINE,info.getColumnName(),typeEnum.getTypeName(),info.getAttributeName()));
            sb.append("\r\n");
            selectSql.append(info.getColumnName());
            if(i != tableInfoList.size() - 1){
                selectSql.append(", ");
            }
            //每6个属性进行换行
            if(i != 0 && i % 5 == 0){
                selectSql.append("\r\n");
                selectSql.append("    ");
            }
        }
        sb.append(MybatisGeneratorConstants.MAPPER_RESULT_MAP_END_LINE);
        sb.append("\r\n");
        sb.append(MybatisGeneratorConstants.MAPPER_SELECT_START_LINE);
        sb.append("\r\n");
        sb.append(selectSql);
        sb.append("\r\n");
        sb.append("    from ");
        sb.append(tableName);
        sb.append("\r\n");
        sb.append(MybatisGeneratorConstants.MAPPER_SELECT_END_LINE);
        sb.append("\r\n");
        sb.append(MybatisGeneratorConstants.MAPPER_NAME_SPACE_END_LINE);
        return sb;
    }

    private static boolean checkFileExist(String filePath){
        File file = new File(filePath);
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
