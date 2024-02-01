package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author yifan
 * @date 2018/7/23 上午9:21
 */
@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price " +
            "from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id=#{good_id}")
    public GoodsVo getGoodsDetail(@Param("good_id") long goodsId);


    @Update("update miaosha_goods set stock_count =stock_count-1 where goods_id=#{goodsId} and stock_count > 0")
    boolean reduceStock(MiaoshaGoods g);

    @Select("select * from goods")
    public List<Goods> getAllGoodsInfo();

    @Insert("insert into goods(id,goods_name,goods_title,goods_img,goods_detail,goods_price,goods_stock) values(3,'oppo','oppo手机','www.baidu.com','888',99.8,66)")
    boolean insert();

    @Insert("insert into goods(id,goods_name,goods_title,goods_img,goods_detail,goods_price,goods_stock) values(4,'vivo','vivo手机','www.xixihaha.com','555',34.8,12)")
    boolean insert2();
}
