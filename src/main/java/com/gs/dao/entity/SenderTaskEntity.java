package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/3 13:53
 * @modified By：
 */
@TableName("sender_task")
public class SenderTaskEntity {

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @TableField("task_status")
    private Integer task_status;

    @TableField("data_id")
    private Integer data_id;

    @TableField("data_pointer_id")
    private Long data_pointer_id;

    @TableField("factory_id")
    private String factory_id;

    @TableField("send_mode")
    private Integer send_mode;

    @TableField("send_cycle")
    private Integer send_cycle;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
