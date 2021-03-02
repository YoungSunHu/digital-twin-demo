package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.*;
import com.gs.VO.CommomResponse;
import com.gs.VO.HFSFCompoundFertilizerQualityStaticVO;
import com.gs.dao.entity.ChemicalExaminationEntity;
import com.gs.dao.entity.HFSFCompoundFertilizerQualityEntity;
import com.gs.service.ChemicalExaminationRecordService;
import com.gs.service.ChemicalExaminationService;
import com.gs.service.HFSFCompoundFertilizerQualityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/27 14:37
 * @modified By：
 * 化验结果相关接口
 */
@RestController
@RequestMapping("/chemicalExamination")
@Slf4j
public class ChemicalExaminationController {

    @Autowired
    HFSFCompoundFertilizerQualityService hfsfCompoundFertilizerQualityService;

    @Autowired
    ChemicalExaminationRecordService chemicalExaminationRecordService;

    @Autowired
    ChemicalExaminationService chemicalExaminationService;

    /**
     * 化验项列表接口
     *
     * @param chemicalExaminationItemListDTO
     * @return
     */
    @PostMapping(value = "/itemList", produces = "application/json;charset=UTF-8")
    public CommomResponse chemicalExaminationItemList(@RequestBody ChemicalExaminationItemListDTO chemicalExaminationItemListDTO) {
        QueryWrapper<ChemicalExaminationEntity> queryWrapper = new QueryWrapper<ChemicalExaminationEntity>()
                .eq("factory_id", chemicalExaminationItemListDTO.getFactoryId())
                .eq("production_line_id", chemicalExaminationItemListDTO.getProductionLineId());
        List<ChemicalExaminationEntity> list = chemicalExaminationService.list(queryWrapper);
        return CommomResponse.data("success", list);
    }

