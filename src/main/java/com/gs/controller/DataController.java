package com.gs.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.gs.DTO.ItemStatusDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.mapper.OPCItemValueRecordMapper;
import com.gs.service.OPCItemValueRecordService;
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

    Snowflake snowflake = IdUtil.createSnowflake(1, 1);

    @PostMapping("/itemUpload")
    public CommomResponse itemUpload(@RequestBody OPCItemValueRecordEntity entity) {
        log.info("接收OPCItem数据:{}", entity.toString());
        entity.setId(snowflake.nextId());
        opcItemValueRecordMapper.insert(entity);
        opcItemValueRecordService.itemCache(entity);
        return CommomResponse.success("success");
    }


}
