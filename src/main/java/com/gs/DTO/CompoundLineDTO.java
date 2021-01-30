package com.gs.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime endDate;

    /**
     * 取点数
     */
    private Integer pointStep = 10;
}
