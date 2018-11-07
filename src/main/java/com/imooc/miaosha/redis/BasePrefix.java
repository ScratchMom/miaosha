package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/19 下午3:15
 */
public abstract class BasePrefix implements KeyPrefix {

//    抽象类，需要有成员变量存储
    private int expireSeconds;
    private String prefix;

    public BasePrefix (String prefix){
//        this.expireSeconds = -1;
////        this.prefix = prefix;
        this(-1,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {    // 默认-1代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();  // getClass() 返回Object 的运行时类
        return className + ":" + prefix;
    }
}
