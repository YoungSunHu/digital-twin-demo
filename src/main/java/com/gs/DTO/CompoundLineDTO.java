package com.gs.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/30 17:19
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class CompoundLineDTO implements Serializable {

    private List<String> twinPointIds;

    private List<String> twinPointAvgIds;

    private List<String> itemIds;

    private List<String> itemAvgIds;

    @NotBlank(message = "factoryId不得为空")
    private String factoryId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /**
     * 时间间隔 默认10分钟 前端传的是分钟间隔
     */
    private Integer pointStep = 10;
}
