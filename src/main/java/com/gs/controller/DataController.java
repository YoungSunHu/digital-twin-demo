package com.gs.controller;

import com.gs.VO.CommomResponse;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.mapper.OPCItemValueRecordMapper;
import com.gs.service.OPCItemValueRecordService;
import com.gs.service.TwinPointValueRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/18 17:13
 * @modified By：
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class DataController {

    @Autowired
    OPCItemValueRecordMapper opcItemValueRecordMapper;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Autowired
    TwinPointValueRecordService twinPointValueRecordService;

    @PostMapping("/itemUpload")
    public CommomResponse itemUpload(@RequestBody OPCItemValueRecordEntity entity) {
        log.info("接收OPCItem数据:{}", entity.toString());
        entity.setId(null);
        opcItemValueRecordMapper.insert(entity);
        opcItemValueRecordService.itemCache(entity);
        twinPointValueRecordService.updateDCSTwinPoint(entity);
        return CommomResponse.success("success");
    }

    /**
     * 发送器数据接收接口
     *
     * @param entity
     * @return
     */
    @PostMapping("/senderDataRecevice")
    public CommomResponse senderDataRecevice(@RequestBody OPCItemValueRecordEntity entity) {
        log.info("接收OPCItem数据:{}", entity.toString());
        entity.setId(null);
        opcItemValueRecordMapper.insert(entity);
        opcItemValueRecordService.itemCache(entity);
        twinPointValueRecordService.updateDCSTwinPoint(entity);
        return CommomResponse.success("success");
    }


}
