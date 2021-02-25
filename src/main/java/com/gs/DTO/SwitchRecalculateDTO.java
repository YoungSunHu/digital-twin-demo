package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/19 9:02
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SwitchRecalculateDTO {
    @NotNull(message = "twinPointIds不得为空")
    private List<String> twinPointIds;
    @NotBlank(message = "factoryId不得为空")
    private String factoryId;
    @NotBlank(message = "productionLineId不得为空")
    private String productionLineId;
}
