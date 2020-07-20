package com.beatshadow.api;

import com.beatshadow.shop.pojo.TradeUser;

/**
 * 用户服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/3 13:51
 */
public interface IUserService {
    /**
     * 根据userId获取用户信息
     * @param userId
     * @return
     */
    TradeUser findOne(long userId);
}
