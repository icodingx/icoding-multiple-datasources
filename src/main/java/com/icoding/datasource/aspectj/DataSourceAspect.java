package com.icoding.datasource.aspectj;

import java.lang.reflect.Method;

import com.icoding.datasource.enums.DataSourceType;
import com.icoding.datasource.spring.SpringContextProcessor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.icoding.datasource.annotation.DataSource;
import com.icoding.datasource.pool.DynamicDataSourceContextHolder;
import org.springframework.util.StringUtils;

/**
 * 多数据源处理
 * @author icoding
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(com.icoding.datasource.annotation.DataSource)"
            + "|| @within(com.icoding.datasource.annotation.DataSource)")
    public void dsPointCut() {
    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String dataSource = getDataSource(point);
        if (!StringUtils.isEmpty(dataSource) && !dataSource.equals(DataSourceType.MASTER.name()) ) {
                SpringContextProcessor.getBean("slaveDataSource_" + dataSource);
                DynamicDataSourceContextHolder.setDataSourceType(dataSource);
        }
        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public String getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends Object> targetClass = point.getTarget().getClass();

        String result = "";
        DataSource targetDataSource = targetClass.getAnnotation(DataSource.class);
        if (null == targetDataSource) {
            Method method = signature.getMethod();
            targetDataSource = method.getAnnotation(DataSource.class);
        }
        if(null != targetDataSource) {
            result = targetDataSource.value().name();
            if(!StringUtils.isEmpty(result) && result.equals(DataSourceType.SLAVE.name())) {
                result = point.getArgs()[0].toString();
            }
        }
        return result;
    }
}
