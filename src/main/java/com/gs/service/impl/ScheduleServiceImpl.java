package com.gs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.service.OPCItemService;
import com.gs.service.OPCItemValueRecordService;
import com.gs.service.ScheduleService;
import com.gs.service.TwinPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    TwinPointService twinPointService;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Autowired
    OPCItemService opcItemService;

    @Override
    @Scheduled(cron = "*/10 * * * * ?")
    public void twinPointUpdate() {
        //需要更新的点位 一次处理20个
        List<TwinPointEntity> twinPointEntities = twinPointService.twinPointForUpdateValue();
        //更新
        twinPointEntities.forEach(
                i -> {
                    twinPointService.pointValueUpdate(i.getId());
                }
        );
    }

    @Override
    @Scheduled(cron = "0 0/30 * * * ?")
    public void itemAverage() {
        //需要统计均值的item
        List<OPCItemEntity> entities = opcItemService.list(new QueryWrapper<OPCItemEntity>().in("item_type", 2, 3, 4));
        entities.forEach(
                i -> {
                    opcItemValueRecordService.itemAverage(i.getId());
                }
        );
    }
}
