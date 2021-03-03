package com.gs.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.gs.VO.CommomResponse;
import com.gs.dao.entity.SenderDataDetailEntity;
import com.gs.easyexcel.MapListener;
import com.gs.exception.BussinessException;
import com.gs.service.DataSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    @PostMapping(value = "dataUpload")
    public CommomResponse dataUpload(@RequestParam(value = "file") MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        if (!split[1].equals("xlsx")) {
            throw new BussinessException("导入文件错误!");
        }
        try {
            InputStream inputStream = file.getInputStream();
            MapListener mapListener = new MapListener();
            ExcelReader excelReader = EasyExcel.read(inputStream, mapListener).build();
            //化验表1
            ReadSheet readSheet1 = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet1);
            //化验表2
            ReadSheet readSheet2 = EasyExcel.readSheet(1).build();
            excelReader.read(readSheet2);
            //DCS数据
            ReadSheet readSheet3 = EasyExcel.readSheet(3).build();
            excelReader.read(readSheet3);
            List<SenderDataDetailEntity> detailEntities = mapListener.getDetailEntities();
            System.out.println(detailEntities);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
