package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/23 10:31
 * @modified By：
 */
@TableName("opc_item")
public class OPCItemEntity {
    @TableId("id")
    private Long id;
    @TableField("factory_id")
    private String factoryId;
    @TableField("item_id")
    private String itemId;
    @TableField("create_time")
    private String createTime;
    @TableField("update_time")
    private String updateTime;
}
