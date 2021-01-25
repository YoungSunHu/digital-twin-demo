package com.gs.service;

/**
 * 定时任务服务
 */
public interface ScheduleService {
    /**
     * 孪生点位数值更新
     */
    void twinPointUpdate();

    /**
     * 原始点位均值统计
     */
    void itemAverage();
}
