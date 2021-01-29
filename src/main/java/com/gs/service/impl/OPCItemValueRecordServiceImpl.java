package com.gs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.DTO.ItemStatusDTO;
import com.gs.config.Constant;
import com.gs.dao.entity.OPCItemAvgEntity;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.mapper.OPCItemValueRecordMapper;
import com.gs.service.OPCItemAvgService;
import com.gs.service.OPCItemService;
import com.gs.service.OPCItemValueRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    OPCItemValueRecordMapper opcItemValueRecordMapper;

    @Autowired
    OPCItemService opcItemService;

    @Autowired
    OPCItemAvgService opcItemAvgService;

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
            if (CollectionUtils.isEmpty(strings) || strings.get(0) == null) {
                return opcItemValueRecordEntities;
            }
            for (String string : strings) {
                opcItemValueRecordEntities.add(JSON.parseObject(string, OPCItemValueRecordEntity.class));
            }
        }
        return opcItemValueRecordEntities;
    }

    @Async
    @Override
    public void itemAverage(Long itemId) {
        OPCItemEntity opcItemEntity = opcItemService.getOne(new QueryWrapper<OPCItemEntity>().eq("id", itemId));
        if (opcItemEntity == null) {
            return;
        }
        Double avg = opcItemValueRecordMapper.itemAverage(opcItemEntity.getFactoryId(), opcItemEntity.getItemId(), LocalDateTime.now().minus(opcItemEntity.getAvgUpdateCycle(), ChronoUnit.SECONDS), LocalDateTime.now());
        OPCItemAvgEntity opcItemAvgEntity = new OPCItemAvgEntity();
        opcItemAvgEntity.setItemAvgValue(String.valueOf(avg));
        opcItemAvgEntity.setItemType(opcItemEntity.getItemType());
        opcItemAvgEntity.setItemId(opcItemEntity.getItemId());
        opcItemAvgEntity.setFactoryId(opcItemEntity.getFactoryId());
        opcItemEntity.setAvgUpdateTime(LocalDateTime.now().plus(opcItemEntity.getAvgUpdateCycle(), ChronoUnit.SECONDS));
        opcItemAvgService.save(opcItemAvgEntity);
        stringRedisTemplate.opsForValue().set(Constant.REDIS_ITEM_AVG_CACHE_PREFIX + opcItemEntity.getFactoryId() + ":" + opcItemEntity.getItemId(), String.valueOf(avg));
        opcItemService.updateById(opcItemEntity);
    }
}
