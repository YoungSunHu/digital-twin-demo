package com.gs.controller;

import com.gs.DTO.CalculateScriptTestDTO;
import com.gs.DTO.SaveTwinPointDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.service.TwinPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * dcs点位列表
     *
     * @return
     */
    @GetMapping("/DCSPointList")
    public CommomResponse getDCSPointList() {
        return null;
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
        return CommomResponse.success("保 存成功");
    }

    @PostMapping("/calculateScriptTest")
    public CommomResponse calculateScriptTest(@RequestBody CalculateScriptTestDTO calculateScriptTestDTO) {
        return null;
    }


}
