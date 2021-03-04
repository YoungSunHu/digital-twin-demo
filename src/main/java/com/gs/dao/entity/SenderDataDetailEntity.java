package com.gs.dao.entity;

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
@TableName("sender_data_detail")
public class SenderDataDetailEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("data_id")
    private Long dataId;

    @TableField("opc_item_type")
    private Integer opcItemType;

    @TableField("opc_item_id")
    private String opcItemId;

    @TableField("opc_item_name")
    private String opcItemName;

    @TableField("opc_item_value")
    private String opcItemValue;

    @TableField("opc_item_timestamp")
    private LocalDateTime opcItemTimestamp;

    @TableField("data_type")
    private Integer dataType;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime = LocalDateTime.now();
}
