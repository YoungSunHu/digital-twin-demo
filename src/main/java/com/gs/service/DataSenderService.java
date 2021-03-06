package com.gs.service;

import com.gs.dao.entity.SenderTaskEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数据发送器服务
 */
public interface DataSenderService {
    /**
     * 数据导入
     *
     * @param file
     */
    void DataInbound(MultipartFile file);

    /**
     * 数据发送
     *
     * @param tasks
     */
    void DataSend(List<SenderTaskEntity> tasks);
}
