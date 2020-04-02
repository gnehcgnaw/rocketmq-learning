package com.beatshadow.rocketmqdemo.delay;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Date;
import java.util.List;

/**
 * 延迟消费
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 20:57
 */
public class DelayConsumer {
    public static void main(String[] args) {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("delay-consumer-groups");
        defaultMQPushConsumer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQPushConsumer.subscribe("DelayTopic","*");
            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt messageExt :msgs){
                        System.out.println(new Date(messageExt.getStoreTimestamp())+"......"+new String(messageExt.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS ;
                }
            });
            defaultMQPushConsumer.start();
            System.out.println(new Date());
            System.out.println("consumer启动");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}
