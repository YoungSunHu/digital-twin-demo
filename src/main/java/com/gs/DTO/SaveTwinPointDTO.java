package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 14:58
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SaveTwinPointDTO implements Serializable {

    private Long id;

    @NotBlank(message = "pointId不得为空")
    private String pointId;

    private String pointCode;

    private String pointName;

    @NotBlank(message = "factoryId不得为空")
    private String factoryId;

    @NotBlank(message = "productionLineId不得为空")
    private String productionLineId;

    private String unit;

    private Integer decimalPalces = 2;

    private Integer calculateSequence = 10000;

    /**
     * 脚本计算周期
     */
    private Integer calculateCycle = 3600;

    /**
     * 当前值计算频率:
     */
    //private Integer calculateFrequency = 1800;

    private Long warnPointId;

    private Float warnPercent;

    private Float thresholdMax;

    private Float thresholdMin;

    @NotNull(message = "dataType不得为空")
    private Integer dataType;

    /**
     * 孪生点位计算脚本
     */
    private String calculateScript;

    /**
     * 孪生点位值 为固定值时必填
     */
    private String pointValue;

    private String itemId;

    /**
     * 关联化验项ID
     */
    private Long chemicalExaminationId;


}
