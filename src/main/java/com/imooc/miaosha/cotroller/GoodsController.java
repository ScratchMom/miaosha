package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.KeyPrefix;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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


    /**
     * 返回商品列表页面
     *
     * Spring MVC 在内部使用了一个org.springframework.ui.Model 接口存储模型数据。
     * Spring MVC 在调用方法前会创建一个–隐含的模型对象作为模型数据的存储容器。
     * 如果方法的入参为 Map 或 Model 类型，Spring MVC 会隐含模型的引用传递给这些入参。
     * 在方法体内，开发者可以通过这个入参对象访问到模型中的所有数据，也可以向模型中添加新的属性数据。
     *
     * QPS优化后  1334/s(特定的查询服务器在规定时间内所处理流量多少的衡量标准)
     * @param model
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(Model model, MiaoshaUser user, HttpServletRequest request, HttpServletResponse response) {

        model.addAttribute("user",user);
        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        html = htmlHandle("goods_list", request, response, model, GoodsKey.getGoodsList, "");
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model,
                                        MiaoshaUser user,
                                        @PathVariable("goodsId") long goodsId,
                                        HttpServletRequest request, HttpServletResponse response) {

        GoodsVo goodsVo = goodsService.getGoodsDetail(goodsId);
        // snowflake 算法主键自增
        model.addAttribute("goods", goodsVo);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long nowAt = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (nowAt < startAt) {  // 秒杀没开始
            miaoshaStatus = 0;
            remainSeconds = (int) (startAt - nowAt) / 1000;
        } else if (nowAt > endAt) { // 秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {    // 秒杀正在进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setUser(user);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(goodsDetailVo);
    }



    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(Model model,
                       MiaoshaUser user,
                       @PathVariable("goodsId") long goodsId,
                       HttpServletRequest request, HttpServletResponse response) {

        // snowflake 算法主键自增
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.getGoodsDetail(goodsId);
        model.addAttribute("goods", goodsVo);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long nowAt = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (nowAt < startAt) {  // 秒杀没开始
            remainSeconds = (int) (nowAt - nowAt) / 1000;
        } else if (nowAt > endAt) { // 秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {    // 秒杀正在进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        // 取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        html = htmlHandle("goods_detail", request, response, model, GoodsKey.getGoodsDetail, String.valueOf(goodsId));
        return html;
    }

    /**
     * 手动渲染模板
     *
     * @param thymeleafTemp
     * @return
     */
    public String htmlHandle(String thymeleafTemp,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             Model model,
                             KeyPrefix keyPrefix,
                             String key) {
        String html = "";
        // 手动渲染
        SpringWebContext context
                = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process(thymeleafTemp, context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(keyPrefix, "" + key, html);
        }
        return html;
    }
}































