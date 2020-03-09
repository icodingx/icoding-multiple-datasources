
## 使用说明

> 多数据源链接组件

> 能力：基于多种配置建立多个数据源连接池

### 引用模块操作方法

**1.基于Nacos配置中心的配置项** `spring.datasource.druid.slave.auth-num` 限制从数据源个数，
项目中以 `@Value("${spring.datasource.druid.slave.auth-num:2}")` 取值，自定义限制逻辑

**2.定义 `DruidConfig` 类，构建连接池**
```java
@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return druidProperties.dataSource(dataSource);
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DynamicDataSource dataSource(DataSource masterDataSource, @Value("${spring.datasource.druid.slave.auth-num:3}") Integer authNum) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
        return new DynamicDataSource(masterDataSource, targetDataSources, authNum);
    }
}
```

**3.定义 `DynamicDataSourceListener` 类，注入 slave 数据源**
```java
@Service
public class DynamicDataSourceListener implements ApplicationListener<ContextRefreshedEvent> {
    protected final Logger logger = LoggerFactory.getLogger(DynamicDataSourceListener.class);
	@Value("${spring.datasource.druid.slave.enabled}")
	private Boolean slaveEnabled;
	@Value("${spring.datasource.druid.slave.auth-num:3}")
	private Integer authNum;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info(" listener start...");
		if(slaveEnabled) {
			try {
				IBizDatasourcesService dbInfoMapper = SpringContextProcessor.getBean(IBizDatasourcesService.class);
				List<BizDatasources> list = dbInfoMapper.selectBizDatasourcesList();
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						BizDatasources config = list.get(i);
						if (i < authNum) {
							SpringContextProcessor.getBean(DynamicDataSource.class).reloadDataSources(config);
						} else {
							logger.info("数据源个数超限，{} 未构建！", config.getDsName());
						}
					}
				}
			} catch (Exception e) {
				logger.error(" datasource reload error...");
				e.printStackTrace();
			}
		}
	}
}
```

**4.获取数据源集合**
```
// Map<String, Object> 记录数据源的信息 key: label / value(dsName用于切换数据源) / did(dsId用于关联子项目，-1为主数据源) / ds(从数据源 DruidDataSource)
List<Map<String, Object>> dss = SpringContextProcessor.getBean(DynamicDataSource.class).getDataSources();
```

**5.销毁连接池**
```
SpringContextProcessor.getBean(DynamicDataSource.class).removeSlaveDataSource(dsName);
```

**6.数据源信息的持久化**

可用 `IBizDatasourcesService` 基于 MyBatis 记录在如下表中
```sql
CREATE TABLE `biz_datasources` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `db_type` varchar(50) DEFAULT NULL COMMENT '数据库类型',
  `ds_name` varchar(30) DEFAULT NULL,
  `db_port` int(11) DEFAULT NULL COMMENT 'PORT',
  `db_ip` varchar(50) DEFAULT NULL COMMENT 'IP',
  `db_name` varchar(20) DEFAULT NULL COMMENT '数据库名称',
  `db_params` varchar(200) DEFAULT NULL COMMENT '连接参数',
  `db_username` varchar(20) DEFAULT NULL COMMENT '用户名',
  `db_password` varchar(50) DEFAULT NULL COMMENT '密码',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态（0正常 1禁用）',
  `initial_size` int(11) DEFAULT NULL,
  `max_active` int(11) DEFAULT NULL,
  `min_idle` int(11) DEFAULT NULL,
  `max_wait` int(11) DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='链接数据源';
```
