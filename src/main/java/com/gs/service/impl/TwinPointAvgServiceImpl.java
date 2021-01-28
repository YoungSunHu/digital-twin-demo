package com.gs.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.dao.entity.TwinPointAvgEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;
import com.gs.dao.mapper.TwinPointAvgMapper;
import com.gs.dao.mapper.TwinPointMapper;
import com.gs.dao.mapper.TwinPointValueRecordMapper;
import com.gs.service.TwinPointAvgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 14:20
 * @modified By：
 */
@Service
public class TwinPointAvgServiceImpl extends ServiceImpl<TwinPointAvgMapper, TwinPointAvgEntity> implements TwinPointAvgService {

    @Autowired
    TwinPointAvgMapper twinPointAvgMapper;

    @Autowired
    TwinPointMapper twinPointMapper;

    @Autowired
    TwinPointValueRecordMapper twinPointValueRecordMapper;

    Snowflake snowflake = new Snowflake(5, 5);

    @Async
    @Override
    public void twinPointAvg(Long twinPointId) {
        TwinPointEntity pointEntity = twinPointMapper.selectOne(new QueryWrapper<TwinPointEntity>().eq("id", twinPointId).orderBy(true, false, "calculate_sequence"));
        if (pointEntity == null) {
            throw new RuntimeException("id:" + twinPointId + "孪生点位不存在");
        }
        Float aFloat = twinPointAvgMapper.twinPointAvg(twinPointId);
        TwinPointAvgEntity twinPointAvgEntity = new TwinPointAvgEntity();
        twinPointAvgEntity.setId(snowflake.nextId());
        twinPointAvgEntity.setFactoryId(pointEntity.getFactoryId());
        twinPointAvgEntity.setDataType(pointEntity.getDataType());
        twinPointAvgEntity.setUnit(pointEntity.getUnit());
        twinPointAvgEntity.setTwinPointAvgValue(String.valueOf(aFloat));
        twinPointAvgEntity.setProductionLineId(pointEntity.getProductionLineId());
        twinPointAvgEntity.setPointId(pointEntity.getId());
        twinPointAvgEntity.setTwinPointName(pointEntity.getPointName());
        twinPointAvgEntity.setTwinPointCode(pointEntity.getPointCode());
        twinPointAvgEntity.setTwinPointId(pointEntity.getPointId());
        twinPointAvgMapper.insert(twinPointAvgEntity);
    }
}
