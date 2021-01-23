package com.gs.service.impl;

import com.gs.dao.entity.TwinPointEntity;
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
}
