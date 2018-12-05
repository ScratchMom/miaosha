package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yifan
 * @date 2018/7/16 下午4:47
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    @RequestMapping("/mq/topic")
    @ResponseBody
    Result<String> topic_exchange() {
        sender.sendTopic("topic exchange test");
        return Result.success("Hi Lao wang!");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    Result<String> fanout() {
        sender.sendFanout("fanout exchange test");
        return Result.success("Hi Lao wang!");
    }

    @RequestMapping("/mq/headers")
    @ResponseBody
    Result<String> headers() {
        sender.sendHeaders("headers exchange test");
        return Result.success("Hi Lao wang!");
    }
    @RequestMapping("/redis/set")
    @ResponseBody
    Result redisSet() {
        User user = new User();
        user.setName("person88");
        user.setId(919);
        boolean flag = redisService.set(UserKey.getByName,"1",user);
        System.out.println("flag === " + flag);
        User str = redisService.get(UserKey.getByName,"1",User.class);
        return  Result.success(str);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        System.out.println("999");
        model.addAttribute("name","laowang");
        return "hello";

    }
    @RequestMapping("/hello")
    @ResponseBody
    Result<String> hello() {
        return  Result.success("hello world");
    }

    @RequestMapping("/db/get")
    @ResponseBody
    Result<User> dbget() {
        User user = userService.getById(1);
        return  Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    Result<String> dbtx() {
        userService.tx();
        return  Result.success("aa");
    }


}
