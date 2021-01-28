package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/28 11:31
 * @modified By：
 */
@Data
@TableName("factory_formula")
public class FormulaEntity {

    @TableId("id")
    private Long id;

    @TableField("factory_id")
    private String factoryId;

    @TableField("formula_json")
    private String formulaJson;

    @TableField("formula_name")
    private String formulaName;

    @TableField("formula_model")
    private String formulaModel;

    @TableField("formula_code")
    private String formulaCode;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();
}
