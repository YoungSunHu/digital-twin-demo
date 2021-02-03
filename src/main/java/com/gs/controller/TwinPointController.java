package com.gs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.*;
import com.gs.VO.*;
import com.gs.config.Constant;
import com.gs.dao.entity.*;
import com.gs.exception.BussinessException;
import com.gs.service.*;
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

    @Autowired
    TwinPointAvgService twinPointAvgService;

    @Autowired
    TwinPointValueRecordService twinPointValueRecordService;

    @Autowired
    OPCItemAvgService opcItemAvgService;

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
            throw new BussinessException("点位" + saveTwinPointDTO.getPointId() + "已存在");
        }
        TwinPointEntity twinPointEntity = new TwinPointEntity();
        Double aDouble = null;
        if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isBlank(saveTwinPointDTO.getCalculateScript())) {
            throw new BussinessException("计算脚本不得为空");
        } else if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isNoneBlank(saveTwinPointDTO.getCalculateScript())) {
            //脚本校验
            CalculateScriptTestDTO calculateScriptTestDTO = new CalculateScriptTestDTO();
            calculateScriptTestDTO.setCalculateScript(saveTwinPointDTO.getCalculateScript());
            calculateScriptTestDTO.setFactoryId(saveTwinPointDTO.getFactoryId());
            aDouble = calculateScriptService.calculateScriptRun(calculateScriptTestDTO);
            twinPointEntity.setPointValue(String.valueOf(aDouble));
        } else if (saveTwinPointDTO.getDataType() == 1 && StringUtils.isBlank(saveTwinPointDTO.getItemId())) {
            throw new BussinessException("DCS点位ID不得为空");
        } else if (saveTwinPointDTO.getDataType() == 1) {
            //dcs点位
            IPage<OPCItemValueRecordEntity> page = opcItemValueRecordService.page(new Page<OPCItemValueRecordEntity>(1, 1), new QueryWrapper<OPCItemValueRecordEntity>().eq("item_id", saveTwinPointDTO.getItemId()).eq("factory_id", saveTwinPointDTO.getFactoryId()).orderBy(true, false, "item_timestamp"));
            List<OPCItemValueRecordEntity> records = page.getRecords();
            twinPointEntity.setPointValue(CollectionUtils.isEmpty(records) ? null : records.get(0).getItemValue());
        }
        //下次更新时间
        twinPointEntity.setNextUpdateTime(LocalDateTime.now().plus(saveTwinPointDTO.getCalculateCycle(), ChronoUnit.SECONDS));
        //均值更新时间
        twinPointEntity.setAvgUpdateTime(LocalDateTime.now().plus(saveTwinPointDTO.getCalculateCycle(), ChronoUnit.SECONDS));
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
        TwinPointEntity twinPointEntity = twinPointService.getOne(new QueryWrapper<TwinPointEntity>().eq("point_id", saveTwinPointDTO.getPointId()));
        if (twinPointEntity == null) {
            twinPointEntity = new TwinPointEntity();
        }
        Double aDouble = null;
        if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isBlank(saveTwinPointDTO.getCalculateScript())) {
            throw new BussinessException("计算脚本不得为空");
        } else if (saveTwinPointDTO.getDataType() == 2 && StringUtils.isNoneBlank(saveTwinPointDTO.getCalculateScript())) {
            //脚本校验
            CalculateScriptTestDTO calculateScriptTestDTO = new CalculateScriptTestDTO();
            calculateScriptTestDTO.setCalculateScript(saveTwinPointDTO.getCalculateScript());
            calculateScriptTestDTO.setFactoryId(saveTwinPointDTO.getFactoryId());
            calculateScriptTestDTO.setProductionLineId(saveTwinPointDTO.getProductionLineId());
            aDouble = calculateScriptService.calculateScriptRun(calculateScriptTestDTO);
            twinPointEntity.setPointValue(String.valueOf(aDouble));
        } else if (saveTwinPointDTO.getDataType() == 1 && StringUtils.isBlank(saveTwinPointDTO.getItemId())) {
            throw new BussinessException("DCS点位ID不得为空");
        } else if (saveTwinPointDTO.getDataType() == 1) {
            //dcs点位
            IPage<OPCItemValueRecordEntity> page = opcItemValueRecordService.page(new Page<OPCItemValueRecordEntity>(1, 1), new QueryWrapper<OPCItemValueRecordEntity>().eq("item_id", saveTwinPointDTO.getItemId()).eq("factory_id", saveTwinPointDTO.getFactoryId()).orderBy(true, false, "item_timestamp"));
            List<OPCItemValueRecordEntity> records = page.getRecords();
            twinPointEntity.setPointValue(CollectionUtils.isEmpty(records) ? null : records.get(0).getItemValue());
        }
        BeanUtils.copyProperties(saveTwinPointDTO, twinPointEntity);
        //下次更新时间
        twinPointEntity.setNextUpdateTime(LocalDateTime.now().plus(saveTwinPointDTO.getCalculateCycle(), ChronoUnit.SECONDS));
        //均值更新时间
        twinPointEntity.setAvgUpdateTime(LocalDateTime.now().plus(saveTwinPointDTO.getCalculateCycle(), ChronoUnit.SECONDS));
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

    /**
     * 孪生点位均值曲线
     *
     * @param twinPointAvgLineDTO
     * @return
     */
    @PostMapping("/twinPointAvgLine")
    public CommomResponse twinPointAvgLine(@RequestBody TwinPointAvgLineDTO twinPointAvgLineDTO) {
        QueryWrapper<TwinPointAvgEntity> objectQueryWrapper = new QueryWrapper<TwinPointAvgEntity>()
                .eq("twin_point_id", twinPointAvgLineDTO.getTwinPointId())
                .eq("factory_id", twinPointAvgLineDTO.getFactoryId())
                .between("create_time", twinPointAvgLineDTO.getStartDate(), twinPointAvgLineDTO.getEndDate());
        int count = twinPointAvgService.count(objectQueryWrapper);
        if (count == 0) {
            return CommomResponse.data("success", null);
        }
        int step = (count / twinPointAvgLineDTO.getPointStep()) > 0 ? (count / twinPointAvgLineDTO.getPointStep()) : 1;
        objectQueryWrapper = objectQueryWrapper.eq("id%" + step, 0).orderBy(true, true, "create_time");

        List<TwinPointAvgEntity> list = twinPointAvgService.list(objectQueryWrapper);
        return CommomResponse.data("success", list);
    }

    /**
     * 孪生点位曲线
     *
     * @param twinPointLineDTO
     * @return
     */
    @PostMapping("/twinPointLine")
    public CommomResponse twinPointLine(@RequestBody TwinPointLineDTO twinPointLineDTO) {
        QueryWrapper<TwinPointValueRecordEntity> objectQueryWrapper = new QueryWrapper<TwinPointValueRecordEntity>()
                .eq("twin_point_id", twinPointLineDTO.getTwinPointId())
                .eq("factory_id", twinPointLineDTO.getFactoryId())
                .between("create_time", twinPointLineDTO.getStartDate(), twinPointLineDTO.getEndDate());
        int count = twinPointValueRecordService.count(objectQueryWrapper);
        if (count == 0) {
            return CommomResponse.data("success", null);
        }
        int step = (count / twinPointLineDTO.getPointStep()) > 0 ? (count / twinPointLineDTO.getPointStep()) : 1;
        objectQueryWrapper = objectQueryWrapper.eq("id%" + step, 0).orderBy(true, true, "create_time");
        List<TwinPointValueRecordEntity> list = twinPointValueRecordService.list(objectQueryWrapper);
        return CommomResponse.data("success", list);
    }

    /**
     * dcs点位曲线
     *
     * @param itemLineDTO
     * @return
     */
    @PostMapping("/itemLine")
    public CommomResponse itemLine(@RequestBody ItemLineDTO itemLineDTO) {
        QueryWrapper<OPCItemValueRecordEntity> objectQueryWrapper = new QueryWrapper<OPCItemValueRecordEntity>()
                .eq("item_id", itemLineDTO.getItemId())
                .between("create_time", itemLineDTO.getStartDate(), itemLineDTO.getEndDate());
        int count = opcItemValueRecordService.count(objectQueryWrapper);
        if (count == 0) {
            return CommomResponse.data("success", null);
        }
        int step = (count / itemLineDTO.getPointStep()) > 0 ? (count / itemLineDTO.getPointStep()) : 1;
        objectQueryWrapper = objectQueryWrapper.eq("id%" + step, 0).orderBy(true, true, "item_timestamp");
        List<OPCItemValueRecordEntity> list = opcItemValueRecordService.list(objectQueryWrapper);
        return CommomResponse.data("success", list);
    }

    /**
     * dcs点位均值曲线
     *
     * @param itemLineDTO
     * @return
     */
    @PostMapping("/itemAvgLine")
    public CommomResponse itemAvgLine(@RequestBody ItemLineDTO itemLineDTO) {
        QueryWrapper<OPCItemAvgEntity> objectQueryWrapper = new QueryWrapper<OPCItemAvgEntity>()
                .eq("item_id", itemLineDTO.getItemId())
                .between("create_time", itemLineDTO.getStartDate(), itemLineDTO.getEndDate());
        int count = opcItemAvgService.count(objectQueryWrapper);
        if (count == 0) {
            return CommomResponse.data("success", null);
        }
        int step = (count / itemLineDTO.getPointStep()) > 0 ? (count / itemLineDTO.getPointStep()) : 1;
        objectQueryWrapper = objectQueryWrapper.eq("id%" + step, 0).orderBy(true, true, "create_time");
        List<OPCItemAvgEntity> list = opcItemAvgService.list(objectQueryWrapper);
        return CommomResponse.data("success", list);
    }

    /**
     * dcs点位均值曲线
     *
     * @param compoundLineDTO
     * @return
     */
    @PostMapping("/compoundLine")
    public CommomResponse compoundLine(@RequestBody CompoundLineDTO compoundLineDTO) {
        CompoundLineVO compoundLineVO = new CompoundLineVO();
        List<TwinPointLineVO> twinPointLineVOS = new ArrayList<>();
        List<TwinPointAvgLineVO> twinPointAvgLineVO = new ArrayList<>();
        List<ItemLineVO> itemLineVOS = new ArrayList<>();
        List<ItemAvgLineVO> itemAvgLineVOS = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(compoundLineDTO.getTwinPointIds())) {
            for (String s : compoundLineDTO.getTwinPointIds()) {
                TwinPointLineDTO twinPointLineDTO = new TwinPointLineDTO();
                twinPointLineDTO.setTwinPointId(s);
                twinPointLineDTO.setFactoryId(compoundLineDTO.getFactoryId());
                twinPointLineDTO.setStartDate(compoundLineDTO.getStartDate());
                twinPointLineDTO.setEndDate(compoundLineDTO.getEndDate());
                twinPointLineDTO.setTwinPointId(s);
                twinPointLineDTO.setPointStep(compoundLineDTO.getPointStep());
                CommomResponse commomResponse = this.twinPointLine(twinPointLineDTO);
                TwinPointLineVO twinPointLineVO = new TwinPointLineVO();
                twinPointLineVO.setTwinPointId(s);
                twinPointLineVO.setTwinPointList((List) commomResponse.getData());
                twinPointLineVOS.add(twinPointLineVO);
            }
        }

        if (CollectionUtils.isNotEmpty(compoundLineDTO.getTwinPointAvgIds())) {
            for (String s : compoundLineDTO.getTwinPointAvgIds()) {
                TwinPointAvgLineDTO twinPointAvgLineDTO = new TwinPointAvgLineDTO();
                twinPointAvgLineDTO.setFactoryId(compoundLineDTO.getFactoryId());
                twinPointAvgLineDTO.setStartDate(compoundLineDTO.getStartDate());
                twinPointAvgLineDTO.setEndDate(compoundLineDTO.getEndDate());
                twinPointAvgLineDTO.setTwinPointId(s);
                twinPointAvgLineDTO.setPointStep(compoundLineDTO.getPointStep());
                CommomResponse commomResponse = this.twinPointAvgLine(twinPointAvgLineDTO);
                TwinPointAvgLineVO twinPointAvgLineVO1 = new TwinPointAvgLineVO();
                twinPointAvgLineVO1.setTwinPointId(s);
                twinPointAvgLineVO1.setTwinPointAvgList((List) commomResponse.getData());
                twinPointAvgLineVO.add(twinPointAvgLineVO1);
            }
        }

        if (CollectionUtils.isNotEmpty(compoundLineDTO.getItemIds())) {
            for (String s : compoundLineDTO.getItemIds()) {
                ItemLineDTO itemLineDTO = new ItemLineDTO();
                itemLineDTO.setItemId(s);
                itemLineDTO.setStartDate(compoundLineDTO.getStartDate());
                itemLineDTO.setEndDate(compoundLineDTO.getEndDate());
                itemLineDTO.setPointStep(compoundLineDTO.getPointStep());
                CommomResponse commomResponse = this.itemLine(itemLineDTO);
                ItemLineVO itemLineVO = new ItemLineVO();
                itemLineVO.setItemId(s);
                itemLineVO.setItemList((List) commomResponse.getData());
                itemLineVOS.add(itemLineVO);
            }
        }

        if (CollectionUtils.isNotEmpty(compoundLineDTO.getItemAvgIds())) {
            for (String s : compoundLineDTO.getItemAvgIds()) {
                ItemLineDTO itemLineDTO = new ItemLineDTO();
                itemLineDTO.setItemId(s);
                itemLineDTO.setStartDate(compoundLineDTO.getStartDate());
                itemLineDTO.setEndDate(compoundLineDTO.getEndDate());
                itemLineDTO.setPointStep(compoundLineDTO.getPointStep());
                CommomResponse commomResponse = this.itemAvgLine(itemLineDTO);
                ItemAvgLineVO itemAvgLineVO = new ItemAvgLineVO();
                itemAvgLineVO.setItemId(s);
                itemAvgLineVO.setItemAvgList((List) commomResponse.getData());
                itemAvgLineVOS.add(itemAvgLineVO);
            }
        }
        compoundLineVO.setTwinPointList(twinPointLineVOS);
        compoundLineVO.setTwinPointAvgtList(twinPointAvgLineVO);
        compoundLineVO.setItemList(itemLineVOS);
        compoundLineVO.setItemAvgList(itemAvgLineVOS);
        return CommomResponse.data("success", compoundLineVO);
    }

}
