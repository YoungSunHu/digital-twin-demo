package com.gs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
import com.gs.dao.entity.FormulaEntity;

public interface FormulaService extends IService<FormulaEntity> {
    HFSFCompoundFertilizerFormulaDTO HFSFCompoundFertilizerFormulaHandler(HFSFCompoundFertilizerFormulaDTO dto);
}
