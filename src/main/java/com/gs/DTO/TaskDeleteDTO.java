package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/4 16:31
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class TaskDeleteDTO {
    @NotNull(message = "id不得为空")
    private Long id;
}
