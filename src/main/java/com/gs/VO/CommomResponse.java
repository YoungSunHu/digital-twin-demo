package com.gs.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 14:32
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommomResponse implements Serializable {

    private Integer code;

    private String msg;

    private Object data;

    public static CommomResponse success(String msg) {
        CommomResponse commomResponse = new CommomResponse(0, msg, null);
        return commomResponse;
    }

    public static CommomResponse data(String msg, Object o) {
        CommomResponse commomResponse = new CommomResponse(0, msg, o);
        return commomResponse;
    }

}
