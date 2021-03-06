package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/23 11:53
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TwinPointListDTO {

    @NotBlank(message = "factoryId不得为空")
    private String factoryId;

    private String productionLineId;

    private List<String> twinPointIds;
}
