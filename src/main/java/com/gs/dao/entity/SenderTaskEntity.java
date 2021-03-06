package com.gs.dao.entity;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
@TableName("sender_task")
public class SenderTaskEntity {

    @TableId(value = "id")
    private Long id;
    /**
     * 任务状态 0:进行中 1:已暂停 2:已过期'
     */
    @TableField("task_status")
    private Integer taskStatus;

    @TableField("data_id")
    private Long dataId;

    @TableField("data_name")
    private String dataName;

    @TableField("data_pointer_time")
    private LocalDateTime dataPointerTime;

    @TableField("factory_id")
    private String factoryId;

    @TableField("production_line_code")
    private String productionLineCode;

    @TableField("send_mode")
    private Integer sendMode;

    @TableField("send_cycle")
    private Integer sendCycle;

    @TableField("next_send_time")
    private LocalDateTime nextSendTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();

    public SenderTaskEntity() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 2);
        this.id = snowflake.nextId() / 1000;
    }
}
