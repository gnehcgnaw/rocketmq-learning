package com.beatshadow.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.beatshadow.api.IGoodsService;
import com.beatshadow.constant.ShopCode;
import com.beatshadow.entity.Result;
import com.beatshadow.exception.CastException;
import com.beatshadow.shop.mapper.TradeGoodsMapper;
import com.beatshadow.shop.mapper.TradeGoodsNumberLogMapper;
import com.beatshadow.shop.pojo.TradeGoods;
import com.beatshadow.shop.pojo.TradeGoodsNumberLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 商品服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/3 13:21
 */
@Slf4j
@Component
@Service
public class GoodsServiceImpl implements IGoodsService {

    private TradeGoodsMapper tradeGoodsMapper ;

    private TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper ;

    public GoodsServiceImpl(TradeGoodsMapper tradeGoodsMapper, TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper) {
        this.tradeGoodsMapper = tradeGoodsMapper;
        this.tradeGoodsNumberLogMapper = tradeGoodsNumberLogMapper;
    }


    @Override
    public TradeGoods findOne(long goodsId) {
        return tradeGoodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    public Result reduceGoodsNum(TradeGoodsNumberLog tradeGoodsNumberLog) {
        if (tradeGoodsNumberLog == null ||
            tradeGoodsNumberLog.getGoodsNumber() == null ||
            tradeGoodsNumberLog.getOrderId() == null ||
            tradeGoodsNumberLog.getGoodsNumber() == null ||
            tradeGoodsNumberLog.getGoodsNumber().intValue() <= 0) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        TradeGoods tradeGoods = tradeGoodsMapper.selectByPrimaryKey(tradeGoodsNumberLog.getGoodsId());
        if(tradeGoods.getGoodsNumber()<tradeGoodsNumberLog.getGoodsNumber()){
            //库存不足
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        //减库存
        tradeGoods.setGoodsNumber(tradeGoods.getGoodsNumber()-tradeGoodsNumberLog.getGoodsNumber());
        tradeGoodsMapper.updateByPrimaryKey(tradeGoods);

        //记录库存操作日志
        tradeGoodsNumberLog.setGoodsNumber(-(tradeGoodsNumberLog.getGoodsNumber()));
        tradeGoodsNumberLog.setLogTime(new Date());
        tradeGoodsNumberLogMapper.insert(tradeGoodsNumberLog);

        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
    }
}
