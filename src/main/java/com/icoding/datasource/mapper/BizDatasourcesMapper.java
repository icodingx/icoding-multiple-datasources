package com.icoding.datasource.mapper;

import java.util.List;

import com.icoding.datasource.domain.BizDatasources;

/**
 * 链接数据源Mapper接口
 * 
 * @author icoding
 * @date 2019-11-15
 */
public interface BizDatasourcesMapper {
    /**
     * 查询链接数据源
     * 
     * @param id 链接数据源ID
     * @return 链接数据源
     */
    public BizDatasources selectBizDatasourcesById(Long id);

    /**
     * 查询链接数据源列表
     * @param ids
     * 
     * @return 链接数据源集合
     */
    public List<BizDatasources> selectBizDatasourcesList(String[] ids);

    /**
     * 新增链接数据源
     * 
     * @param bizDatasources 链接数据源
     * @return 结果
     */
    public int insertBizDatasources(BizDatasources bizDatasources);

    /**
     * 修改链接数据源
     * 
     * @param bizDatasources 链接数据源
     * @return 结果
     */
    public int updateBizDatasources(BizDatasources bizDatasources);

    /**
     * 删除链接数据源
     * 
     * @param id 链接数据源ID
     * @return 结果
     */
    public int deleteBizDatasourcesById(Long id);

    /**
     * 批量删除链接数据源
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBizDatasourcesByIds(String[] ids);

	public BizDatasources selectBizDatasourcesByName(String dsName);
}
