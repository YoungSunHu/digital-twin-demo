package com.gs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDetailDTO;
import com.gs.dao.entity.ChemicalExaminationEntity;
import com.gs.dao.entity.ChemicalExaminationRecordEntity;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;
import com.gs.dao.mapper.ChemicalExaminationMapper;
import com.gs.dao.mapper.ChemicalExaminationRecordMapper;
import com.gs.service.ChemicalExaminationRecordService;
import com.gs.service.TwinPointService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/3 16:04
 * @modified By：
 */
@Service
public class ChemicalExaminationRecordServiceImpl extends ServiceImpl<ChemicalExaminationRecordMapper, ChemicalExaminationRecordEntity> implements ChemicalExaminationRecordService {

    @Autowired
    ChemicalExaminationRecordMapper chemicalExaminationRecordMapper;

    @Autowired
    ChemicalExaminationMapper chemicalExaminationMapper;

    @Autowired
    TwinPointService twinPointService;

    @Override
    public void HFSFLine6(HFSFCompoundFertilizerQualityEntity entity) {
        //养分N
        QueryWrapper<ChemicalExaminationEntity> nWrapper = new QueryWrapper<ChemicalExaminationEntity>().eq("exam_code", "fhf6_cfq_1_1").eq("factory_id", "AHHSF001").eq("production_line_code", "fhf6");
        ChemicalExaminationEntity nEntity = chemicalExaminationMapper.selectOne(nWrapper);
        ChemicalExaminationRecordEntity nRecordEntity = new ChemicalExaminationRecordEntity();
        BeanUtils.copyProperties(nEntity, nRecordEntity);
        nRecordEntity.setId(null);
        nRecordEntity.setExamItemValue(String.valueOf(entity.getN()));
        nRecordEntity.setExamTime(entity.getExamTime());
        nEntity.setExamItemValue(String.valueOf(entity.getN()));
        nEntity.setExamTime(entity.getExamTime());
        chemicalExaminationMapper.updateById(nEntity);
        chemicalExaminationRecordMapper.insert(nRecordEntity);
        twinPointService.chemicalExamUpdate(nEntity.getId(), nEntity.getExamItemValue(), nEntity.getExamTime());

        //养分P
        QueryWrapper<ChemicalExaminationEntity> pWrapper = new QueryWrapper<ChemicalExaminationEntity>().eq("exam_code", "fhf6_cfq_2_1").eq("factory_id", "AHHSF001").eq("production_line_code", "fhf6");
        ChemicalExaminationEntity pEntity = chemicalExaminationMapper.selectOne(pWrapper);
        ChemicalExaminationRecordEntity pRecordEntity = new ChemicalExaminationRecordEntity();
        BeanUtils.copyProperties(pEntity, pRecordEntity);
        pRecordEntity.setId(null);
        pRecordEntity.setExamItemValue(String.valueOf(entity.getP()));
        pRecordEntity.setExamTime(entity.getExamTime());
        pEntity.setExamItemValue(String.valueOf(entity.getP()));
        pEntity.setExamTime(entity.getExamTime());
        chemicalExaminationMapper.updateById(pEntity);
        chemicalExaminationRecordMapper.insert(pRecordEntity);
        twinPointService.chemicalExamUpdate(pEntity.getId(), pEntity.getExamItemValue(), pEntity.getExamTime());

        //养分P
        QueryWrapper<ChemicalExaminationEntity> kWrapper = new QueryWrapper<ChemicalExaminationEntity>().eq("exam_code", "fhf6_cfq_3_1").eq("factory_id", "AHHSF001").eq("production_line_code", "fhf6");
        ChemicalExaminationEntity kEntity = chemicalExaminationMapper.selectOne(pWrapper);
        ChemicalExaminationRecordEntity kRecordEntity = new ChemicalExaminationRecordEntity();
        BeanUtils.copyProperties(kEntity, kRecordEntity);
        kRecordEntity.setId(null);
        kRecordEntity.setExamItemValue(String.valueOf(entity.getK()));
        kRecordEntity.setExamTime(entity.getExamTime());
        kEntity.setExamItemValue(String.valueOf(entity.getK()));
        kEntity.setExamTime(entity.getExamTime());
        chemicalExaminationMapper.updateById(kEntity);
        chemicalExaminationRecordMapper.insert(kRecordEntity);
        twinPointService.chemicalExamUpdate(kEntity.getId(), kEntity.getExamItemValue(), kEntity.getExamTime());

        //水分
        QueryWrapper<ChemicalExaminationEntity> waterWrapper = new QueryWrapper<ChemicalExaminationEntity>().eq("exam_code", "fhf6_cfq_4_1").eq("factory_id", "AHHSF001").eq("production_line_code", "fhf6");
        ChemicalExaminationEntity waterEntity = chemicalExaminationMapper.selectOne(waterWrapper);
        ChemicalExaminationRecordEntity waterRecordEntity = new ChemicalExaminationRecordEntity();
        BeanUtils.copyProperties(waterEntity, waterRecordEntity);
        waterRecordEntity.setId(null);
        waterRecordEntity.setExamItemValue(String.valueOf(entity.getWater()));
        waterRecordEntity.setExamTime(entity.getExamTime());
        waterEntity.setExamItemValue(String.valueOf(entity.getWater()));
        waterEntity.setExamTime(entity.getExamTime());
        chemicalExaminationMapper.updateById(waterEntity);
        chemicalExaminationRecordMapper.insert(waterRecordEntity);
        twinPointService.chemicalExamUpdate(waterEntity.getId(), waterEntity.getExamItemValue(), waterEntity.getExamTime());
    }

