package com.gs.service;

import java.io.InputStream;

/**
 * 数据发送器服务
 */
public interface DataSenderService {
    /**
     * 数据导入
     *
     * @param inputStream
     */
    void DataInbound(InputStream inputStream);
}
