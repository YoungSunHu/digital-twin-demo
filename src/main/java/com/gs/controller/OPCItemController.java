package com.gs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gs.DTO.ItemDelDTO;
import com.gs.DTO.ItemListDTO;
import com.gs.DTO.ItemSaveDTO;
import com.gs.DTO.ItemUpdateDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.service.OPCItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/5 11:07
 * @modified By：
 */
@RestController
@RequestMapping("/OPCItem")
public class OPCItemController {


    @Autowired
    OPCItemService opcItemService;

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
     * 新增dcs点位配置
     *
     * @return
     */
    @PostMapping("/itemSave")
    public CommomResponse itemSave(@RequestBody @Validated ItemSaveDTO dto) {
        OPCItemEntity opcItemEntity = new OPCItemEntity();
        BeanUtils.copyProperties(dto, opcItemEntity);
        opcItemEntity.setAvgUpdateTime(LocalDateTime.now().plus(opcItemEntity.getAvgUpdateCycle(), ChronoUnit.SECONDS));
        opcItemService.save(opcItemEntity);
        return CommomResponse.success("success");
    }

    /**
     * 删除dcs点位配置
     *
     * @return
     */
    @PostMapping("/itemDel")
    public CommomResponse itemDel(@RequestBody @Validated ItemDelDTO dto) {
        opcItemService.removeById(dto.getId());
        return CommomResponse.success("success");
    }

    /**
     * 修改dcs点位配置
     *
     * @return
     */
    @PostMapping("/itemUpdate")
    public CommomResponse itemUpdate(@RequestBody @Validated ItemUpdateDTO dto) {
        OPCItemEntity opcItemEntity = new OPCItemEntity();
        BeanUtils.copyProperties(dto, opcItemEntity);
        opcItemEntity.setAvgUpdateTime(LocalDateTime.now().plus(opcItemEntity.getAvgUpdateCycle(), ChronoUnit.SECONDS));
        opcItemService.updateById(opcItemEntity);
        return CommomResponse.success("success");
    }

}
