//package com.imooc.miaosha.validator;
//
//import com.alibaba.fastjson.JSON;
//import com.imooc.miaosha.vo.LoginVo;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.validation.annotation.Validated;
//
//import javax.validation.Valid;
//
///**
// * @author laowang
// * @date 2018/11/8 2:42 PM
// * @Description:
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class IsMobileTest {
//
//    @Test
//    public void setParams() {
//        LoginVo loginVo = new LoginVo();
////        loginVo.setMobile("13");
//        loginVo.setPassword("1");
//        testVolidator(loginVo);
//    }
//
//
//    public void testVolidator (@Validated LoginVo loginVo) {
//        String printStr = String.format("loginVo to json string : %s" , JSON.toJSONString(loginVo));
//        System.out.println(printStr);
//    }
//
//}
