package com.beatshadow.shop.pojo;

import java.io.Serializable;
import java.util.Date;
/**
 * @author gnehcgnaw
 */
public class TradeGoodsNumberLog extends TradeGoodsNumberLogKey implements Serializable {
    private Integer goodsNumber;

    private Date logTime;

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }
}