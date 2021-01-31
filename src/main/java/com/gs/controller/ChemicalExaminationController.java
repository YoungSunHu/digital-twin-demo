package com.gs.controller;

import com.gs.DTO.HFSFCompoundFertilizerQualityDTO;
import com.gs.VO.CommomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/HFSF" + "/CompoundFertilizerQuality" + "/save")
    public CommomResponse CompoundFertilizer(@RequestBody HFSFCompoundFertilizerQualityDTO dto) {
        //配方换算
        return CommomResponse.data("success", null);
    }

}
