package com.gs.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/18 11:15
 * @modified By：
 */
@ToString
@Data
@TableName("public.opc_item_value_record")
public class OPCItemValueRecordEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("item_id")
    private String itemId;

    @TableField("item_type")
    private Integer itemType;

    @TableField("item_value")
    private String itemValue;

    @TableField("is_success")
    private Boolean isSuccess;

    @TableField("item_timestamp")
    private Date itemTimestamp;

    @TableField("factory_id")
    private String factoryId;

    @TableField("data_source")
    private Integer dataSource;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime = new Date();

}
