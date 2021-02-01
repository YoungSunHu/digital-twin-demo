package com.gs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.VO.HFSFCompoundFertilizerQualityStaticVO;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;
import com.gs.dao.mapper.HFSFCompoundFertilizerQualityMapper;
import com.gs.service.HFSFCompoundFertilizerQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/1 10:02
 * @modified By：
 */
@Service
public class HFSFCompoundFertilizerQualityServiceImpl extends ServiceImpl<HFSFCompoundFertilizerQualityMapper, HFSFCompoundFertilizerQualityEntity> implements HFSFCompoundFertilizerQualityService {

    @Autowired
    HFSFCompoundFertilizerQualityMapper hfsfCompoundFertilizerQualityMapper;

    @Override
    public HFSFCompoundFertilizerQualityStaticVO hFSFCompoundFertilizerQualityStatic(LocalDate startDate, LocalDate endDate) {
        HFSFCompoundFertilizerQualityStaticVO hfsfCompoundFertilizerQualityStaticVO = hfsfCompoundFertilizerQualityMapper.hFSFCompoundFertilizerQualityStatic(startDate, endDate);
        return hfsfCompoundFertilizerQualityStaticVO;
    }
}
