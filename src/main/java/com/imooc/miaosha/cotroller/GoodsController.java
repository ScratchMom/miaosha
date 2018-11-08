package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yifan
 * @date 2018/7/21 下午8:33
 */
@Controller
@RequestMapping("/api/goods")
public class GoodsController {

    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);


    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(Model model, MiaoshaUser user, HttpServletRequest request,HttpServletResponse response){
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsVos);
//        return "goods_list";

        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        // 手动渲染
        SpringWebContext context
                = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap(),applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }

//    @RequestMapping("/to_list")
//    public String list(Model model, MiaoshaUser user){
//        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
//        model.addAttribute("goodsList",goodsVos);
//        return "goods_list";
//    }

    @RequestMapping("/to_detail/{goodsId}")
    public String list(Model model, MiaoshaUser user,
                       @PathVariable("goodsId") long goodsId){

        logger.info("iii");
        // snowflake 算法主键自增
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.getGoodsDetail(goodsId);
        model.addAttribute("goods",goodsVo);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long nowAt = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (nowAt < startAt) {  // 秒杀没开始
            remainSeconds = (int)(nowAt - nowAt)/1000;
        } else if (nowAt > endAt) { // 秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {    // 秒杀正在进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);

        return "goods_detail";
    }
}































