package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 13:50
 * @modified By：
 * 孪生点位实体
 */
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "public.twin_point")
public class TwinPointEntity implements Serializable {

    @TableId("id")
    private Long id;

    @TableField("point_id")
    private String pointId;

    @TableField("point_code")
    private String pointCode;

    @TableField("point_name")
    private String pointName;

    @TableField("factory_id")
    private String factoryId;

    @TableField("production_line_id")
    private String productionLineId;

    @TableField("unit")
    private String unit;

    @TableField("decimal_palces")
    private Integer decimalPalces;

    @TableField("calculate_sequence")
    private Integer calculateSequence;

    @TableField("calculate_cycle")
    private Integer calculateCycle;

    @TableField("warn_point_id")
    private Long warnPointId;

    @TableField("warn_percent")
    private Float warnPercent;

    @TableField("threshold_max")
    private Float thresholdMax;

    @TableField("threshold_min")
    private Float thresholdMin;

    @TableField("data_type")
    private Integer dataType;

    @TableField("calculate_script")
    private String calculateScript;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
