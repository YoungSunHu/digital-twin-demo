package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.dao.entity.TwinPointEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 孪生点位
 */
public interface TwinPointService extends IService<TwinPointEntity> {

    /**
     * 查询需要更新数值的孪生点位
     *
     * @return
     */
    List<TwinPointEntity> twinPointForUpdateValue();

    /**
     * 点位数值更新
     *
     * @param pointId
     */
    void pointValueUpdate(Long pointId);

    /**
     * 孪生点位缓存
     *
     * @param twinPointEntity
     */
    void twinPointCache(TwinPointEntity twinPointEntity);

    /**
     * 孪生点位化验数据更新
     *
     * @param chemicalItemId 化验项id
     * @param itemValue      化验值
     * @param examTime       化验时间
     */
    void chemicalExamUpdate(Long chemicalItemId, String itemValue, LocalDateTime examTime);
}
