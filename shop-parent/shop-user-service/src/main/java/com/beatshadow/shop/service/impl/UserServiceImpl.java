package com.beatshadow.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.beatshadow.api.IUserService;
import com.beatshadow.shop.mapper.TradeUserMapper;
import com.beatshadow.shop.pojo.TradeUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/3 13:53
 */
@Slf4j
@Component
@Service
public class UserServiceImpl implements IUserService {

    private TradeUserMapper tradeUserMapper ;

    public UserServiceImpl(TradeUserMapper tradeUserMapper) {
        this.tradeUserMapper = tradeUserMapper;
    }

    @Override
    public TradeUser findOne(long userId) {
        return tradeUserMapper.selectByPrimaryKey(userId);
    }
}
