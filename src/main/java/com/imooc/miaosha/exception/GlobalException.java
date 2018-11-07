package com.imooc.miaosha.exception;

import com.imooc.miaosha.Result.CodeMsg;

/**
 * @author yifan
 * @date 2018/7/21 下午3:24
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 695586319123279591L;

    private CodeMsg cm;

    public GlobalException (CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
