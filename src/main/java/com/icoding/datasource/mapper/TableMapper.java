package com.icoding.datasource.mapper;

import java.util.List;
import java.util.Map;

import com.icoding.datasource.domain.Table;
import com.icoding.datasource.domain.TableColumn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.icoding.datasource.utils.SqlProvider;

/**
 * 表信息 数据层
 */
public interface TableMapper {

    /**
     * 查 MySQL 的表
     */
    List<Table> selectMySQLTables(@Param("keyword") String keyword);

    /**
     * 查 SQLServer 的表
     */
    List<Table> selectSQLServerTables(@Param("keyword") String keyword);

    /**
     * 查 MySQL 的列
     */
    List<TableColumn> selectMySQLTableColumns(@Param("tableName") String tableName);

    /**
     * 查 SQLServer 的列
     */
    List<TableColumn> selectSQLServerTableColumns(@Param("tableName") String tableName);

    /**
     * @param tables      : tab1 a, tab2 b on a.c1 = b.c1
     * @param cols        : c1, c2, ...
     * @param conditions: c1=v1 and c2=v2 or c3=v3
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = "select")
    List<Map<String, Object>> select(@Param("tables") String tables, @Param("cols") String cols, @Param("conditions") String conditions);

    @SelectProvider(type = SqlProvider.class, method = "selectBySql")
    List<Map<String, Object>> selectBySql(@Param("sql") String sql);

    Integer createSQLServerDataTable();

    Integer isExistSQLServerDataTable();

    Integer insertMySQLFormData(Map<String, Object> data);

    Integer updateMySQLFormData(Map<String, Object> data);

    Integer deleteMySQLFormData(Map<String, Object> data);

    Integer insertSQLServerFormXMLData(Map<String, Object> data);

    Integer updateSQLServerFormXMLData(Map<String, Object> data);

    Integer checkSQLServerFormXMLData(String uuid);

}
