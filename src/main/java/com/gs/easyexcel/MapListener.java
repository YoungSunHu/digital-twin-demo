package com.gs.easyexcel;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.gs.dao.entity.SenderDataDetailEntity;
import com.gs.dao.entity.SenderDataEntity;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 每解析一行会回调invoke()方法。
 * 整个excel解析结束会执行doAfterAllAnalysed()方法
 */

//有个很重要的点   不能被spring管理,要每次读取excel都要new。
//这边就会有一个问题：如果UserInfoDataListener中需要用到Spring中的主键怎么办？
public class MapListener extends AnalysisEventListener<Map<Integer, String>> {

    List<SenderDataDetailEntity> detailEntities = new ArrayList<>();

    private SenderDataEntity senderDataEntity = new SenderDataEntity();

    private static Pattern pattern = Pattern.compile("(^[\\-0-9][0-9]*(.[0-9]+)?)$");

    private static Long count = 0L;

    private Map<Integer, CellData> nameMap = null;

    private Map<Integer, String> IDMap = null;

    private Map<String, String> pointInfo = new HashMap<>();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:mm");

    @Override
    public void invoke(Map map, AnalysisContext context) {
        if (count == 0) {
            IDMap = map;
            IDMap.forEach(
                    (k, v) -> {
                        if ((Integer) k > 0) {
                            pointInfo.put((String) v, nameMap.get((Integer) k).getStringValue());
                        }
                    }
            );
            senderDataEntity.setPointInfo(pointInfo.toString());
        } else if (count > 0) {
            String opcTimestamp = (String) map.get(0);
            map.forEach(
                    (k, v) -> {
                        if ((Integer) k > 0) {
                            SenderDataDetailEntity senderDataDetailEntity = new SenderDataDetailEntity();
                            senderDataDetailEntity.setDataId(senderDataEntity.getId());
                            senderDataDetailEntity.setOpcItemId(IDMap.get(k));
                            senderDataDetailEntity.setOpcItemName(nameMap.get(k).getStringValue());
                            senderDataDetailEntity.setOpcItemType(isNumeric((String) v) ? 4 : 8);
                            senderDataDetailEntity.setOpcItemTimestamp(LocalDateTime.parse(opcTimestamp, formatter));
                            senderDataDetailEntity.setOpcItemValue((String) v);
                            detailEntities.add(senderDataDetailEntity);
                        }
                    }
            );
        }
        count++;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);
        nameMap = headMap;
    }

    public List<SenderDataDetailEntity> getDetailEntities() {
        return detailEntities;
    }

    public static boolean isNumeric(String str) {
        return pattern.matcher(str).matches();
    }

    public SenderDataEntity getSenderDataEntity() {
        return senderDataEntity;
    }

    public void defaultSet() {
        count = 0L;
        detailEntities.clear();
        pointInfo = new HashMap<>();
        IDMap = null;
        nameMap = null;
    }
}