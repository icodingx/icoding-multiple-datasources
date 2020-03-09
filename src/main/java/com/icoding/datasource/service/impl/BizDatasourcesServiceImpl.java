package com.icoding.datasource.service.impl;

import com.icoding.datasource.domain.BizDatasources;
import com.icoding.datasource.mapper.BizDatasourcesMapper;
import com.icoding.datasource.service.IBizDatasourcesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 链接数据源Service业务层处理
 * 
 * @author icoding
 * @date 2019-11-15
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BizDatasourcesServiceImpl implements IBizDatasourcesService {

    private final BizDatasourcesMapper bizDatasourcesMapper;

    /**
     * 查询链接数据源
     * 
     * @param id 链接数据源ID
     * @return 链接数据源
     */
    @Override
    public BizDatasources selectBizDatasourcesById(Long id) {
        return bizDatasourcesMapper.selectBizDatasourcesById(id);
    }

	@Override
	public BizDatasources selectBizDatasourcesByName(String dsName) {
		return bizDatasourcesMapper.selectBizDatasourcesByName(dsName);
	}

	/**
     * 查询链接数据源列表
     * 
     * @return 链接数据源
     */
    @Override
    public List<BizDatasources> selectBizDatasourcesList() {
        return bizDatasourcesMapper.selectBizDatasourcesList(null);
    }

    @Override
    public List<BizDatasources> selectBizDatasourcesList(String ids) {
        return bizDatasourcesMapper.selectBizDatasourcesList(ids.split(","));
    }

    /**
     * 新增链接数据源
     * 
     * @param bizDatasources 链接数据源
     * @return 结果
     */
    @Override
    public int insertBizDatasources(BizDatasources bizDatasources) {
        bizDatasources.setCreateTime(new Date());
        return bizDatasourcesMapper.insertBizDatasources(bizDatasources);
    }

    /**
     * 修改链接数据源
     * 
     * @param bizDatasources 链接数据源
     * @return 结果
     */
    @Override
    public int updateBizDatasources(BizDatasources bizDatasources) {
        bizDatasources.setUpdateTime(new Date());
        return bizDatasourcesMapper.updateBizDatasources(bizDatasources);
    }

    /**
     * 删除链接数据源对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBizDatasourcesByIds(String ids) {
        return bizDatasourcesMapper.deleteBizDatasourcesByIds(ids.split(","));
    }

    /**
     * 删除链接数据源信息
     * 
     * @param id 链接数据源ID
     * @return 结果
     */
    @Override
    public int deleteBizDatasourcesById(Long id) {
        return bizDatasourcesMapper.deleteBizDatasourcesById(id);
    }

}
