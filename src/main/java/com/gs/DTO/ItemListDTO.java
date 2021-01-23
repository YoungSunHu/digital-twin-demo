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
public class ItemListDTO implements Serializable {
    @NotBlank(message = "factoryId不得为空")
    private String factoryId;
}
