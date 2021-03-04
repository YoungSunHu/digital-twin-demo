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
     * 孪生点位均值统计
     */
    void twinPointAverage();

    /**
     * 原始点位均值统计
     */
    void itemAverage();

    /**
     * 模拟数据发送
     */
    void dataSend();
}
