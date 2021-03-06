package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/5 12:01
 * @modified By：
 */
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Data
public class SenderDataChemicalDTO {
    /**
     * 化验项编号
     */
    @NotBlank(message = "examCode不得为空")
    private String examCode;

    /**
     * 化验项数值
     */
    @NotBlank(message = "examItemValue不得为空")
    private String examItemValue;

    /**
     * 取样时间
     */
    @NotNull(message = "examTime不得为空")
    private LocalDateTime examTime;

    /**
     * 生产线编号
     */
    @NotBlank(message = "productionLineCode不得为空")
    private String productionLineCode;
}
