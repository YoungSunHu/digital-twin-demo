package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/27 14:45
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class HFSFCompoundFertilizerFormulaDTO implements Serializable {
    /**
     * 受方单位
     */
    String formulaUnit;

    /**
     * 产品名称
     */
    String productionName;

    /**
     * 配方编号
     */
    String formulaCode;

    /**
     * 产品型号
     */
    String productionModel;

    /**
     * 产品计划批量
     */
    String productionPlanBatch;

    /**
     * 配方日期
     */
    LocalDateTime formualDate;

    /**
     * 配方详情
     */
    List<HFSFCompoundFertilizerFormulaDetailDTO> details;

    /**
     * 养分换算系数
     */
    Float nutrientConversionFactor = 0f;

    /**
     * 原料消耗系数
     */
    Float rawMaterialConsumptionCoefficient = 0f;

    /**
     * 控制成本
     */
    Float costControl = 0f;

    /**
     * 生产毛利
     */
    Float grossProfitOfProduction = 0f;

    /**
     * 成品水分
     */
    Float productMoisture = 0f;

    /**
     * 配合式: N
     */
    Float N = 0f;

    /**
     * 配合式: P
     */
    Float P = 0f;

    /**
     * 配合式: CL-
     */
    Float CL_minus = 0f;

    /**
     * 物料重量合计
     */
    Float totalMaterialWeight = 0f;

    /**
     * 水分合计
     */
    Float totalWaterContent = 0f;

    /**
     * 原料配比合计
     */
    Float totalRawMaterialRatio = 0f;

    /**
     * 成品养分含量合计
     */
    Float totalProductNutrientContent = 0f;

    /**
     * 单耗合计
     */
    Float totalUnitConsumption = 0f;

    /**
     * 原料成本
     */
    Float totalRawMaterialCost = 0f;

    /**
     * 生产成本
     */
    Float manufacturingCost = 0f;


}
