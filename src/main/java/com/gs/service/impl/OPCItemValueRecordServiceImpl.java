package com.gs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.DTO.ItemStatusDTO;
import com.gs.config.Constant;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.mapper.OPCItemValueRecordMapper;
import com.gs.service.OPCItemValueRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/22 13:44
 * @modified By：
 */
@Service
public class OPCItemValueRecordServiceImpl extends ServiceImpl<OPCItemValueRecordMapper, OPCItemValueRecordEntity> implements OPCItemValueRecordService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    @Async
    public void itemCache(OPCItemValueRecordEntity entity) {
        String s = stringRedisTemplate.opsForValue().get("GS:ITEM_CACHE:" + entity.getFactoryId() + ":" + entity.getItemId());
        if (StringUtils.isBlank(s) || (JSON.parseObject(s, OPCItemValueRecordEntity.class).getItemTimestamp().before(entity.getItemTimestamp()))) {
            stringRedisTemplate.opsForValue().set("GS:ITEM_CACHE:" + entity.getFactoryId() + ":" + entity.getItemId(), JSONObject.toJSONString(entity));
        }
    }

    @Override
    public List<OPCItemValueRecordEntity> itemStatus(ItemStatusDTO itemStatusDTO) {
        List<OPCItemValueRecordEntity> opcItemValueRecordEntities = new ArrayList<>();
        if (CollectionUtils.isEmpty(itemStatusDTO.getItemIds())) {
            Set<String> keys = stringRedisTemplate.keys(Constant.REDIS_ITEM_CACHE_PREFIX + itemStatusDTO.getFactoryId() + ":*");
            List<String> strings = stringRedisTemplate.opsForValue().multiGet(keys);
            for (String string : strings) {
                opcItemValueRecordEntities.add(JSON.parseObject(string, OPCItemValueRecordEntity.class));
            }
        } else {
            Set<String> keys = new HashSet<>();
            for (String itemId : itemStatusDTO.getItemIds()) {
                keys.add(Constant.REDIS_ITEM_CACHE_PREFIX + itemStatusDTO.getFactoryId() + ":" + itemId);
            }
            List<String> strings = stringRedisTemplate.opsForValue().multiGet(keys);
            for (String string : strings) {
                opcItemValueRecordEntities.add(JSON.parseObject(string, OPCItemValueRecordEntity.class));
            }
        }
        return opcItemValueRecordEntities;
    }
}
