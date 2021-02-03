package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/3 17:03
 * @modified By：
 */
@Data
@TableName("param_config")
public class ParamConfigEntity {

    @TableId("id")
    private Long id;

    @TableField("param_type")
    private String paramType;

    @TableField("param_key")
    private String paramKey;

    @TableField("param_value")
    private String paramValue;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

}
