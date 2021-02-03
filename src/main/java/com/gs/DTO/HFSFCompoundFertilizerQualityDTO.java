package com.gs.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/31 16:48
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class HFSFCompoundFertilizerQualityDTO {
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "date不得为空")
    private LocalDate date;
    /**
     * 品名
     */
    @NotNull(message = "productionName不得为空")
    private String productionName;

    /**
     * 班次 1:甲 2:乙 3:丙 4:丁
     */
    @NotNull(message = "shift不得为空")
    private Integer shift;

    /**
     * 0:白班 1:夜班
     */
    @NotNull(message = "shiftType不得为空")
    private Integer shiftType;

    /**
     * 养分 N
     */
    private Float n = 0f;

    /**
     * 养分 P
     */
    private Float p = 0f;

    /**
     * 养分 K
     */
    private Float k = 0f;

    /**
     * 总养分
     */
    private Float totalNutrition = 0f;

    /**
     * 水分
     */
    private Float water = 0f;

    /**
     * 甲一级品
     */
    @JsonProperty(value = "aFirstClass")
    private Float aFirstClass = 0f;
    /**
     * 甲自用包
     */
    @JsonProperty(value = "aOwnUsePackage")
    private Float aOwnUsePackage = 0f;
    /**
     * 甲超养分
     */
    @JsonProperty(value = "aOverNutrition")
    private Float aOverNutrition = 0f;
    /**
     * 甲超水分
     */
    @JsonProperty(value = "aOverWater")
    private Float aOverWater = 0f;
    /**
     * 甲不合格
     */
    @JsonProperty(value = "aUnqualified")
    private Float aUnqualified = 0f;


    /**
     * 乙一级品
     */
    @JsonProperty(value = "bFirstClass")
    private Float bFirstClass = 0f;
    /**
     * 乙自用包
     */
    @JsonProperty(value = "bOwnUsePackage")
    private Float bOwnUsePackage = 0f;
    /**
     * 乙超养分
     */
    @JsonProperty(value = "bOverNutrition")
    private Float bOverNutrition = 0f;
    /**
     * 乙超水分
     */
    @JsonProperty(value = "bOverWater")
    private Float bOverWater = 0f;
    /**
     * 乙不合格
     */
    @JsonProperty(value = "bUnqualified")
    private Float bUnqualified = 0f;

    /**
     * 丙一级品
     */
    @JsonProperty(value = "cFirstClass")
    private Float cFirstClass = 0f;
    /**
     * 丙自用包
     */
    @JsonProperty(value = "cOwnUsePackage")
    private Float cOwnUsePackage = 0f;
    /**
     * 丙超养分
     */
    @JsonProperty(value = "cOverNutrition")
    private Float cOverNutrition = 0f;
    /**
     * 丙超水分
     */
    @JsonProperty(value = "cOverWater")
    private Float cOverWater = 0f;
    /**
     * 丙不合格
     */
    @JsonProperty(value = "cUnqualified")
    private Float cUnqualified = 0f;


    /**
     * 丁一级品
     */
    @JsonProperty(value = "dFirstClass")
    private Float dFirstClass = 0f;
    /**
     * 丁自用包
     */
    @JsonProperty(value = "dOwnUsePackage")
    private Float dOwnUsePackage = 0f;
    /**
     * 丁超养分
     */
    @JsonProperty(value = "dOverNutrition")
    private Float dOverNutrition = 0f;
    /**
     * 丁超水分
     */
    @JsonProperty(value = "dOverWater")
    private Float dOverWater = 0f;
    /**
     * 丁不合格
     */
    @JsonProperty(value = "dUnqualified")
    private Float dUnqualified = 0f;

    /**
     * 班产
     */
    private Float shiftYeild = 0f;

    /**
     * 高浓度
     */
    private Float highConcentration = 0f;

    /**
     * 中浓度
     */
    private Float middleConcentration = 0f;

    /**
     * 低浓度
     */
    private Float lowConcentration = 0f;

    /**
     * 送检时间
     */
    @NotNull(message = "testTime不得为空")
    private LocalDateTime testTime;
}
