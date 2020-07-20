package com.beatshadow.api;

import com.beatshadow.entity.Result;
import com.beatshadow.shop.pojo.TradeOrder;

/**
 * 订单服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/2 19:12
 */

public interface IOrderService {
    /**
     * 确认订单
     * @param tradeOrder
     * @return
     */
    Result confirmOrder(TradeOrder tradeOrder);
}
