package com.gs.VO;

import com.gs.dao.entity.OPCItemAvgEntity;
import com.gs.dao.entity.OPCItemEntity;
import com.gs.dao.entity.TwinPointAvgEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/30 17:22
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompoundLineVO {
    List twinPointList;
    List twinPointAvgtList;
    List itemList;
    List itemAvgList;
}
