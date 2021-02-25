package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/3 15:19
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("chemical_examination")
public class ChemicalExaminationEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("factory_id")
    private String factoryId;

    @TableField("production_line_id")
    private String productionLineId;

    @TableField("production_line_code")
    private String productionLineCode;

    @TableField("exam_code")
    private String examCode;

    @TableField("exam_item_name")
    private String examItemName;

    @TableField("exam_item_value")
    private String examItemValue;

    @TableField("exam_time")
    private LocalDateTime examTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();
}
