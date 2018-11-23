package com.imooc.miaosha.exception;

import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.cotroller.GoodsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yifan
 * @date 2018/7/20 下午3:42
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e) {

        if (e instanceof GlobalException) {
            GlobalException ge = (GlobalException)e;
            return Result.error(ge.getCm());

        } else if (e instanceof BindException) {
            BindException ex = (BindException)e;
            List<ObjectError> errors =  ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            logger.error("e:",ex);
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {
            logger.error(CodeMsg.SERVER_ERROR.getMsg() + ":",e);
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
