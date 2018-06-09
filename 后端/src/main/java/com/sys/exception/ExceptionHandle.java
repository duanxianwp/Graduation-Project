package com.sys.exception;

import com.sys.utils.Result;
import com.sys.utils.ResultUtils;
import com.sys.utils.StatusEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sys
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {

        if (e instanceof AppException) {

            AppException exception = (AppException) e;
            return ResultUtils.error(exception.getCode(), exception.getMessage());
        } else {
            return ResultUtils.error(StatusEnum.UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
    }
}
