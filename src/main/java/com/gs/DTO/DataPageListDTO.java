package com.gs.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/3/4 14:32
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class DataPageListDTO {
    @NotNull(message = "startDate不得为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "endDate不得为空")
    private LocalDateTime endDate;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
