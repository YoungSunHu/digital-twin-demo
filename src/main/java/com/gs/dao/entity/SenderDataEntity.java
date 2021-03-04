package com.gs.dao.entity;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/3 13:53
 * @modified By：
 */
@Data
@TableName("sender_data")
public class SenderDataEntity {
    @TableId(value = "id")
    private Long id;

    @TableField("point_info")
    private String pointInfo;

    @TableField("data_name")
    private String dataName;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    @TableField(exist = false)
    private Snowflake snowflake = new Snowflake(1, 1);

    public SenderDataEntity() {
        this.id = snowflake.nextId() / 1000;
    }
}
