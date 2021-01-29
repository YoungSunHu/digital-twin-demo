package com.gs.controller;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.FormulaEntity;
import com.gs.service.FormulaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/27 14:39
 * @modified By：
 * 配方相关接口
 */
@RestController
@RequestMapping("/formula")
@Slf4j
public class FormulaController {

    @Autowired
    FormulaService formulaService;

    /**
     * 合肥四方集团公司复混肥配方通知书
     * 红四方工厂
     *
     * @param dto
     * @return
     */
    @PostMapping("/HFSF" + "/CompoundFertilizerFormula" + "/Save")
    public CommomResponse HFSFCompoundFertilizerFormula(@RequestBody HFSFCompoundFertilizerFormulaDTO dto) {
        //配方换算
        HFSFCompoundFertilizerFormulaDTO hfsfCompoundFertilizerFormulaDTO = formulaService.HFSFCompoundFertilizerFormulaHandler(dto);
        FormulaEntity formulaEntity = new FormulaEntity();
        formulaEntity.setFormulaJson(JSON.toJSONString(hfsfCompoundFertilizerFormulaDTO));
        formulaEntity.setFactoryId("AHHSF001");
        formulaEntity.setFormulaName("尿基复合肥(45%)");
        formulaEntity.setFormulaModel("15-15-15（含氯）");
        formulaEntity.setFormulaCode("15-15-15");
        formulaEntity.setId(1L);
        //储存配方
        formulaService.saveOrUpdate(formulaEntity);
        return CommomResponse.data("success", hfsfCompoundFertilizerFormulaDTO);
    }

    @GetMapping("/HFSF" + "/CompoundFertilizerFormula")
    public CommomResponse HFSFCompoundFertilizerFormula() {
        FormulaEntity formulaEntity = formulaService.getOne(new QueryWrapper<FormulaEntity>().eq("factory_id", "AHHSF001").eq("formula_code", "15-15-15"));
        String formulaJson = formulaEntity.getFormulaJson();
        HFSFCompoundFertilizerFormulaDTO dto = JSON.parseObject(formulaJson, HFSFCompoundFertilizerFormulaDTO.class);
        return CommomResponse.data("success", dto);
    }

}
