package com.beatshadow.api;

import com.beatshadow.entity.Result;
import com.beatshadow.shop.pojo.TradeGoods;
import com.beatshadow.shop.pojo.TradeGoodsNumberLog;

/**
 * 商品服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/3 13:18
 */
public interface IGoodsService {
    /**
     * 根据goodsId查询商品
     * @param goodsId
     * @return
     */
    TradeGoods findOne(long goodsId);

    /**
     * 扣减库存数量
     * @param tradeGoodsNumberLog
     * @return
     */
    Result reduceGoodsNum(TradeGoodsNumberLog tradeGoodsNumberLog);
}
