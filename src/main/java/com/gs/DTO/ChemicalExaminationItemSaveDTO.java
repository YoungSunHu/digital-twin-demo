package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/5 16:28
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ChemicalExaminationItemSaveDTO {
    @NotBlank(message = "factoryId不得为空")
    private String factoryId;
    @NotBlank(message = "productionLineId不得为空")
    private String productionLineId;
    @NotBlank(message = "productionLineCode不得为空")
    private String productionLineCode;
    @NotBlank(message = "examCode不得为空")
    private String examCode;
    @NotBlank(message = "examItemName不得为空")
    private String examItemName;
}
