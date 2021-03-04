package com.gs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.*;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.SenderDataDetailEntity;
import com.gs.dao.entity.SenderDataEntity;
import com.gs.dao.entity.SenderTaskEntity;
import com.gs.exception.BussinessException;
import com.gs.service.DataSenderService;
import com.gs.service.SenderDataDetailService;
import com.gs.service.SenderDataService;
import com.gs.service.SenderTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/2 15:07
 * @modified By：
 * 数据发送器相关接口
 */
@RestController
@RequestMapping("/sender")
@Slf4j
public class DataSenderController {

    @Autowired
    DataSenderService dataSenderService;

    @Autowired
    SenderDataService senderDataService;

    @Autowired
    SenderDataDetailService senderDataDetailService;

    @Autowired
    SenderTaskService senderTaskService;

    @PostMapping(value = "/dataUpload")
    public CommomResponse dataUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String[] split = file.getOriginalFilename().split("\\.");
        if (!split[1].equals("xlsx")) {
            throw new BussinessException("导入文件错误!");
        }
        dataSenderService.DataInbound(file.getInputStream());
        return CommomResponse.success("数据导入中");
    }

    @PostMapping(value = "/data/pageList")
    public CommomResponse dataPageList(@RequestBody DataPageListDTO dto) {
        QueryWrapper<SenderDataEntity> querywrap = new QueryWrapper<SenderDataEntity>().between("create_time", dto.getStartDate(), dto.getEndDate()).orderBy(true, false, "create_time");
        IPage<SenderDataEntity> page = senderDataService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), querywrap);
        return CommomResponse.data("success", page);
    }

    @PostMapping(value = "/data/delete")
    public CommomResponse dataDelete(@RequestBody @Validated DataDeleteDTO dto) {
        int count = senderTaskService.count(new QueryWrapper<SenderTaskEntity>().eq("data_id", dto.getId()));
        if (count > 0) {
            throw new BussinessException("数据仍有绑定关联任务!");
        }
        senderDataService.remove(new QueryWrapper<SenderDataEntity>().eq("id", dto.getId()));
        senderDataDetailService.remove(new QueryWrapper<SenderDataDetailEntity>().eq("data_id", dto.getId()));
        return CommomResponse.success("success");
    }

    @PostMapping(value = "/data/detail")
    public CommomResponse dataDCS(@RequestBody @Validated DataDTO dto) {
        IPage<SenderDataDetailEntity> page = senderDataDetailService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), new QueryWrapper<SenderDataDetailEntity>().eq("data_type", dto.getDataType()).eq("data_id", dto.getId()).orderBy(true, true, "opc_item_timestamp"));
        return CommomResponse.data("success", page);
    }


    @PostMapping(value = "/task/save")
    public CommomResponse taskSave(@RequestBody @Validated TaskSaveDTO dto) {
        SenderTaskEntity senderTaskEntity = new SenderTaskEntity();
        BeanUtils.copyProperties(dto, senderTaskEntity);
        senderTaskService.save(senderTaskEntity);
        return CommomResponse.success("success");
    }

    @PostMapping(value = "/task/pageList")
    public CommomResponse taskPageList(@RequestBody @Validated TaskPageListDTO dto) {
        QueryWrapper<SenderTaskEntity> queryWrapper = new QueryWrapper<SenderTaskEntity>().between("create_time", dto.getStartDate(), dto.getEndDate()).orderByDesc("create_time");
        IPage<SenderTaskEntity> page = senderTaskService.page(new Page<>(dto.getPageNum(), dto.getPageSize()), queryWrapper);
        return CommomResponse.data("success", page);
    }

    @PostMapping(value = "/task/update")
    public CommomResponse taskUpdate(@RequestBody @Validated TaskUpdateDTO dto) {
        QueryWrapper<SenderTaskEntity> queryWrapper = new QueryWrapper<SenderTaskEntity>().eq("id", dto.getId());
        SenderTaskEntity one = senderTaskService.getOne(queryWrapper);
        SenderTaskEntity senderTaskEntity = new SenderTaskEntity();
        BeanUtils.copyProperties(dto, senderTaskEntity);
        senderTaskEntity.setId(one.getId());
        senderTaskService.updateById(senderTaskEntity);
        return CommomResponse.success("success");
    }

    @PostMapping(value = "/task/delete")
    public CommomResponse taskDelete(@RequestBody @Validated TaskDeleteDTO dto) {
        senderTaskService.remove(new QueryWrapper<SenderTaskEntity>().eq("id", dto.getId()));
        return CommomResponse.success("success");
    }
}