    /**
     * 四方集团复合肥产品质量统计报表登记接口
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/HFSF" + "/CompoundFertilizerQuality" + "/save", produces = "application/json;charset=UTF-8")
    public CommomResponse CompoundFertilizerQualitySave(@RequestBody HFSFCompoundFertilizerQualityDTO dto) {
        HFSFCompoundFertilizerQualityEntity hfsfCompoundFertilizerQualityEntity = new HFSFCompoundFertilizerQualityEntity();
        BeanUtils.copyProperties(dto, hfsfCompoundFertilizerQualityEntity);
        String s = JSON.toJSONString(dto);
        System.out.println(s);
        //统计班产,高浓度,中浓度,低浓度
        //班产
        hfsfCompoundFertilizerQualityEntity.setShiftYeild(
                hfsfCompoundFertilizerQualityEntity.getAFirstClass() +
                        hfsfCompoundFertilizerQualityEntity.getAOverNutrition() +
                        hfsfCompoundFertilizerQualityEntity.getAOverWater() +
                        hfsfCompoundFertilizerQualityEntity.getAOwnUsePackage() +
                        hfsfCompoundFertilizerQualityEntity.getAUnqualified() +
                        hfsfCompoundFertilizerQualityEntity.getBFirstClass() +
                        hfsfCompoundFertilizerQualityEntity.getBOverNutrition() +
                        hfsfCompoundFertilizerQualityEntity.getBOverWater() +
                        hfsfCompoundFertilizerQualityEntity.getBOwnUsePackage() +
                        hfsfCompoundFertilizerQualityEntity.getBUnqualified() +
                        hfsfCompoundFertilizerQualityEntity.getCFirstClass() +
                        hfsfCompoundFertilizerQualityEntity.getCOverNutrition() +
                        hfsfCompoundFertilizerQualityEntity.getCOverWater() +
                        hfsfCompoundFertilizerQualityEntity.getCOwnUsePackage() +
                        hfsfCompoundFertilizerQualityEntity.getCUnqualified() +
                        hfsfCompoundFertilizerQualityEntity.getDFirstClass() +
                        hfsfCompoundFertilizerQualityEntity.getDOverNutrition() +
                        hfsfCompoundFertilizerQualityEntity.getDOverWater() +
                        hfsfCompoundFertilizerQualityEntity.getDOwnUsePackage() +
                        hfsfCompoundFertilizerQualityEntity.getDUnqualified()
        );
        //总养分
        hfsfCompoundFertilizerQualityEntity.setTotalNutrition(hfsfCompoundFertilizerQualityEntity.getN() + hfsfCompoundFertilizerQualityEntity.getP() + hfsfCompoundFertilizerQualityEntity.getK());
        //高浓度
        hfsfCompoundFertilizerQualityEntity.setHighConcentration(hfsfCompoundFertilizerQualityEntity.getTotalNutrition() > 40 ? hfsfCompoundFertilizerQualityEntity.getShiftYeild() : 0f);
        //低浓度
        hfsfCompoundFertilizerQualityEntity.setLowConcentration((hfsfCompoundFertilizerQualityEntity.getTotalNutrition() > 0 && hfsfCompoundFertilizerQualityEntity.getTotalNutrition() < 30) ? hfsfCompoundFertilizerQualityEntity.getShiftYeild() : 0f);
        //中浓度
        hfsfCompoundFertilizerQualityEntity.setLowConcentration((hfsfCompoundFertilizerQualityEntity.getTotalNutrition() > 30 && hfsfCompoundFertilizerQualityEntity.getTotalNutrition() < 40) ? hfsfCompoundFertilizerQualityEntity.getShiftYeild() : 0f);

        //hfsfCompoundFertilizerQualityService.remove(new QueryWrapper<HFSFCompoundFertilizerQualityEntity>().eq("date", hfsfCompoundFertilizerQualityEntity.getDate()));
        hfsfCompoundFertilizerQualityService.save(hfsfCompoundFertilizerQualityEntity);

        //化验数据登记
        chemicalExaminationRecordService.HFSFLine6(hfsfCompoundFertilizerQualityEntity);
        return CommomResponse.success("success");
    }

    @PostMapping(value = "/HFSF" + "/CompoundFertilizerQuality" + "/list", produces = "application/json;charset=UTF-8")
    public CommomResponse CompoundFertilizerQualityList(@RequestBody CompoundFertilizerQualityListDTO dto) {
        QueryWrapper<HFSFCompoundFertilizerQualityEntity> wrapper = new QueryWrapper<HFSFCompoundFertilizerQualityEntity>()
                .between("date", dto.getStartDate(), dto.getEndDate())
                .eq("del_flag", 0)
                .orderBy(true, false, "date");
        IPage<HFSFCompoundFertilizerQualityEntity> page = hfsfCompoundFertilizerQualityService.page(new Page<HFSFCompoundFertilizerQualityEntity>(dto.getPageNum(), dto.getPageSize()), wrapper);
        return CommomResponse.data("success", page);
    }

    @PostMapping(value = "/HFSF" + "/CompoundFertilizerQuality" + "/delete", produces = "application/json;charset=UTF-8")
    public CommomResponse CompoundFertilizerQualityDel(@RequestBody CompoundFertilizerQualityDelDTO dto) {
        hfsfCompoundFertilizerQualityService.update(new UpdateWrapper<HFSFCompoundFertilizerQualityEntity>().set("del_flag", 1).eq("id", dto.getId()));
        return CommomResponse.success("success");
    }

    /**
     * 四方集团复合肥产品质量统计报表统计接口
     *
     * @param dto
     * @return
     */
    @PostMapping("/HFSF" + "/CompoundFertilizerQuality" + "/static")
    public CommomResponse CompoundFertilizerQualityStatic(@RequestBody @Validated CompoundFertilizerQualityStaticDTO dto) {
        HFSFCompoundFertilizerQualityStaticVO vo = hfsfCompoundFertilizerQualityService.hFSFCompoundFertilizerQualityStatic(dto.getStartDate(), dto.getEndDate());
        return CommomResponse.data("success", vo);
    }

}
