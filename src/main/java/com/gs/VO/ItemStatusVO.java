package com.gs.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/24 10:38
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStatusVO {
    private Long id;

    private String itemId;

    private Integer itemType;

    private String itemValue;

    private Boolean isSuccess;

    private Date itemTimestamp;

    private String factoryId;

    /**
     * 点位均值
     */
    private String valueAvg;

    private Date createTime;

    private Date updateTime;

}
