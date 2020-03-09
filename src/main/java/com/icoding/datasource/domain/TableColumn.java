package com.icoding.datasource.domain;

/**
 * 表单关联业务表 字段信息
 */
public class TableColumn {

	/** 列名称 */
	private String columnName;

	/** 列描述 */
	private String columnComment;

	/** 列类型 */
	private String columnType;

	/** 是否主键（1是） */
	private String isPk;
	/** 是否自增（1是） */
	private String isIncrement;

	/** 是否必填（1是） */
	private String isRequired;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getIsPk() {
		return isPk;
	}

	public void setIsPk(String isPk) {
		this.isPk = isPk;
	}

	public String getIsIncrement() {
		return isIncrement;
	}

	public void setIsIncrement(String isIncrement) {
		this.isIncrement = isIncrement;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	
}
