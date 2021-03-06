package com.gs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.dao.entity.SenderDataEntity;
import com.gs.dao.entity.SenderTaskEntity;
import com.gs.dao.mapper.SenderDataMapper;
import com.gs.dao.mapper.SenderTaskMapper;
import com.gs.service.SenderDataService;
import com.gs.service.SenderTaskService;
import org.springframework.stereotype.Service;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/3 14:47
 * @modified By：
 */
@Service
public class SenderDataServiceImpl extends ServiceImpl<SenderDataMapper, SenderDataEntity> implements SenderDataService {
}
