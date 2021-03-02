package com.gs.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/3 17:47
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HFSFCompoundFertilizerFormulaListDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
