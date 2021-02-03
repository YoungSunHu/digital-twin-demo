package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;

public interface TwinPointValueRecordService extends IService<TwinPointValueRecordEntity> {
    /**
     * 更新DCS孪生点位数值
     */
    void updateDCSTwinPoint(OPCItemValueRecordEntity entity);
}
