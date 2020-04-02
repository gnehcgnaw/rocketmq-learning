package com.beatshadow.rocketmqdemo.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 广播模式
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/3/30 20:54
 */
public class ConsumerByMessageModelBroadcasting {
    public static void main(String[] args) {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("group1");
        defaultMQPushConsumer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        //设置消费者消费模式，默认是负载均衡模式
        defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
        try {
            defaultMQPushConsumer.subscribe("base","*");
            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt messageExt :msgs){
                        System.out.println(new String(messageExt.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}
