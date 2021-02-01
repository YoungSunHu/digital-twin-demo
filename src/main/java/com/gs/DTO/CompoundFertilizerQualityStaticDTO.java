package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/1 10:21
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CompoundFertilizerQualityStaticDTO implements Serializable {
    @NotNull(message = "startDate不得为空")
    private LocalDate startDate;
    @NotNull(message = "endDate不得为空")
    private LocalDate endDate;

}
