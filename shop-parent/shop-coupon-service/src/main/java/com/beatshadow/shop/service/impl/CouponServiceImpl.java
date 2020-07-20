package com.beatshadow.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.beatshadow.api.ICouponService;
import com.beatshadow.constant.ShopCode;
import com.beatshadow.entity.Result;
import com.beatshadow.exception.CastException;
import com.beatshadow.shop.mapper.TradeCouponMapper;
import com.beatshadow.shop.pojo.TradeCoupon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 优惠卷服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/3 15:06
 */

@Slf4j
@Component
@Service
public class CouponServiceImpl implements ICouponService {

    private TradeCouponMapper tradeCouponMapper ;

    public CouponServiceImpl(TradeCouponMapper tradeCouponMapper) {
        this.tradeCouponMapper = tradeCouponMapper;
    }

    @Override
    public TradeCoupon findOne(Long couponId) {
        return tradeCouponMapper.selectByPrimaryKey(couponId);
    }

    @Override
    public Result changeCouponStatus(TradeCoupon tradeCoupon) {
        try{
            if (tradeCoupon==null||tradeCoupon.getCouponId()!=null) {
                CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
            }
            tradeCouponMapper.updateByPrimaryKey(tradeCoupon);
            return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
        }catch (Exception e){
            return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_FAIL.getMessage());
        }
    }

}
