package com.gs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;
import com.gs.dao.mapper.TwinPointMapper;
import com.gs.dao.mapper.TwinPointValueRecordMapper;
import com.gs.service.TwinPointValueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;


/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 14:20
 * @modified By：
 */
@Service
public class TwinPointValueRecordServiceImpl extends ServiceImpl<TwinPointValueRecordMapper, TwinPointValueRecordEntity> implements TwinPointValueRecordService {

    @Autowired
    TwinPointValueRecordMapper twinPointValueRecordMapper;

    @Autowired
    TwinPointMapper twinPointMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    @Async
    public void updateDCSTwinPoint(OPCItemValueRecordEntity entity) {
        List<TwinPointEntity> twinPointEntities = twinPointMapper.selectList(new QueryWrapper<TwinPointEntity>().eq("factory_id", entity.getFactoryId()).eq("item_id", entity.getItemId()));
        for (TwinPointEntity twinPointEntity : twinPointEntities) {
            //保存孪生点位记录
            TwinPointValueRecordEntity twinPointValueRecordEntity = new TwinPointValueRecordEntity();
            twinPointValueRecordEntity.setUnit(twinPointEntity.getUnit());
            twinPointValueRecordEntity.setDataType(twinPointEntity.getDataType());
            twinPointValueRecordEntity.setTwinPointId(twinPointEntity.getPointId());
            twinPointValueRecordEntity.setTwinPointName(twinPointEntity.getPointName());
            twinPointValueRecordEntity.setTwinPointValue(entity.getItemValue());
            twinPointValueRecordEntity.setFactoryId(twinPointEntity.getFactoryId());
            twinPointValueRecordEntity.setTwinPointCode(twinPointEntity.getPointCode());
            twinPointValueRecordEntity.setPointId(twinPointEntity.getId());
            twinPointValueRecordEntity.setProductionLineId(twinPointEntity.getProductionLineId());
            twinPointValueRecordEntity.setItemTimestamp(Instant.ofEpochMilli(entity.getItemTimestamp().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            twinPointValueRecordMapper.insert(twinPointValueRecordEntity);
            //更新孪生点位值
            twinPointEntity.setPointValue(entity.getItemValue());
            twinPointMapper.updateById(twinPointEntity);
        }
    }
}
