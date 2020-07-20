package com.beatshadow.shop.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.beatshadow.api.ICouponService;
import com.beatshadow.api.IGoodsService;
import com.beatshadow.api.IOrderService;
import com.beatshadow.api.IUserService;
import com.beatshadow.constant.ShopCode;
import com.beatshadow.entity.Result;
import com.beatshadow.exception.CastException;
import com.beatshadow.shop.mapper.TradeOrderMapper;
import com.beatshadow.shop.pojo.*;
import com.beatshadow.utils.IDWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单服务实现类
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/2 19:22
 */
@Slf4j
@Component
@Service
public class OrderServiceImpl implements IOrderService {

    @Reference
    private IGoodsService iGoodsService ;

    @Reference
    private IUserService iUserService ;

    @Reference
    private ICouponService iCouponService ;

    @Autowired
    private IDWorker idWorker ;

    @Autowired
    private TradeOrderMapper tradeOrderMapper ;
    /**
     * 确认订单
     * @param tradeOrder
     * @return
     */
    @Override
    public Result confirmOrder(TradeOrder tradeOrder) {
        //1. 校验订单
        checkOrder(tradeOrder);
        //2. 生产预订单
        Long orderId = savePreOrder(tradeOrder);
        try{
            //3. 扣减库存
            reduceGoodsNum(tradeOrder);
            //4. 扣减优惠卷
            changeCouponStatus(tradeOrder);
            //5. 扣减用户余额
        }catch (Exception e){
            //1. 确认订单失败，发送消息
            //2. 返回失败状态

        }
        return null;
    }


