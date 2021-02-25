package com.gs.VO;

import com.gs.dao.entity.TwinPointAvgEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/31 10:38
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwinPointAvgLineVO {
    String twinPointId;
    List<TwinPointAvgEntity> twinPointAvgList;
}
