package com.gs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.dao.entity.ChemicalExaminationRecordEntity;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;
import com.gs.dao.mapper.ChemicalExaminationRecordMapper;
import com.gs.service.ChemicalExaminationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/3 16:04
 * @modified By：
 */
@Service
public class ChemicalExaminationRecordServiceImpl extends ServiceImpl<ChemicalExaminationRecordMapper, ChemicalExaminationRecordEntity> implements ChemicalExaminationRecordService {

    @Autowired
    ChemicalExaminationRecordMapper chemicalExaminationRecordMapper;

    @Override
    public void HFSFLine6(HFSFCompoundFertilizerQualityEntity entity) {
        ChemicalExaminationRecordEntity chemicalExaminationRecordEntity = new ChemicalExaminationRecordEntity();
        chemicalExaminationRecordEntity.setFactoryId("AHHSF001");
        chemicalExaminationRecordEntity.setProductionLineId("fhf6");
        chemicalExaminationRecordEntity.setExamItem1Name("养分N");
        chemicalExaminationRecordEntity.setExamItem1Value(String.valueOf(entity.getN()));
        chemicalExaminationRecordEntity.setExamItem2Name("养分P");
        chemicalExaminationRecordEntity.setExamItem2Value(String.valueOf(entity.getP()));
        chemicalExaminationRecordEntity.setExamItem2Name("养分K");
        chemicalExaminationRecordEntity.setExamItem2Value(String.valueOf(entity.getK()));
        chemicalExaminationRecordEntity.setExamItem2Name("水分");
        chemicalExaminationRecordEntity.setExamItem2Value(String.valueOf(entity.getWater()));
        chemicalExaminationRecordEntity.setExamTime(entity.getExamTime());
        chemicalExaminationRecordMapper.insert(chemicalExaminationRecordEntity);
    }
}
