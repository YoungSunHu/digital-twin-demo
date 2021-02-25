package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 13:50
 * @modified By：
 * 孪生点位实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "public.twin_point")
public class TwinPointEntity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
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

    @TableField("calculate_frequency")
    private Integer calculateFrequency;

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

    @TableField("point_value")
    private String pointValue;

    @TableField("item_id")
    private String itemId;

    @TableField("next_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextUpdateTime;

    @TableField("avg_update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime avgUpdateTime;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 关联化验项ID
     */
    @TableField("chemical_examination_id")
    private Long chemicalExaminationId;

    @TableField("exam_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime examTime;

    public TwinPointValueRecordEntity genRecordEntity() {
        TwinPointValueRecordEntity twinPointValueRecordEntity = new TwinPointValueRecordEntity();
        twinPointValueRecordEntity.setPointId(this.id);
        twinPointValueRecordEntity.setTwinPointId(this.pointId);
        twinPointValueRecordEntity.setProductionLineId(this.productionLineId);
        twinPointValueRecordEntity.setTwinPointCode(this.pointCode);
        twinPointValueRecordEntity.setDataType(this.dataType);
        twinPointValueRecordEntity.setUnit(this.unit);
        twinPointValueRecordEntity.setFactoryId(this.factoryId);
        twinPointValueRecordEntity.setTwinPointValue(this.pointValue);
        return twinPointValueRecordEntity;
    }

}
