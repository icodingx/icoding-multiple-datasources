package com.icoding.datasource.service;

import java.util.List;
import java.util.Map;

import com.icoding.datasource.domain.Table;
import com.icoding.datasource.domain.TableColumn;

/**
 * 业务 服务层
 */
public interface ITableService {

	/** 查 MySQL 的表 */
	public List<Table> selectMySQLTables(String keyword);

	/** 查 SQLServer 的表 */
	public List<Table> selectSQLServerTables(String ds, String keyword);

	/** 查 MySQL 的列 */
	public List<TableColumn> selectMySQLTableColumns(String tableName);

	/** 查 SQLServer 的列 */
	public List<TableColumn> selectSQLServerTableColumns(String ds, String tableName);

	List<Map<String, Object>> select(String ds, String tables, String cols, String conditions);

	List<Map<String, Object>> selectBySql(String ds, String sql);

	/** 在从数据库创建 存储表单数据的 table */
	public Integer createSQLServerDataTable(String ds);

	public Integer isExistSQLServerDataTable(String ds);

	/** 中间数据，不指定数据源 */
	public Integer saveMySQLFormData(Map<String, Object> data);
	/** 业务数据，需要指定数据源 */
	public Integer saveSQLServerFormXMLData(String ds, Map<String, Object> data);

	public Integer checkSQLServerFormXMLData(String dsid, String string);

	public Integer deleteMySQLFormData(Map<String, Object> data);

}
