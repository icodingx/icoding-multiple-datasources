package com.icoding.datasource.domain;

/**
 * 表单关联业务表
 */
public class Table {

	/** 编号 */
	private Long tableId;

	/** 表名称 */
	private String tableName;

	/** 表描述 */
	private String tableComment;

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

}
