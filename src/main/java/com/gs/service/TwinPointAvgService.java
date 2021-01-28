package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.dao.entity.TwinPointAvgEntity;

public interface TwinPointAvgService extends IService<TwinPointAvgEntity> {
    void twinPointAvg(Long twinPointId);
}
