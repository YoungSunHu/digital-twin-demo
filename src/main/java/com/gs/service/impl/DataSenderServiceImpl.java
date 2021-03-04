package com.gs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.gs.dao.entity.SenderDataDetailEntity;
import com.gs.dao.entity.SenderDataEntity;
import com.gs.easyexcel.MapListener;
import com.gs.service.DataSenderService;
import com.gs.service.SenderDataDetailService;
import com.gs.service.SenderDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/3 13:52
 * @modified By：
 */
@Service
public class DataSenderServiceImpl implements DataSenderService {

    @Autowired
    SenderDataService senderDataService;

    @Autowired
    SenderDataDetailService senderDataDetailService;

    @Override
    @Async
    public void DataInbound(InputStream inputStream) {
        ExcelReader excelReader = null;
        try {
            MapListener mapListener = new MapListener();
            mapListener.defaultSet();
            excelReader = EasyExcel.read(inputStream, mapListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            ReadSheet readSheet1 = EasyExcel.readSheet(1).build();
            ReadSheet readSheet2 = EasyExcel.readSheet(2).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能

            //化验数据
            excelReader.read(readSheet);
            List<SenderDataDetailEntity> detailEntities = mapListener.getDetailEntities();
            detailEntities.forEach(
                    i -> {
                        i.setDataType(2);
                    }
            );
            //保存化验数据
            senderDataDetailService.saveBatch(detailEntities);
            mapListener.defaultSet();


            //化验数据2
            //保存化验数据2
            excelReader.read(readSheet1);
            List<SenderDataDetailEntity> detailEntities2 = mapListener.getDetailEntities();
            detailEntities2.forEach(
                    i -> {
                        i.setDataType(2);
                    }
            );
            //保存化验数据
            senderDataDetailService.saveBatch(detailEntities2);
            mapListener.defaultSet();


            //DCS数据
            //保存DCS数据
            excelReader.read(readSheet2);
            List<SenderDataDetailEntity> detailEntities3 = mapListener.getDetailEntities();
            detailEntities3.forEach(
                    i -> {
                        i.setDataType(1);
                    }
            );
            //保存化验数据
            senderDataDetailService.saveBatch(detailEntities3);
            mapListener.defaultSet();

            //保存数据记录
            SenderDataEntity senderDataEntity = mapListener.getSenderDataEntity();
            senderDataService.save(senderDataEntity);

        } finally {
            if (excelReader != null) {
                excelReader.finish();
            }
        }
    }
}
