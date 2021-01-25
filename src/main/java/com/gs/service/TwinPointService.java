package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.dao.entity.TwinPointEntity;

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
}
