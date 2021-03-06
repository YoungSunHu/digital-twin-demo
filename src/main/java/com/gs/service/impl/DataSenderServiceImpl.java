package com.gs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.SenderDataChemicalDTO;
import com.gs.DTO.SenderDataReceviceDTO;
import com.gs.VO.CommomResponse;
import com.gs.controller.DataController;
import com.gs.dao.entity.SenderDataDetailEntity;
import com.gs.dao.entity.SenderDataEntity;
import com.gs.dao.entity.SenderTaskEntity;
import com.gs.easyexcel.MapListener;
import com.gs.service.DataSenderService;
import com.gs.service.SenderDataDetailService;
import com.gs.service.SenderDataService;
import com.gs.service.SenderTaskService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/3 13:52
 * @modified By：
 */
@Slf4j
@Service
public class DataSenderServiceImpl implements DataSenderService {

    @Autowired
    SenderDataService senderDataService;

    @Autowired
    SenderDataDetailService senderDataDetailService;


    @Autowired
    DataController dataController;

    @Autowired
    SenderTaskService senderTaskService;

    @Value("${sender.uploadurl}")
    String uploadurl;

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    @Async
    @Override
    public void DataInbound(MultipartFile file) {
        ExcelReader excelReader = null;

        try {
            MapListener mapListener = new MapListener();
            mapListener.defaultSet();
            excelReader = EasyExcel.read(file.getInputStream(), mapListener).build();
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
            //保存数据记录
            SenderDataEntity senderDataEntity = mapListener.getSenderDataEntity();
            senderDataEntity.setChemicalInfo(senderDataEntity.getPointInfo());
            mapListener.setPointInfo(new HashMap<>());
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


            senderDataEntity.setDataName(file.getOriginalFilename().split("\\.")[0]);
            senderDataService.save(senderDataEntity);

            //清空点位记录
            mapListener.setPointInfo(new HashMap<>());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelReader != null) {
                excelReader.finish();
            }
        }
    }

    @Override
    @Async
    public void DataSend(List<SenderTaskEntity> tasks) {
        for (SenderTaskEntity task : tasks) {
            SenderDataEntity data = senderDataService.getOne(new QueryWrapper<SenderDataEntity>().eq("id", task.getDataId()));
            //根据数据指针获取data_detail
            Map map1 = JSON.parseObject(data.getPointInfo(), Map.class);
            List<SenderDataDetailEntity> list = senderDataDetailService.list(new QueryWrapper<SenderDataDetailEntity>().eq("data_id", task.getDataId()).eq("data_type", 1).in("opc_item_id", map1.keySet()).eq("opc_item_timestamp", task.getDataPointerTime()));


            for (SenderDataDetailEntity dataDetail : list) {
                //需要发送的DCS数据,化验数据,生成DTO发送
                SenderDataReceviceDTO senderDataReceviceDTO = new SenderDataReceviceDTO();
                senderDataReceviceDTO.setOpcItemId(dataDetail.getOpcItemId());
                senderDataReceviceDTO.setOpcItemType(dataDetail.getDataType());
                senderDataReceviceDTO.setOpcItemValue(dataDetail.getOpcItemValue());
                senderDataReceviceDTO.setFactoryId(task.getFactoryId());
                //发送时间 0:循环模式 1:一次发送 2:原始时间点发送
                if (task.getSendMode() == 0 || task.getSendMode() == 1) {
                    senderDataReceviceDTO.setOpcItemTimestamp(LocalDateTime.now());
                } else if (task.getSendMode() == 2) {
                    senderDataReceviceDTO.setOpcItemTimestamp(dataDetail.getOpcItemTimestamp());
                }


                //化验数据处理
                if (StringUtils.isNotBlank(task.getProductionLineCode())) {
                    Map<String, String> map = JSON.parseObject(data.getChemicalInfo(), Map.class);
                    Set<String> items = map.keySet();
                    List<SenderDataChemicalDTO> chemicalDTOS = new ArrayList<>();
                    for (String item : items) {
                        IPage<SenderDataDetailEntity> page = senderDataDetailService.page(new Page<>(1, 1), new QueryWrapper<SenderDataDetailEntity>().eq("opc_item_id", item).eq("data_id", task.getDataId()).eq("data_type", 2).gt("opc_item_timestamp", dataDetail.getOpcItemTimestamp()));
                        if (CollectionUtils.isEmpty(page.getRecords())) {
                            page = senderDataDetailService.page(new Page<>(1, 1), new QueryWrapper<SenderDataDetailEntity>().eq("opc_item_id", item).eq("data_id", task.getDataId()).eq("data_type", 2).lt("opc_item_timestamp", dataDetail.getOpcItemTimestamp()));
                        }
                        SenderDataChemicalDTO senderDataChemicalDTO = new SenderDataChemicalDTO();
                        senderDataChemicalDTO.setProductionLineCode(task.getProductionLineCode());
                        senderDataChemicalDTO.setExamCode(item);
                        senderDataChemicalDTO.setExamItemValue(page.getRecords().get(0).getOpcItemValue());
                        //发送时间 0:循环模式 1:一次发送 2:原始时间点发送
                        if (task.getSendMode() == 0 || task.getSendMode() == 1) {
                            senderDataChemicalDTO.setExamTime(LocalDateTime.now());
                        } else if (task.getSendMode() == 2) {
                            senderDataChemicalDTO.setExamTime(dataDetail.getOpcItemTimestamp());
                        }
                        chemicalDTOS.add(senderDataChemicalDTO);
                    }
                    senderDataReceviceDTO.setDataChemicalDTOs(chemicalDTOS);
                }

                //向数据中台发送数据(保留接口,这里直接调用)
                try {
                    boolean isSuccess = this.send(senderDataReceviceDTO);
                    if (isSuccess) {
                        //调整data_detail指针
                        //发送时间 0:循环模式 1:一次发送 2:原始时间点发送
                        if (task.getSendMode() == 0) {
                            //循环模式如果没有下一个,则就指向第一个
                            IPage<SenderDataDetailEntity> page = senderDataDetailService.page(new Page<>(1, 1), new QueryWrapper<SenderDataDetailEntity>().select("distinct opc_item_timestamp").eq("data_type", 1).in("opc_item_id", map1.keySet()).gt("opc_item_timestamp", task.getDataPointerTime()).orderByAsc("opc_item_timestamp"));
                            if (CollectionUtils.isEmpty(page.getRecords())) {
                                page = senderDataDetailService.page(new Page<>(1, 1), new QueryWrapper<SenderDataDetailEntity>().eq("data_type", 1).in("opc_item_id", map1.keySet()).orderByAsc("opc_item_timestamp"));
                            }
                            task.setDataPointerTime(page.getRecords().get(0).getOpcItemTimestamp());
                            task.setNextSendTime(LocalDateTime.now().plus(task.getSendCycle(), ChronoUnit.SECONDS));
                            senderTaskService.updateById(task);
                        } else if (task.getSendMode() == 1 || task.getSendMode() == 2) {
                            //一次发送或原始时间点发送
                            IPage<SenderDataDetailEntity> page = senderDataDetailService.page(new Page<>(1, 1), new QueryWrapper<SenderDataDetailEntity>().select("distinct opc_item_timestamp").eq("data_type", 1).in("opc_item_id", map1.keySet()).gt("opc_item_timestamp", task.getDataPointerTime()).orderByAsc("opc_item_timestamp"));
                            if (CollectionUtils.isEmpty(page.getRecords())) {
                                //为空判定已发送完成,任务更新已过期
                                task.setTaskStatus(2);
                                //数据指针时间初始化
                                page = senderDataDetailService.page(new Page<>(1, 1), new QueryWrapper<SenderDataDetailEntity>().select("distinct opc_item_timestamp").eq("data_type", 1).in("opc_item_id", map1.keySet()).gt("opc_item_timestamp", task.getDataPointerTime()).orderByAsc("opc_item_timestamp"));
                                task.setDataPointerTime(page.getRecords().get(0).getOpcItemTimestamp());
                            } else {
                                task.setDataPointerTime(page.getRecords().get(0).getOpcItemTimestamp());
                                task.setNextSendTime(LocalDateTime.now().plus(task.getSendCycle(), ChronoUnit.SECONDS));
                            }
                            senderTaskService.updateById(task);
                        }
                    } else {
                        task.setNextSendTime(LocalDateTime.now().plus(task.getSendCycle(), ChronoUnit.SECONDS));
                        senderTaskService.updateById(task);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    task.setNextSendTime(LocalDateTime.now().plus(task.getSendCycle(), ChronoUnit.SECONDS));
                    senderTaskService.updateById(task);
                }
            }


        }
    }

    public synchronized boolean send(SenderDataReceviceDTO senderDataReceviceDTO) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(senderDataReceviceDTO));
        Request request = new Request.Builder().url(uploadurl).post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            CommomResponse commomResponse = JSON.parseObject(responseBody, CommomResponse.class);
            if (commomResponse.getCode().equals(0)) {
                return true;
            } else {
                log.error("数据:{}上传失败,返回:{}", senderDataReceviceDTO.toString(), commomResponse.getMsg());
            }
        }
        return false;
    }

}
