package com.gs.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gs.VO.HFSFCompoundFertilizerQualityStaticVO;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/1 9:59
 * @modified By：
 */
public interface HFSFCompoundFertilizerQualityMapper extends BaseMapper<HFSFCompoundFertilizerQualityEntity> {
    HFSFCompoundFertilizerQualityStaticVO hFSFCompoundFertilizerQualityStatic(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
