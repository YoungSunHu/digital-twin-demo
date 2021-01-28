package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 17:33
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CalculateScriptTestDTO implements Serializable {
    /**
     * 运算脚本
     */
    @NotBlank(message = "calculateScript不得为空")
    private String calculateScript;

    @NotBlank(message = "factoryId不得为空")
    private String factoryId;

    @NotBlank(message = "productionLineId不得为空")
    private String productionLineId;

}
