package com.icoding.datasource.service.impl;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icoding.datasource.annotation.DataSource;
import com.icoding.datasource.domain.Table;
import com.icoding.datasource.domain.TableColumn;
import com.icoding.datasource.enums.DataSourceType;
import com.icoding.datasource.mapper.TableMapper;
import com.icoding.datasource.service.ITableService;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TableServiceImpl implements ITableService {

	private final TableMapper tableMapper;

	@Override
	public List<Table> selectMySQLTables(String keyword) {
		return tableMapper.selectMySQLTables(keyword);
	}

	@Override
	public List<TableColumn> selectMySQLTableColumns(String tableName) {
		return tableMapper.selectMySQLTableColumns(tableName);
	}

	@Override
	public Integer saveMySQLFormData(Map<String, Object> data) {
		Integer result = 0;
		if(null == data.get("id")) {
			result = tableMapper.insertMySQLFormData(data);
		} else {
			result = tableMapper.updateMySQLFormData(data);
		}
		return result;
	}

	@Override
	public Integer deleteMySQLFormData(Map<String, Object> data) {
		return tableMapper.deleteMySQLFormData(data);
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public List<Table> selectSQLServerTables(String ds, String keyword) {
		return tableMapper.selectSQLServerTables(keyword);
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public List<TableColumn> selectSQLServerTableColumns(String ds, String tableName) {
		return tableMapper.selectSQLServerTableColumns(tableName);
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public List<Map<String, Object>> select(String ds, String tables, String cols, String conditions) {
		return tableMapper.select(tables, cols, conditions);	
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public List<Map<String, Object>> selectBySql(String ds, String sql) {
		return tableMapper.selectBySql(sql);
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public Integer createSQLServerDataTable(String ds) {
		return tableMapper.createSQLServerDataTable();
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public Integer isExistSQLServerDataTable(String ds) {
		return tableMapper.isExistSQLServerDataTable();
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public Integer saveSQLServerFormXMLData(String ds, Map<String, Object> data) {
//		Integer result = 0;
//		if(null == data.get("id")) {
//			result = tableMapper.insertSQLServerFormXMLData(data);
//		} else {
//			result = tableMapper.updateSQLServerFormXMLData(data);
//		}
		return tableMapper.insertSQLServerFormXMLData(data);
	}

	@Override @DataSource(DataSourceType.SLAVE)
	public Integer checkSQLServerFormXMLData(String ds, String uuid) {
		return tableMapper.checkSQLServerFormXMLData(uuid);
	}

}
