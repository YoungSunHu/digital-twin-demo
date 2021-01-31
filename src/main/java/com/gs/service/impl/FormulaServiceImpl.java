package com.gs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDetailDTO;
import com.gs.dao.entity.FormulaEntity;
import com.gs.dao.mapper.FormulaMapper;
import com.gs.service.FormulaService;
import org.springframework.stereotype.Service;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/28 9:59
 * @modified By：
 */
@Service
public class FormulaServiceImpl extends ServiceImpl<FormulaMapper, FormulaEntity> implements FormulaService {
    @Override
    public HFSFCompoundFertilizerFormulaDTO HFSFCompoundFertilizerFormulaHandler(HFSFCompoundFertilizerFormulaDTO dto) {
        //清零
        dto.setTotalRawMaterialRatio(0f);
        dto.setTotalWaterContent(0f);
        dto.setTotalMaterialWeight(0f);
        dto.setTotalProductNutrientContent(0f);
        dto.setTotalProductNutrientContent(0f);
        dto.setTotalUnitConsumption(0f);
        dto.setNutrientConversionFactor(0f);
        dto.setN(0f);
        dto.setP(0f);
        dto.setK(0f);
        dto.setCL_minus(0f);
        //物料重量合计
        dto.getDetails().forEach(
                i -> {
                    dto.setTotalMaterialWeight(dto.getTotalMaterialWeight() + i.getMaterialWeight());
                }
        );
        //detail的原料配比,原料配比总和,水分总和
        dto.getDetails().forEach(
                i -> {
                    i.setRawMaterialRatio((i.getMaterialWeight() / dto.getTotalMaterialWeight()) * 100);
                    //原料配比总和
                    dto.setTotalRawMaterialRatio(dto.getTotalRawMaterialRatio() + i.getRawMaterialRatio());
                    //水分总和 = (物料重量*水分)之和/物料重量总重
                    dto.setTotalWaterContent((dto.getTotalWaterContent() + (i.getWaterContent() * i.getMaterialWeight())));
                }
        );
        dto.setTotalWaterContent(dto.getTotalWaterContent() / dto.getTotalMaterialWeight());
        //养分换算系数=100/(100-(水分合计-成品水分))
        dto.setNutrientConversionFactor(100 / (100 - (dto.getTotalWaterContent() - dto.getProductMoisture())));
        //detail的成品养分含量=养分含量*原料配比/100*养分换算系数 单耗 = 原料配比 * 原料消耗系数 /100
        Float preRawMaterialRatio = 0f;
        for (HFSFCompoundFertilizerFormulaDetailDTO i : dto.getDetails()) {
            if (i.getDataType() == 3) {
                //dataType=3原料配比要使用上一个detail的值
                i.setProductNutrientContent(i.getNutrientContent() * preRawMaterialRatio / 100 * dto.getNutrientConversionFactor());
            } else {
                i.setProductNutrientContent(i.getNutrientContent() * i.getRawMaterialRatio() / 100 * dto.getNutrientConversionFactor());
            }
            //单耗计算=原料配比*原料消耗系数 或为固定值
            if (i.getDataType() == 0) {
                i.setUnitConsumption(i.getRawMaterialRatio() * dto.getRawMaterialConsumptionCoefficient() / 100);
            }
            //原料成本=单耗*原料价格
            i.setRawMaterialCost(i.getUnitConsumption() * i.getRawMaterialsPrice());
            dto.setTotalUnitConsumption(dto.getTotalUnitConsumption() + i.getUnitConsumption());
            dto.setTotalRawMaterialCost(i.getRawMaterialCost());
            //成品养分含量合计
            dto.setTotalProductNutrientContent(dto.getTotalProductNutrientContent() + i.getProductNutrientContent());
            preRawMaterialRatio = i.getRawMaterialRatio();
        }
        //生产成本= 原料成本合计 + 154
        dto.setManufacturingCost(dto.getTotalRawMaterialCost() + 154);
        //生产毛利=控制成本-生产成本
        dto.setGrossProfitOfProduction(dto.getCostControl() - dto.getManufacturingCost());

        //配合式
        for (HFSFCompoundFertilizerFormulaDetailDTO detail : dto.getDetails()) {
            if (detail.getRawMaterialName().equals("碳铵") || detail.getRawMaterialName().equals("液氨") || detail.getRawMaterialName().equals("尿素") || detail.getRawMaterialName().equals("氯化铵") || detail.getRawMaterialName().equals("返料(N)") || (detail.getRawMaterialName().equals("磷一铵") && detail.getDataType() == 3)) {
                dto.setN(dto.getN() + detail.getProductNutrientContent());
            } else if (detail.getRawMaterialName().equals("返料(P)") || detail.getRawMaterialName().equals("普钙") || (detail.getRawMaterialName().equals("磷一铵") && detail.getDataType() == 0)) {
                dto.setP(dto.getP() + detail.getProductNutrientContent());
            } else if (detail.getRawMaterialName().equals("返料(K)") || detail.getRawMaterialName().equals("氯化钾(红)") || detail.getRawMaterialName().equals("氯化钾(白)") || detail.getRawMaterialName().equals("控失剂(白)")) {
                dto.setK(dto.getK() + detail.getProductNutrientContent());
            }

            if (detail.getRawMaterialName().equals("氯化钾(红)")) {
                dto.setCL_minus(dto.getCL_minus() + (detail.getRawMaterialRatio() * 47) / 100);
            } else if (detail.getRawMaterialName().equals("氯化钾(白)")) {
                dto.setCL_minus(dto.getCL_minus() + (detail.getRawMaterialRatio() * 47) / 100);
            } else if (detail.getRawMaterialName().equals("氯化铵")) {
                dto.setCL_minus(dto.getCL_minus() + (detail.getRawMaterialRatio() * 67) / 100);
            }
        }
        dto.setCL_minus(dto.getCL_minus() * dto.getNutrientConversionFactor());
        return dto;
    }
}
