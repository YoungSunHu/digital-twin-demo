package com.gs.exception;

import lombok.Data;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/1 13:58
 * @modified By：
 */
@Data
public class BussinessException extends RuntimeException {

    Integer code;

    String msg;

    public BussinessException(String msg) {
        this.code = 200;
        this.msg = msg;
    }

}
