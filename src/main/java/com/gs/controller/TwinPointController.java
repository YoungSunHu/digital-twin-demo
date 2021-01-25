package com.gs.controller;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.*;
import com.gs.VO.CommomResponse;
import com.gs.VO.ItemStatusVO;
import com.gs.config.Constant;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.service.CalculateScriptService;
import com.gs.service.OPCItemService;
import com.gs.service.OPCItemValueRecordService;
import com.gs.service.TwinPointService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 14:30
 * @modified By：
 */
@RestController
@RequestMapping("/twinPoint")
public class TwinPointController {

    @Autowired
    TwinPointService twinPointService;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Autowired
    OPCItemService opcItemService;

    @Autowired
    CalculateScriptService calculateScriptService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    Snowflake snowflake = new Snowflake(2, 2);

    /**
     * dcs点位列表
     *
     * @return
     */
    @PostMapping("/itemList")
    public CommomResponse itemList(@RequestBody ItemListDTO itemListDTO) {
        List<OPCItemEntity> opcItemEntities = opcItemService.list(new QueryWrapper<OPCItemEntity>().eq("factory_id", itemListDTO.getFactoryId()));
        return CommomResponse.data("success", opcItemEntities);
    }

    /**
     * 点位保存
     *
     * @param saveTwinPointDTO
     * @return
     */
    @PostMapping("/saveTwinPoint")
    public CommomResponse saveTwinPoint(@RequestBody @Validated SaveTwinPointDTO saveTwinPointDTO) {
        int count = twinPointService.count(new QueryWrapper<TwinPointEntity>().eq("point_id", saveTwinPointDTO.getPointId()).eq("factory_id", saveTwinPointDTO.getFactoryId()));
        if (count > 0) {
            throw new RuntimeException("点位" + saveTwinPointDTO.getPointId() + "已存在");
        }
        TwinPointEntity twinPointEntity = new TwinPointEntity();
        Double aDouble = null;
        if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isBlank(saveTwinPointDTO.getCalculateScript())) {
            throw new RuntimeException("计算脚本不得为空");
        } else if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isNoneBlank(saveTwinPointDTO.getCalculateScript())) {
            //脚本校验
            CalculateScriptTestDTO calculateScriptTestDTO = new CalculateScriptTestDTO();
            calculateScriptTestDTO.setCalculateScript(saveTwinPointDTO.getCalculateScript());
            calculateScriptTestDTO.setFactoryId(saveTwinPointDTO.getFactoryId());
            aDouble = calculateScriptService.calculateScriptRun(calculateScriptTestDTO);
            twinPointEntity.setPointValue(String.valueOf(aDouble));
        } else if (saveTwinPointDTO.getDataType() == 1) {
            //dcs点位
            IPage<OPCItemValueRecordEntity> page = opcItemValueRecordService.page(new Page<OPCItemValueRecordEntity>(1, 1), new QueryWrapper<OPCItemValueRecordEntity>().eq("item_id", saveTwinPointDTO.getItemId()).eq("factory_id", saveTwinPointDTO.getFactoryId()).orderBy(true, false, "item_timestamp"));
            List<OPCItemValueRecordEntity> records = page.getRecords();
            twinPointEntity.setPointValue(CollectionUtils.isEmpty(records) ? null : records.get(0).getItemValue());
        }
        //下次更新时间
        twinPointEntity.setNextUpdateTime(LocalDateTime.now().plus(saveTwinPointDTO.getCalculateCycle(), ChronoUnit.SECONDS));
        twinPointEntity.setId(snowflake.nextId());
        BeanUtils.copyProperties(saveTwinPointDTO, twinPointEntity);
        twinPointService.save(twinPointEntity);
        return CommomResponse.success("保存成功");
    }

    /**
     * 点位保存更新
     *
     * @param saveTwinPointDTO
     * @return
     */
    @PostMapping("/saveOrUpdateTwinPoint")
    public CommomResponse saveOrUpdateTwinPoint(@RequestBody @Validated SaveTwinPointDTO saveTwinPointDTO) {
        if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isBlank(saveTwinPointDTO.getCalculateScript())) {
            throw new RuntimeException("计算脚本不得为空");
        }
        TwinPointEntity twinPointEntity = new TwinPointEntity();
        BeanUtils.copyProperties(saveTwinPointDTO, twinPointEntity);
        //下次更新时间
        twinPointEntity.setNextUpdateTime(LocalDateTime.now().plus(saveTwinPointDTO.getCalculateCycle(), ChronoUnit.SECONDS));
        twinPointService.saveOrUpdate(twinPointEntity, new QueryWrapper<TwinPointEntity>().eq("point_id", saveTwinPointDTO.getPointId()).eq("factory_id", saveTwinPointDTO.getFactoryId()));
        return CommomResponse.success("保存成功");
    }

    /**
     * 孪生点位列表
     *
     * @param twinPointListDTO
     * @return
     */
    @PostMapping("/twinPointList")
    public CommomResponse twinPointList(@RequestBody @Validated TwinPointListDTO twinPointListDTO) {
        QueryWrapper<TwinPointEntity> twinPointEntityQueryWrapper = new QueryWrapper<TwinPointEntity>()
                .eq("factory_id", twinPointListDTO.getFactoryId())
                .eq(StringUtils.isNoneBlank(twinPointListDTO.getProductionLineId()), "production_line_id", twinPointListDTO.getProductionLineId())
                .in(CollectionUtils.isNotEmpty(twinPointListDTO.getTwinPointIds()), "point_id", twinPointListDTO.getTwinPointIds());
        List<TwinPointEntity> list = twinPointService.list(twinPointEntityQueryWrapper);
        return CommomResponse.data("success", list);
    }

    /**
     * 删除孪生点位
     *
     * @param delTwinPointDTO
     * @return
     */
    @PostMapping("/delTwinPoint")
    public CommomResponse delTwinPoint(@RequestBody DelTwinPointDTO delTwinPointDTO) {
        twinPointService.remove(new QueryWrapper<TwinPointEntity>().eq("point_id", delTwinPointDTO.getPointId()).eq("factory_id", delTwinPointDTO.getFactoryId()).eq(delTwinPointDTO.getId() != null, "id", delTwinPointDTO.getId()));
        return CommomResponse.success("success");
    }

    /**
     * 修改点位信息
     *
     * @param updateTwinPointDTO
     * @return
     */
    @PostMapping("/updateTwinPoint")
    public CommomResponse updateTwinPoint(@RequestBody @Validated UpdateTwinPointDTO updateTwinPointDTO) {
        TwinPointEntity twinPointEntity = twinPointService.getOne(new QueryWrapper<TwinPointEntity>().eq("point_id", updateTwinPointDTO.getPointId()).eq("factory_id", updateTwinPointDTO.getFactoryId()));
        Long id = twinPointEntity.getId();
        BeanUtils.copyProperties(updateTwinPointDTO, twinPointEntity);
        twinPointEntity.setId(id);
        twinPointService.updateById(twinPointEntity);
        return CommomResponse.success("success");
    }


    /**
     * 计算脚本测试
     *
     * @param calculateScriptTestDTO
     * @return
     */
    @PostMapping("/calculateScriptTest")
    public CommomResponse calculateScriptTest(@RequestBody CalculateScriptTestDTO calculateScriptTestDTO) {
        Double aDouble = calculateScriptService.calculateScriptRun(calculateScriptTestDTO);
        return CommomResponse.data("success", aDouble);
    }

    /**
     * dcs点位状态
     *
     * @param itemStatusDTO
     * @return
     */
    @PostMapping("/itemStatus")
    public CommomResponse itemStatus(@RequestBody ItemStatusDTO itemStatusDTO) {
        List<OPCItemValueRecordEntity> opcItemValueRecordEntities = opcItemValueRecordService.itemStatus(itemStatusDTO);
        List<ItemStatusVO> vos = new ArrayList<>();
        opcItemValueRecordEntities.forEach(
                i -> {
                    ItemStatusVO itemStatusVO = new ItemStatusVO();
                    BeanUtils.copyProperties(i, itemStatusVO);
                    //获取均值
                    String s = stringRedisTemplate.opsForValue().get(Constant.REDIS_ITEM_AVG_CACHE_PREFIX + i.getFactoryId() + ":" + i.getItemId());
                    itemStatusVO.setValueAvg(s);
                    vos.add(itemStatusVO);
                }
        );
        return CommomResponse.data("success", vos);
    }

}
