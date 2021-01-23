package com.gs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gs.DTO.CalculateScriptTestDTO;
import com.gs.DTO.ItemListDTO;
import com.gs.DTO.SaveTwinPointDTO;
import com.gs.DTO.TwinPointListDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.service.CalculateScriptService;
import com.gs.service.OPCItemService;
import com.gs.service.OPCItemValueRecordService;
import com.gs.service.TwinPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CommomResponse saveTwinPoint(@RequestBody SaveTwinPointDTO saveTwinPointDTO) {
        TwinPointEntity twinPointEntity = new TwinPointEntity();
        BeanUtils.copyProperties(saveTwinPointDTO, twinPointEntity);
        twinPointService.save(twinPointEntity);
        return CommomResponse.success("保存成功");
    }


    /**
     * 孪生点位列表
     *
     * @param twinPointListDTO
     * @return
     */
    @PostMapping("/twinPointList")
    public CommomResponse twinPointList(@RequestBody TwinPointListDTO twinPointListDTO) {
        QueryWrapper<TwinPointEntity> twinPointEntityQueryWrapper = new QueryWrapper<TwinPointEntity>()
                .eq("factory_id", twinPointListDTO.getFactoryId());
        List<TwinPointEntity> list = twinPointService.list(twinPointEntityQueryWrapper);
        return CommomResponse.data("success", list);
    }


    /**
     * 计算脚本测试
     *
     * @param calculateScriptTestDTO
     * @return
     */
    @PostMapping("/calculateScriptTest")
    public CommomResponse calculateScriptTest(@RequestBody CalculateScriptTestDTO calculateScriptTestDTO) {
        Double aDouble = calculateScriptService.calculateScriptTest(calculateScriptTestDTO);
        return CommomResponse.data("success", aDouble);
    }


}
