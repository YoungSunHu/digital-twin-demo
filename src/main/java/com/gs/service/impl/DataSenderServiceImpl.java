package com.gs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.gs.easyexcel.MapListener;
import com.gs.service.DataSenderService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/3 13:52
 * @modified By：
 */
@Service
public class DataSenderServiceImpl implements DataSenderService {

    @Override
    public void DataInbound(InputStream inputStream) {
        MapListener mapListener = new MapListener();
        ExcelReader excelReader = EasyExcel.read(inputStream, mapListener).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        ReadSheet readSheet1 = EasyExcel.readSheet(1).build();
        excelReader.read(readSheet1);
        ReadSheet readSheet2 = EasyExcel.readSheet(2).build();
        excelReader.read(readSheet2);
    }
}
