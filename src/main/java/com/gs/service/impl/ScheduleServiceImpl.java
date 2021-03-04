package com.gs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;
import com.gs.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    TwinPointService twinPointService;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Autowired
    OPCItemService opcItemService;

    @Autowired
    TwinPointValueRecordService twinPointValueRecordService;

    @Autowired
    TwinPointAvgService twinPointAvgService;


    @Override
    @Scheduled(cron = "*/1 * * * * ?")
    @Async
    public void twinPointUpdate() {
        //需要更新的点位 一次处理20个
        List<TwinPointEntity> twinPointEntities = twinPointService.twinPointForUpdateValue();
        //更新
        twinPointEntities.forEach(
                i -> {
                    twinPointService.pointValueUpdate(i.getId());
                    //孪生点位数值记录
                    TwinPointValueRecordEntity twinPointValueRecordEntity = new TwinPointValueRecordEntity();
                    twinPointValueRecordEntity.setDataType(i.getDataType());
                    twinPointValueRecordEntity.setFactoryId(i.getFactoryId());
                    twinPointValueRecordEntity.setTwinPointId(i.getPointId());
                    twinPointValueRecordEntity.setTwinPointValue(i.getPointValue());
                    twinPointValueRecordEntity.setTwinPointCode(i.getPointCode());
                    twinPointValueRecordEntity.setProductionLineId(i.getProductionLineId());
                    twinPointValueRecordEntity.setTwinPointName(i.getPointName());
                    twinPointValueRecordEntity.setPointId(i.getId());
                    twinPointValueRecordEntity.setUnit(i.getUnit());
                    twinPointValueRecordService.save(twinPointValueRecordEntity);
                }
        );
    }

    @Override
    @Scheduled(cron = "*/1 * * * * ?")
    @Async
    public void twinPointAverage() {
        //需要更新均值的孪生点位
        List<TwinPointEntity> list = twinPointService.list(new QueryWrapper<TwinPointEntity>().in("data_type", 1, 2).lt("avg_update_time", LocalDateTime.now()));
        //统计均值
        for (TwinPointEntity pointEntity : list) {
            twinPointAvgService.twinPointAvg(pointEntity.getId());
            pointEntity.setAvgUpdateTime(pointEntity.getAvgUpdateTime().plus(pointEntity.getCalculateCycle(), ChronoUnit.SECONDS));
            twinPointService.updateById(pointEntity);
        }
    }

    @Override
    @Scheduled(cron = "*/1 * * * * ?")
    @Async
    public void itemAverage() {
        //需要统计均值的item
        List<OPCItemEntity> entities = opcItemService.list(new QueryWrapper<OPCItemEntity>().lt("avg_update_time", LocalDateTime.now()).in("item_type", 2, 3, 4));
        entities.forEach(
                i -> {
                    opcItemValueRecordService.itemAverage(i.getId());
                }
        );
    }

    @Override
    @Scheduled(cron = "*/1 * * * * ?")
    @Async
    public void dataSend() {

    }
}
