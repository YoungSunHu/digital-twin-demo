package com.gs.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.dao.entity.TwinPointEntity;

import java.util.List;

public interface TwinPointMapper extends BaseMapper<TwinPointEntity> {
    List<TwinPointEntity> twinPointForUpdateValue();
}
