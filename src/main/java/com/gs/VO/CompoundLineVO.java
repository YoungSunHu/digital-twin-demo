package com.gs.VO;

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
    List<TwinPointLineVO> twinPointList;
    List<TwinPointAvgLineVO> twinPointAvgtList;
    List<ItemLineVO> itemList;
    List<ItemAvgLineVO> itemAvgList;
}
