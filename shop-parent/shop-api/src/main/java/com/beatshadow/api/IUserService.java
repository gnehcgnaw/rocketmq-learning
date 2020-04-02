package com.beatshadow.api;

import com.beatshadow.entity.Result;
import com.beatshadow.shop.pojo.TradeUser;
import com.beatshadow.shop.pojo.TradeUserMoneyLog;

public interface IUserService {
    TradeUser findOne(Long userId);

    Result updateMoneyPaid(TradeUserMoneyLog userMoneyLog);
}
