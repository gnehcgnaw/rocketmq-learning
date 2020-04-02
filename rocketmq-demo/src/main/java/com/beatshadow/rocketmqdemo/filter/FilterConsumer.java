package com.beatshadow.rocketmqdemo.filter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 22:26
 */
public class FilterConsumer {
    public static void main(String[] args) {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("filter-consumer-groups");
        defaultMQPushConsumer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");

        try {
            // 只有订阅的消息有这个属性a, a >=0 and a <= 3
            defaultMQPushConsumer.subscribe("filterTopic", MessageSelector.bySql("a > 0 and a < 3"));
            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt messageExt : msgs){
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
