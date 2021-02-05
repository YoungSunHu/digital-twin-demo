package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
import com.gs.dao.entity.ChemicalExaminationRecordEntity;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;

public interface ChemicalExaminationRecordService extends IService<ChemicalExaminationRecordEntity> {

    /**
     * 合肥四方6号线复合肥化验登记
     */
    void HFSFLine6(HFSFCompoundFertilizerQualityEntity entity);

    /**
     * 合肥四方6号线配方化验登记
     */
    void HFSFLine6(HFSFCompoundFertilizerFormulaDTO dto);

}
