package com.beatshadow.exception;

import com.beatshadow.constant.ShopCode;

/**
 * 自定义异常
 * @author gnehcgnaw
 */
public class CustomerException extends RuntimeException{

    private ShopCode shopCode;

    public CustomerException(ShopCode shopCode) {
        this.shopCode = shopCode;
    }
}
