package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/4 14:41
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class DataDeleteDTO {
    @NotNull(message = "id不得为空")
    private Long id;
}
