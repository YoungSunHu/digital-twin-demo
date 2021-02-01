package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.VO.HFSFCompoundFertilizerQualityStaticVO;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;

import java.time.LocalDate;

public interface HFSFCompoundFertilizerQualityService extends IService<HFSFCompoundFertilizerQualityEntity> {
    HFSFCompoundFertilizerQualityStaticVO hFSFCompoundFertilizerQualityStatic(LocalDate startDate, LocalDate endDate);
}
