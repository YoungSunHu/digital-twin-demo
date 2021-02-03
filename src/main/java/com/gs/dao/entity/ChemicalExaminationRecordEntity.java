package com.gs.dao.entity;

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
@TableName("chemical_examination_record")
public class ChemicalExaminationRecordEntity {
    @TableId("id")
    private Long id;
    @TableField("factory_id")
    private String factoryId;
    @TableField("production_line_id")
    private String productionLineId;
    @TableField("exam_item1_name")
    private String examItem1Name;
    @TableField("exam_item1_value")
    private String examItem1Value;
    @TableField("exam_item2_name")
    private String examItem2Name;
    @TableField("exam_item2_value")
    private String examItem2Value;
    @TableField("exam_item3_name")
    private String examItem3Name;
    @TableField("exam_item3_value")
    private String examItem3Value;
    @TableField("exam_item4_name")
    private String examItem4Name;
    @TableField("exam_item4_value")
    private String examItem4Value;
    @TableField("exam_item5_name")
    private String examItem5Name;
    @TableField("exam_item5_value")
    private String examItem5Value;
    @TableField("exam_time")
    private LocalDateTime examTime;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();
}
