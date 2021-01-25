package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/24 9:04
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class DelTwinPointDTO {
    @NotBlank(message = "pointId不得为空")
    private String pointId;
    private Long id;
    @NotBlank(message = "factoryId不得为空")
    private String factoryId;
}
