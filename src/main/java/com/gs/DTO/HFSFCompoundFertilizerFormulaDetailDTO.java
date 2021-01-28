package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/27 16:00
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class HFSFCompoundFertilizerFormulaDetailDTO implements Serializable {
    /**
     * 数据类型0=计算类型 1=固定类型
     */
    Integer dataType = 0;

    /**
     * 原料名称
     */
    @NotBlank(message = "rawMaterialName不得为空")
    String rawMaterialName;
    /**
     * 水分
     */
    Float waterContent = 0f;
    /**
     * 物料重量
     */
    Float materialWeight = 0f;
    /**
     * 原料配比
     */
    Float rawMaterialRatio = 0f;
    /**
     * 成品养分含量
     */
    Float productNutrientContent = 0f;
    /**
     * 单耗
     */
    Float unitConsumption = 0f;
    /**
     * 原料价格
     */
    Float rawMaterialsPrice = 0f;
    /**
     * 原料成本
     */
    Float rawMaterialCost = 0f;
}
