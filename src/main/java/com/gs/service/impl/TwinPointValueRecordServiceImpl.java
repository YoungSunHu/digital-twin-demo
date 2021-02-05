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

import java.math.BigDecimal;
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
            twinPointValueRecordEntity.setFactoryId(twinPointEntity.getFactoryId());
            twinPointValueRecordEntity.setTwinPointCode(twinPointEntity.getPointCode());
            twinPointValueRecordEntity.setPointId(twinPointEntity.getId());
            twinPointValueRecordEntity.setProductionLineId(twinPointEntity.getProductionLineId());
            twinPointValueRecordEntity.setItemTimestamp(Instant.ofEpochMilli(entity.getItemTimestamp().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            if (entity.getItemType() == 8) {
                twinPointValueRecordEntity.setTwinPointValue(entity.getItemValue());
                twinPointEntity.setPointValue(entity.getItemValue());
            } else {
                twinPointValueRecordEntity.setTwinPointValue(BigDecimal.valueOf(Double.valueOf(entity.getItemValue())).setScale(twinPointEntity.getDecimalPalces(), BigDecimal.ROUND_CEILING).toString());
                twinPointEntity.setPointValue(BigDecimal.valueOf(Double.valueOf(entity.getItemValue())).setScale(twinPointEntity.getDecimalPalces(), BigDecimal.ROUND_CEILING).toString());
            }
            twinPointValueRecordMapper.insert(twinPointValueRecordEntity);
            //更新孪生点位值
            twinPointMapper.updateById(twinPointEntity);
        }
    }
}
