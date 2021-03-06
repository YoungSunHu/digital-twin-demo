package com.gs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gs.DTO.SenderDataChemicalDTO;
import com.gs.DTO.SenderDataReceviceDTO;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.ChemicalExaminationEntity;
import com.gs.dao.entity.ChemicalExaminationRecordEntity;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.mapper.OPCItemValueRecordMapper;
import com.gs.exception.BussinessException;
import com.gs.service.*;
import lombok.extern.slf4j.Slf4j;
import okio.BufferedSink;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.Date;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/18 17:13
 * @modified By：
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class DataController {

    @Autowired
    OPCItemValueRecordMapper opcItemValueRecordMapper;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Autowired
    OPCItemService opcItemService;

    @Autowired
    TwinPointValueRecordService twinPointValueRecordService;

    @Autowired
    ChemicalExaminationService chemicalExaminationService;

    @Autowired
    ChemicalExaminationRecordService chemicalExaminationRecordService;


    @PostMapping("/itemUpload")
    public CommomResponse itemUpload(@RequestBody OPCItemValueRecordEntity entity) {
        log.info("接收OPCItem数据:{}", entity.toString());
        entity.setId(null);
        opcItemValueRecordMapper.insert(entity);
        opcItemValueRecordService.itemCache(entity);
        twinPointValueRecordService.updateDCSTwinPoint(entity);
        return CommomResponse.success("success");
    }

    /**
     * 发送器数据接收接口
     *
     * @param dto
     * @return
     */
    @PostMapping("/senderDataRecevice")
    public CommomResponse senderDataRecevice(@RequestBody SenderDataReceviceDTO dto) {
        //todo 发送器数据接收接口
        //校验opc点位和化验项配置
        int count = opcItemService.count(new QueryWrapper<OPCItemEntity>().eq("item_id", dto.getOpcItemId()).eq("factory_id", dto.getFactoryId()));
        if (count == 0) {
            throw new BussinessException("点位不存在");
        }
        //插入opc点位数据
        OPCItemValueRecordEntity opcItemValueRecordEntity = new OPCItemValueRecordEntity();
        opcItemValueRecordEntity.setItemTimestamp(Date.from(dto.getOpcItemTimestamp().atZone(ZoneOffset.ofHours(8)).toInstant()));
        opcItemValueRecordEntity.setItemValue(dto.getOpcItemValue());
        opcItemValueRecordEntity.setItemType(dto.getOpcItemType());
        opcItemValueRecordEntity.setFactoryId(dto.getFactoryId());
        opcItemValueRecordEntity.setIsSuccess(false);
        opcItemValueRecordService.save(opcItemValueRecordEntity);
        //化验数据核对
        if (dto.getDataChemicalDTOs() != null) {
            for (SenderDataChemicalDTO dataChemicalDTO : dto.getDataChemicalDTOs()) {
                ChemicalExaminationEntity one = chemicalExaminationService.getOne(new QueryWrapper<ChemicalExaminationEntity>().eq("exam_code", dataChemicalDTO.getExamCode()).eq("production_line_code", dataChemicalDTO.getProductionLineCode()).eq("factory_id", dto.getFactoryId()));
                if (one != null) {
                    ChemicalExaminationRecordEntity chemicalExaminationRecordEntity = new ChemicalExaminationRecordEntity();
                    BeanUtils.copyProperties(one, chemicalExaminationRecordEntity);
                    chemicalExaminationRecordEntity.setExamItemValue(dataChemicalDTO.getExamItemValue());
                    chemicalExaminationRecordService.save(chemicalExaminationRecordEntity);
                }
            }
        }
        return CommomResponse.success("success");
    }


}
