package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/28 11:31
 * @modified By：
 */
@Data
@TableName("formula")
public class FormulaEntity {

    @TableId(value = "id", type = IdType.AUTO)
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

    @TableField("formula_date")
    private LocalDate formulaDate;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();
}
