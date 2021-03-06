package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/4 14:57
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TaskSaveDTO {

    /**
     * 任务状态 默认为暂停
     */
    private Integer taskStatus = 1;

    /**
     * 关联数据ID
     */
    @NotNull(message = "dataId不得为空")
    private Long dataId;

    /**
     * 指向的最后data_detail数据id
     */
    private Long dataPointerId;

    /**
     * 工厂编号
     */
    @NotNull(message = "factoryId不得为空")
    private String factoryId;

    /**
     * 生产线编号
     */
    private String productionLineCode;

    /**
     * 发送模式 0:循环发送 1:一次发送
     */
    @NotNull(message = "sendMode不得为空")
    private Integer sendMode;

    /**
     * 发送周期
     */
    private Integer sendCycle = 60;
}
