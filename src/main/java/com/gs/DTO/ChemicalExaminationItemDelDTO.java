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
public class ChemicalExaminationItemDelDTO {
    @NotBlank(message = "id不得为空")
    private Long id;
}
