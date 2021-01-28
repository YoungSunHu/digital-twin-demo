package com.gs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
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
                    dto.setTotalWaterContent((dto.getTotalWaterContent() + (i.getWaterContent() * i.getMaterialWeight())) / dto.getTotalMaterialWeight());
                }
        );
        //养分换算系数=100/(100-(物料总重-成品水分))
        dto.setNutrientConversionFactor(100 / (100 - (dto.getTotalMaterialWeight() - dto.getProductMoisture())));
        //detail的成品养分含量=水分*原料配比/100*养分换算系数 单耗 = 原料配比 * 原料消耗系数 /100
        dto.getDetails().forEach(
                i -> {
                    i.setProductNutrientContent(i.getWaterContent() * i.getRawMaterialRatio() / 100 * dto.getNutrientConversionFactor());
                    //单耗计算=原料配比*原料消耗系数 或为固定值
                    if (i.getDataType() == 0) {
                        i.setUnitConsumption(i.getRawMaterialRatio() * dto.getRawMaterialConsumptionCoefficient() / 100);
                    }
                    //原料成本=单耗*原料价格
                    i.setRawMaterialCost(i.getUnitConsumption() * i.getRawMaterialsPrice());
                    dto.setTotalUnitConsumption(dto.getTotalUnitConsumption() + i.getUnitConsumption());
                    dto.setTotalRawMaterialCost(i.getRawMaterialCost());
                }
        );
        //生产成本= 原料成本合计 + 154
        dto.setManufacturingCost(dto.getTotalRawMaterialCost() + 154);
        //生产毛利=控制成本-生产成本
        dto.setGrossProfitOfProduction(dto.getCostControl() - dto.getManufacturingCost());
        return dto;
    }
}
