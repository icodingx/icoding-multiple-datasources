package com.icoding.datasource.pool;

import com.alibaba.druid.pool.DruidDataSource;
import com.icoding.datasource.domain.BizDatasources;
import com.icoding.datasource.enums.DataSourceType;
import com.icoding.datasource.spring.SpringContextProcessor;
import org.springframework.beans.BeansException;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源
 * @author icoding
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private List<Map<String, Object>> dataSources = new ArrayList<>();
    private Map<Object, Object> targetDataSources;
    private Integer authNum = 1;

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources, Integer authNum) {
        logger.info("开启多数据源模式...");
        Map<String, Object> ds = new HashMap<>();
        ds.put("label", DataSourceType.MASTER.name());
        ds.put("value", -1L);
        ds.put("did", -1L);
        dataSources.add(ds);

        logger.debug("授权数据源数:" + authNum);
        this.authNum = authNum;
        this.targetDataSources = targetDataSources;
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

    public boolean removeSlaveDataSource(String dsName) {
        try {
            this.targetDataSources.remove(dsName);
            Map<String, Object> removedDataSource = null;
            for(Map<String, Object> ds : dataSources) {
                if(ds.get("label").equals(dsName)) {
                    removedDataSource = ds;
                    break;
                }
            }
            dataSources.remove(removedDataSource);
            logger.info("数据源["+dsName+"]删除成功！");
            DruidDataSource masterDataSource = SpringContextProcessor.getBean("masterDataSource");
//			this.targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
            super.setDefaultTargetDataSource(masterDataSource);
            super.setTargetDataSources(this.targetDataSources);
            super.afterPropertiesSet();
        } catch (Exception e) {
            logger.error("数据源["+dsName+"]删除失败！", e);
            return false;
        }
        return true;
    }

    /**
     * 只支持 MySQL ，MSSQL
     * @param dbInfo
     * @return
     */
    public boolean reloadDataSources(BizDatasources dbInfo) {
        try {
            // 销毁数据源，释放资源
            try {
                this.targetDataSources.remove(dbInfo.getDsName());
                Map<String, Object> removedDataSource = null;
                for(Map<String, Object> ds : dataSources) {
                    if(ds.get("label").equals(dbInfo.getDsName())) {
                        removedDataSource = ds;
                        SpringContextProcessor.removeBean("slaveDataSource_" + dbInfo.getDsName());
                        logger.info("数据源["+dbInfo.getDsName()+"]销毁成功！");
                        break;
                    }
                }
                dataSources.remove(removedDataSource);
            } catch (Exception e) {
                logger.error("数据源["+dbInfo.getDsName()+"]销毁失败！", e);
            }

            // 注册数据源
            if(0 == dbInfo.getStatus()) {
                if(this.targetDataSources.keySet().contains(dbInfo.getDsName())) {
                    throw new RuntimeException("数据源名称重复，请确认!");
                }
                DruidDataSource slaveDataSource = SpringContextProcessor.registerBean("slaveDataSource_"+dbInfo.getDsName(), DruidDataSource.class, dataSourceProperty(dbInfo), null);
                // 默认连接池配置
                slaveDataSource = SpringContextProcessor.getBean(DruidProperties.class).dataSource(slaveDataSource);
                // 动态连接池配置
                slaveDataSource = dynamicConfig(slaveDataSource, dbInfo);

                Map<String, Object> ds = new HashMap<>();
                ds.put("label", dbInfo.getDsName());
                ds.put("value", dbInfo.getDsName());
                ds.put("did", dbInfo.getId());
                ds.put("ds", slaveDataSource);
                dataSources.add(ds);

                this.targetDataSources.put(dbInfo.getDsName(), slaveDataSource);
                logger.debug("动态数据源["+dbInfo.getDsName()+"]注册成功!");
            }

            DruidDataSource masterDataSource = SpringContextProcessor.getBean("masterDataSource");
//			this.targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
            super.setDefaultTargetDataSource(masterDataSource);
            super.setTargetDataSources(this.targetDataSources);
            super.afterPropertiesSet();
        } catch (BeansException e) {
            logger.error("数据源加载失败!", e);
            return false;
        }
        return true;
    }

    private DruidDataSource dynamicConfig(DruidDataSource slaveDataSource, BizDatasources dbInfo) {

        if(dbInfo.getDbType().contains("sqlserver")) {
            logger.debug("----------------sqlserver datasource set validation query: select 1----------");
            slaveDataSource.setValidationQuery("SELECT 1");
        }
        if(null != dbInfo.getInitialSize() && dbInfo.getInitialSize() > 0) {
            slaveDataSource.setInitialSize(dbInfo.getInitialSize());
        }
        if(null != dbInfo.getMaxActive() && dbInfo.getMaxActive() > 0) {
            slaveDataSource.setMaxActive(dbInfo.getMaxActive());
        }
        if(null != dbInfo.getMinIdle() && dbInfo.getMinIdle() > 0) {
            slaveDataSource.setMinIdle(dbInfo.getMinIdle());
        }
        if(null != dbInfo.getMaxWait() && dbInfo.getMaxWait() > 0) {
            slaveDataSource.setMaxWait(dbInfo.getMaxWait());
        }
        return slaveDataSource;
    }

    private Map<String, Object> dataSourceProperty(BizDatasources config) {
        String url = "";
        String driver = config.getDbType();
        if(driver.contains("sqlserver") || driver.contains("jtds")) {
            url = String.format("jdbc:sqlserver://%s:%s;DatabaseName=%s", config.getDbIp(), config.getDbPort(), config.getDbName());
//    		url = String.format("jdbc:jtds:sqlserver://%s:%s;DatabaseName=%s", config.getDbIp(), config.getDbPort(), config.getDbName());
        } else if(driver.contains("mysql")) {
            url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8%s", config.getDbIp(), config.getDbPort(), config.getDbName(), config.getDbParams());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("driverClassName", config.getDbType());
        params.put("url", url);
        params.put("username", config.getDbUsername());
        params.put("password", config.getDbPassword());
        return params;
    }

    public List<Map<String, Object>> getDataSources() {
        return dataSources;
    }
}