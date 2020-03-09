package com.icoding.datasource.utils;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

/**
 * SQL 构建类
 */
public class SqlProvider {

	public String select(Map<String, Object> para) {
		return new SQL() {{
			SELECT(para.get("cols").toString());
			  FROM(para.get("tables").toString());
			 WHERE(para.get("conditions").toString());
		}}.toString();
	}

	public String selectBySql(Map<String, Object> para) {
		return para.get("sql").toString();
	}

}
