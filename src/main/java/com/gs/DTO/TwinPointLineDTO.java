package com.gs.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/29 9:28
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class TwinPointLineDTO implements Serializable {
    private String twinPointId;
    private String twinPointCode;
    private String factoryId;
    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime endDate;
    /**
     * 点位间隔,颗粒度
     */
    private Integer pointStep = 10;
}
