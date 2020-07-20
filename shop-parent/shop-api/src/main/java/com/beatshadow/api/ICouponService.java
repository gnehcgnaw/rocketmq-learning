package com.beatshadow.api;

import com.beatshadow.entity.Result;
import com.beatshadow.shop.pojo.TradeCoupon;

/**
 * 优惠卷服务
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/3 14:54
 */
public interface ICouponService {
    /**
     * 根据couponId获取优惠卷
     * @param couponId 优惠卷Id
     * @return
     */
    TradeCoupon findOne(Long couponId);

    /**
     * 扣减优惠卷
     * @param tradeCoupon
     * @return
     */
    Result changeCouponStatus(TradeCoupon tradeCoupon);

}
