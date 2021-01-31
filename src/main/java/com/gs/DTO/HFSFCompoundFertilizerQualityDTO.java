package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

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
    private LocalDateTime date;
    /**
     * 品名
     */
    private String productionName;

    /**
     * 班次 1:甲 2:乙 3:丙 4:丁
     */
    private Integer shift;

    /**
     * 0:白班 1:夜班
     */
    private Integer shiftType;

    /**
     * 养分 N
     */
    private Float N = 0f;

    /**
     * 养分 P
     */
    private Float P = 0f;

    /**
     * 养分 K
     */
    private Float K = 0f;

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
    private Float AFirstClass = 0f;
    /**
     * 甲自用包
     */
    private Float AOwnUsePackage = 0f;
    /**
     * 甲超养分
     */
    private Float AOverNutrition = 0f;
    /**
     * 甲超水分
     */
    private Float AOverWater = 0f;
    /**
     * 甲不合格
     */
    private Float AUnqualified = 0f;


    /**
     * 乙一级品
     */
    private Float BFirstClass = 0f;
    /**
     * 乙自用包
     */
    private Float BOwnUsePackage = 0f;
    /**
     * 乙超养分
     */
    private Float BOverNutrition = 0f;
    /**
     * 乙超水分
     */
    private Float BOverWater = 0f;
    /**
     * 乙不合格
     */
    private Float BUnqualified = 0f;

    /**
     * 丙一级品
     */
    private Float CFirstClass = 0f;
    /**
     * 丙自用包
     */
    private Float COwnUsePackage = 0f;
    /**
     * 丙超养分
     */
    private Float COverNutrition = 0f;
    /**
     * 丙超水分
     */
    private Float COverWater = 0f;
    /**
     * 丙不合格
     */
    private Float CUnqualified = 0f;


    /**
     * 丁一级品
     */
    private Float DFirstClass = 0f;
    /**
     * 丁自用包
     */
    private Float DOwnUsePackage = 0f;
    /**
     * 丁超养分
     */
    private Float DOverNutrition = 0f;
    /**
     * 丁超水分
     */
    private Float DOverWater = 0f;
    /**
     * 丁不合格
     */
    private Float DUnqualified = 0f;

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
}
