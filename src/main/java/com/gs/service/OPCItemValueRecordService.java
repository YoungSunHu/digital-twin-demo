package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.DTO.ItemStatusDTO;
import com.gs.dao.entity.OPCItemValueRecordEntity;

import java.util.List;

public interface OPCItemValueRecordService extends IService<OPCItemValueRecordEntity> {

    void itemCache(OPCItemValueRecordEntity opcItemValueRecordEntity);

    List<OPCItemValueRecordEntity> itemStatus(ItemStatusDTO itemStatusDTO);

    /**
     * 原始点位均值统计
     *
     * @param itemId
     */
    void itemAverage(Long itemId);
}