    @Override
    public void HFSFLine6(HFSFCompoundFertilizerFormulaDTO dto) {
        for (HFSFCompoundFertilizerFormulaDetailDTO detail : dto.getDetails()) {
            if (StringUtils.isNoneBlank(detail.getExamCode())) {
                QueryWrapper<ChemicalExaminationEntity> queryWrapper = new QueryWrapper<ChemicalExaminationEntity>().like("exam_code", detail.getExamCode()).eq("factory_id", "AHHSF001").eq("production_line_code", "fhf6");
                List<ChemicalExaminationEntity> examinationEntities = chemicalExaminationMapper.selectList(queryWrapper);
                for (ChemicalExaminationEntity examinationEntity : examinationEntities) {
                    if (examinationEntity.getExamCode().equals(detail.getExamCode() + "_1")) {
                        //1养分含量
                        ChemicalExaminationRecordEntity chemicalExaminationRecordEntity = new ChemicalExaminationRecordEntity();
                        BeanUtils.copyProperties(examinationEntity, chemicalExaminationRecordEntity);
                        chemicalExaminationRecordEntity.setId(null);
                        chemicalExaminationRecordEntity.setExamItemValue(String.valueOf(detail.getNutrientContent()));
                        chemicalExaminationRecordEntity.setExamTime(dto.getFormualDate());
                        chemicalExaminationRecordEntity.setChemicalExaminationId(examinationEntity.getId());
                        examinationEntity.setExamItemValue(String.valueOf(detail.getNutrientContent()));
                        examinationEntity.setExamTime(dto.getFormualDate());
                        chemicalExaminationMapper.updateById(examinationEntity);
                        chemicalExaminationRecordEntity.setCreateTime(LocalDateTime.now());
                        chemicalExaminationRecordMapper.insert(chemicalExaminationRecordEntity);
                        twinPointService.chemicalExamUpdate(examinationEntity.getId(), examinationEntity.getExamItemValue(), examinationEntity.getExamTime());
                    } else if (examinationEntity.getExamCode().equals(detail.getExamCode() + "_2")) {
                        //2水分含量
                        ChemicalExaminationRecordEntity chemicalExaminationRecordEntity = new ChemicalExaminationRecordEntity();
                        BeanUtils.copyProperties(examinationEntity, chemicalExaminationRecordEntity);
                        chemicalExaminationRecordEntity.setId(null);
                        chemicalExaminationRecordEntity.setExamItemValue(String.valueOf(detail.getWaterContent()));
                        chemicalExaminationRecordEntity.setExamTime(dto.getFormualDate());
                        chemicalExaminationRecordEntity.setChemicalExaminationId(examinationEntity.getId());
                        examinationEntity.setExamItemValue(String.valueOf(detail.getWaterContent()));
                        examinationEntity.setExamTime(dto.getFormualDate());
                        chemicalExaminationMapper.updateById(examinationEntity);
                        chemicalExaminationRecordEntity.setCreateTime(LocalDateTime.now());
                        chemicalExaminationRecordMapper.insert(chemicalExaminationRecordEntity);
                        twinPointService.chemicalExamUpdate(examinationEntity.getId(), examinationEntity.getExamItemValue(), examinationEntity.getExamTime());
                    } else if (examinationEntity.getExamCode().equals(detail.getExamCode() + "_3")) {
                        //3物料重量
                        ChemicalExaminationRecordEntity chemicalExaminationRecordEntity = new ChemicalExaminationRecordEntity();
                        BeanUtils.copyProperties(examinationEntity, chemicalExaminationRecordEntity);
                        chemicalExaminationRecordEntity.setId(null);
                        chemicalExaminationRecordEntity.setExamItemValue(String.valueOf(detail.getMaterialWeight()));
                        chemicalExaminationRecordEntity.setExamTime(dto.getFormualDate());
                        chemicalExaminationRecordEntity.setChemicalExaminationId(examinationEntity.getId());
                        examinationEntity.setExamItemValue(String.valueOf(detail.getMaterialWeight()));
                        examinationEntity.setExamTime(dto.getFormualDate());
                        chemicalExaminationMapper.updateById(examinationEntity);
                        chemicalExaminationRecordEntity.setCreateTime(LocalDateTime.now());
                        chemicalExaminationRecordMapper.insert(chemicalExaminationRecordEntity);
                        twinPointService.chemicalExamUpdate(examinationEntity.getId(), examinationEntity.getExamItemValue(), examinationEntity.getExamTime());
                    }
                }
            }
        }
    }
}
