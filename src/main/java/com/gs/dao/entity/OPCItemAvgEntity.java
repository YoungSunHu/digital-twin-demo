package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/29 10:57
 * @modified By：
 */
@Data
@TableName("opc_item_avg")
public class OPCItemAvgEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("item_id")
    private String itemId;

    @TableField("item_type")
    private Integer itemType;

    @TableField("item_avg_value")
    private String itemAvgValue;

    @TableField("factory_id")
    private String factoryId;

    @TableField("create_time")
    private LocalDateTime createTime;

}
