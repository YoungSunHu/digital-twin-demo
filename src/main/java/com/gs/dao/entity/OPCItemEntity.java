package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/23 10:31
 * @modified By：
 */
@Data
@TableName("opc_item")
public class OPCItemEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("factory_id")
    private String factoryId;

    @TableField("item_id")
    private String itemId;

    /**
     * item由后台自行标注,不从opcserver中获取
     */
    @TableField("item_name")
    private String itemName;
    /**
     * 均值更新周期
     */
    @TableField("avg_update_cycle")
    private Integer avgUpdateCycle;

    /**
     * 均值更新时间
     */
    @TableField("avg_update_time")
    private LocalDateTime avgUpdateTime;


    @TableField("item_type")
    private Integer itemType;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
