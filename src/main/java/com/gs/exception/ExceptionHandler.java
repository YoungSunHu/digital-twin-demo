package com.gs.exception;

import com.gs.VO.CommomResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/2/1 13:55
 * @modified By：
 */
@ControllerAdvice
@RestController
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BussinessException.class)
    public CommomResponse bussinessExceptionHandler(BussinessException e) {
        CommomResponse commomResponse = new CommomResponse(e.getCode(), e.getMsg(), null);
        return commomResponse;
    }

}
