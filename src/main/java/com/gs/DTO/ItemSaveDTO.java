package com.gs.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/23 10:28
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemSaveDTO implements Serializable {

    @NotBlank(message = "factoryId不得为空")
    private String factoryId;

    @NotBlank(message = "itemId不得为空")
    private String itemId;

    /**
     * 2:short 3:long 4:float 8:字符
     */
    @NotBlank(message = "itemType不得为空")
    private Integer itemType;

    @NotBlank(message = "itemName不得为空")
    private String itemName;

    /**
     * 均值统计周期(秒) 默认1800
     */
    private Integer avgUpdateCycle = 1800;

}
