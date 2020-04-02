package com.beatshadow.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.beatshadow.api.ICouponService;
import com.beatshadow.constant.ShopCode;
import com.beatshadow.entity.Result;
import com.beatshadow.exception.CastException;
import com.beatshadow.shop.mapper.TradeCouponMapper;
import com.beatshadow.shop.pojo.TradeCoupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = ICouponService.class)
public class CouponServiceImpl implements ICouponService{

    @Autowired
    private TradeCouponMapper couponMapper;

    @Override
    public TradeCoupon findOne(Long coupouId) {
        if(coupouId==null){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }

        return couponMapper.selectByPrimaryKey(coupouId);
    }

    @Override
    public Result updateCouponStatus(TradeCoupon coupon) {
        if(coupon==null||coupon.getCouponId()==null){
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        //更新优惠券状态
        couponMapper.updateByPrimaryKey(coupon);
        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(),ShopCode.SHOP_SUCCESS.getMessage());
    }
}
