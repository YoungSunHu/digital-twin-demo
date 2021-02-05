package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.HFSFCompoundFertilizerFormulaContentDTO;
import com.gs.DTO.HFSFCompoundFertilizerFormulaDTO;
import com.gs.DTO.HFSFCompoundFertilizerFormulaListDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.FormulaEntity;
import com.gs.dao.entity.ParamConfigEntity;
import com.gs.dao.mapper.ChemicalExaminationRecordMapper;
import com.gs.dao.mapper.ParamConfigMapper;
import com.gs.service.ChemicalExaminationRecordService;
import com.gs.service.FormulaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    ParamConfigMapper paramConfigMapper;

    @Autowired
    ChemicalExaminationRecordService chemicalExaminationRecordService;

    /**
     * 合肥四方集团公司复混肥配方通知书
     * 红四方工厂
     *
     * @param dto
     * @return
     */
    @PostMapping("/HFSF" + "/CompoundFertilizerFormula" + "/save")
    public CommomResponse HFSFCompoundFertilizerFormula(@RequestBody HFSFCompoundFertilizerFormulaDTO dto) {
        //读取模板
        ParamConfigEntity paramConfigEntity = paramConfigMapper.selectOne(new QueryWrapper<ParamConfigEntity>().eq("param_type", "HFSF_FORMULA_MODEL").eq("param_key", "15-15-15"));
        HFSFCompoundFertilizerFormulaDTO model = JSONObject.parseObject(paramConfigEntity.getParamValue(), HFSFCompoundFertilizerFormulaDTO.class);
        BeanUtils.copyProperties(dto, model);
        //配方换算
        HFSFCompoundFertilizerFormulaDTO hfsfCompoundFertilizerFormulaDTO = formulaService.HFSFCompoundFertilizerFormulaHandler(model);
        FormulaEntity formulaEntity = new FormulaEntity();
        formulaEntity.setFormulaJson(JSON.toJSONString(hfsfCompoundFertilizerFormulaDTO));
        formulaEntity.setFactoryId("AHHSF001");
        formulaEntity.setFormulaName("尿基复合肥(45%)");
        formulaEntity.setFormulaModel("15-15-15（含氯）");
        formulaEntity.setFormulaCode("15-15-15");
        //储存配方
        formulaService.save(formulaEntity);
        //提交配方化验数据
        chemicalExaminationRecordService.HFSFLine6(model);
        return CommomResponse.data("success", hfsfCompoundFertilizerFormulaDTO);
    }

    @PostMapping("/HFSF" + "/CompoundFertilizerFormula" + "/list")
    public CommomResponse HFSFCompoundFertilizerFormulaList(@RequestBody HFSFCompoundFertilizerFormulaListDTO dto) {
        IPage<FormulaEntity> page = formulaService.page(new Page<FormulaEntity>(dto.getPageNum(), dto.getPageSize()), new QueryWrapper<FormulaEntity>().eq("factory_id", "AHHSF001").eq("formula_code", "15-15-15"));
        page.getRecords().forEach(
                i -> {
                    i.setFormulaJson(null);
                }
        );
        return CommomResponse.data("success", page);
    }

    @PostMapping("/HFSF" + "/CompoundFertilizerFormula" + "/content")
    public CommomResponse HFSFCompoundFertilizerFormulaDetail(@RequestBody HFSFCompoundFertilizerFormulaContentDTO dto) {
        FormulaEntity entity = formulaService.getOne(new QueryWrapper<FormulaEntity>().eq("id", dto.getFormulaId()));
        HFSFCompoundFertilizerFormulaDTO model = JSONObject.parseObject(entity.getFormulaJson(), HFSFCompoundFertilizerFormulaDTO.class);
        return CommomResponse.data("success", model);
    }

    /**
     * 读取配方模板
     *
     * @return
     */
    @GetMapping("/HFSF" + "/CompoundFertilizerFormula" + "/model")
    public CommomResponse HFSFCompoundFertilizerFormulaModel() {
        //读取模板
        ParamConfigEntity paramConfigEntity = paramConfigMapper.selectOne(new QueryWrapper<ParamConfigEntity>().eq("param_type", "HFSF_FORMULA_MODEL").eq("param_key", "15-15-15"));
        HFSFCompoundFertilizerFormulaDTO model = JSONObject.parseObject(paramConfigEntity.getParamValue(), HFSFCompoundFertilizerFormulaDTO.class);
        String s = JSON.toJSONString(model);
        return CommomResponse.data("success", model);
    }

}
