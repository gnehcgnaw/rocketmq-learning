package com.beatshadow.api;

import com.beatshadow.entity.Result;
import com.beatshadow.shop.pojo.TradeOrder;

public interface IOrderService {

    /**
     * 下单接口
     * @param order
     * @return
     */
    public Result confirmOrder(TradeOrder order);

}
