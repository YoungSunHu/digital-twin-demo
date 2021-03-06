package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/4 17:16
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class SenderDataReceviceDTO {

    /**
     * 工厂ID
     */
    @NotBlank(message = "factoryId不得为空")
    private String factoryId;

    /**
     * opc点位id
     */
    @NotBlank(message = "opcItemId不得为空")
    private String opcItemId;
    /**
     * opc点位id
     */
    @NotBlank(message = "opcItemValue不得为空")
    private String opcItemValue;


    /**
     * opc点位数据类型
     */
    @NotNull(message = "opcItemType不得为空")
    private Integer opcItemType;

    /**
     * opc点位时间戳
     */
    @NotNull(message = "opcItemTimestamp不得为空")
    private LocalDateTime opcItemTimestamp;


    private List<SenderDataChemicalDTO> dataChemicalDTOs;

}
