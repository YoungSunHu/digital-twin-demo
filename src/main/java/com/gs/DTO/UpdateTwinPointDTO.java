package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/24 9:21
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class UpdateTwinPointDTO {

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

    private Integer calculateCycle = 3600;

    private Long warnPointId;

    private Float warnPercent;

    private Float thresholdMax;

    private Float thresholdMin;

    @NotNull(message = "dataType不得为空")
    private Float dataType;

    /**
     * 孪生点位计算脚本
     */
    private String calculateScript;
}
