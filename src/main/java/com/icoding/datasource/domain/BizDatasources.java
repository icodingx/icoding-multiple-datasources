package com.icoding.datasource.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 链接数据源对象 biz_datasources
 *
 * @author icoding
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizDatasources {
    /**
     * 编号
     */
    private Long id;

    private String dsName;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * PORT
     */
    private Integer dbPort;

    /**
     * IP
     */
    private String dbIp;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 连接参数
     */
    private String dbParams;

    /**
     * 用户名
     */
    private String dbUsername;

    /**
     * 密码
     */
    private String dbPassword;

    /**
     * 状态（0正常 1禁用）
     */
    private Integer status;

    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxWait;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
