package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/22 14:26
 * @modified By：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ItemStatusDTO implements Serializable {

    /**
     * 工厂ID 必填
     */
    private String factoryId;

    /**
     * 工厂点位ID
     */
    private List<String> itemIds;
}