    /**
     * 校验订单
     * @param tradeOrder
     */
    private void checkOrder(TradeOrder tradeOrder){
        //1. 判断订单是否为null
        if (tradeOrder==null){
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        //2.判断商品是否存在
        TradeGoods tradeGoods = iGoodsService.findOne(tradeOrder.getGoodsId());
        if (tradeGoods==null){
            CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        //3. 用户是否存在
        TradeUser tradeUser = iUserService.findOne(tradeOrder.getUserId());
        if (tradeUser==null){
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        //4. 商品单价是否合法
        if (tradeGoods.getGoodsPrice().compareTo(tradeOrder.getGoodsPrice())!=0){
            CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID);
        }
        //5. 购买数量是否合法
        if (tradeGoods.getGoodsNumber()>=tradeOrder.getGoodsNumber()){
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        log.info("校验订单通过");

    }

    /**
     * 生成预订单:
     *      其实就是保存订单的一个功能，预订单用户是不可见的。
     * @param tradeOrder
     * @return 订单ID
     */
    private Long savePreOrder(TradeOrder tradeOrder){
        //1、设置订单状态不可见
        tradeOrder.setOrderStatus(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        //2、设置订单ID
        tradeOrder.setOrderId(idWorker.nextId());
        //3、检验运费是否正确
        BigDecimal shippingFee = calculateShippingFee(tradeOrder.getOrderAmount());
        if (tradeOrder.getShippingFee().compareTo(shippingFee)!=0) {
            CastException.cast(ShopCode.SHOP_ORDER_SHIPPINGFEE_INVALID);
        }
        //4、核算订单总价是否正确（订单的总金额包含运费）
        BigDecimal orderAmount = tradeOrder.getGoodsPrice().multiply(new BigDecimal(tradeOrder.getGoodsNumber())).add(shippingFee);
        if (tradeOrder.getOrderAmount().compareTo(orderAmount)!=0){
            CastException.cast(ShopCode.SHOP_ORDERAMOUNT_INVALID);
        }
        //5、是否使用优惠卷、优惠卷是不是合法
        Long couponId = tradeOrder.getCouponId();
        if (couponId != null) {
            TradeCoupon tradeCoupon = iCouponService.findOne(couponId);
            //优惠卷不存在
            if (tradeCoupon==null) {
                CastException.cast(ShopCode.SHOP_COUPON_NO_EXIST);
            }
            //优惠卷已使用
            if ((ShopCode.SHOP_COUPON_ISUSED.getCode().toString()).equals(tradeCoupon.getIsUsed().toString())) {
                CastException.cast(ShopCode.SHOP_COUPON_INVALIED);
            }
            tradeOrder.setCouponPaid(tradeCoupon.getCouponPrice());
        }else {
            tradeOrder.setCouponPaid(BigDecimal.ZERO);
        }
        //6、是否使用余额、余额是不是合法
        BigDecimal moneyPaid = tradeOrder.getMoneyPaid();
        if (moneyPaid!=null){
            //比较余额是否大于0
            int r = moneyPaid.compareTo(BigDecimal.ZERO);
            //余额小于0
            if (r == -1) {
                CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            }
            if (r == 1){
                TradeUser tradeUser = iUserService.findOne(tradeOrder.getUserId());
                if (tradeUser == null) {
                    CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
                }
                if (tradeUser.getUserMoney().compareTo(moneyPaid.longValue())==-1){
                    CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALID);
                }
                tradeOrder.setMoneyPaid(moneyPaid);
            }
        }else {
            tradeOrder.setMoneyPaid(BigDecimal.ZERO);
        }
        //7、核算订单的总价
        tradeOrder.setPayAmount(orderAmount.subtract(tradeOrder.getCouponPaid()).subtract(tradeOrder.getMoneyPaid()));
        //8、设置订单时间
        tradeOrder.setAddTime(new Date());
        //9、保存订单到数据库
        int insert = tradeOrderMapper.insert(tradeOrder);
        if (ShopCode.SHOP_SUCCESS.getCode() != insert) {
            CastException.cast(ShopCode.SHOP_ORDER_SAVE_ERROR);
        }
        log.info("订单:["+tradeOrder.getOrderId()+"]预订单生成成功");
        return tradeOrder.getOrderId();
    }

    /**
     * 核算订单运费
     */
    private BigDecimal calculateShippingFee(BigDecimal orderAmount){
        if (orderAmount.compareTo(new BigDecimal(100))==1){
            return BigDecimal.ZERO ;
        }else {
            return new BigDecimal(10);
        }
    }

    /**
     * 扣减库存
     * @param tradeOrder
     */
    private void reduceGoodsNum(TradeOrder tradeOrder) {
        TradeGoodsNumberLog tradeGoodsNumberLog = new TradeGoodsNumberLog();
        tradeGoodsNumberLog.setOrderId(tradeOrder.getOrderId());
        tradeGoodsNumberLog.setGoodsNumber(tradeOrder.getGoodsNumber());
        tradeGoodsNumberLog.setGoodsId(tradeOrder.getGoodsId());
        Result result = iGoodsService.reduceGoodsNum(tradeGoodsNumberLog);
        if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())){
            CastException.cast(ShopCode.SHOP_REDUCE_GOODS_NUM_FAIL);
        }
        log.info("订单:["+tradeOrder.getOrderId()+"]扣减库存["+tradeOrder.getGoodsNumber()+"个]成功");
    }

    /**
     * 扣减优惠卷
     * @param tradeOrder
     */
    private void changeCouponStatus(TradeOrder tradeOrder) {
        //判断用户是否使用优惠卷
        if (tradeOrder.getCouponId()!=null) {
            TradeCoupon tradeCoupon = iCouponService.findOne(tradeOrder.getCouponId());
            tradeCoupon.setIsUsed(ShopCode.SHOP_COUPON_ISUSED.getCode());
            tradeCoupon.setUsedTime(new Date());
            tradeCoupon.setOrderId(tradeOrder.getOrderId());
            Result result = iCouponService.changeCouponStatus(tradeCoupon);
            //判断执行结果
            if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
                //优惠券使用失败
                CastException.cast(ShopCode.SHOP_COUPON_USE_FAIL);
            }
            log.info("订单:["+tradeOrder.getOrderId()+"]使用扣减优惠券["+tradeCoupon.getCouponPrice()+"元]成功");
        }
    }
}
