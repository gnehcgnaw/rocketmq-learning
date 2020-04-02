package com.beatshadow.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.beatshadow.api.IOrderService;
import com.beatshadow.entity.Result;
import com.beatshadow.shop.pojo.TradeOrder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderControllre {

    @Reference
    private IOrderService orderService;

    @RequestMapping("/confirm")
    public Result confirmOrder(@RequestBody TradeOrder order){
        return orderService.confirmOrder(order);
    }

}
