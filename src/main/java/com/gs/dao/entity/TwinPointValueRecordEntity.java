package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 13:50
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "public.twin_point_value_record")
public class TwinPointValueRecordEntity implements Serializable {

    @TableId("id")
    private Long id;

    @TableField("twin_point_id")
    private String twinPointId;

    @TableField("twin_point_code")
    private String twinPointCode;

    @TableField("twin_point_name")
    private String twinPointName;

    @TableField("factory_id")
    private String factoryId;

    @TableField("production_line_id")
    private String productionLineId;

    @TableField("unit")
    private String unit;

    @TableField("data_type")
    private Integer dataType;

    @TableField("twin_point_value")
    private String twinPointValue;

    @TableField("create_time")
    private LocalDateTime createTime;

}