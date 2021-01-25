package com.gs.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

public interface OPCItemValueRecordMapper extends BaseMapper<OPCItemValueRecordEntity> {
    Double itemAverage(@Param("factoryId") String factoryId, @Param("itemId") String itemId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
