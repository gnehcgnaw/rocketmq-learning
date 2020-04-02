package com.beatshadow.rocketmqdemo.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 19:12
 */
public class Consumer {
    public static void main(String[] args) {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("order_consumer_groups");
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQPushConsumer.subscribe("TopicOrder","*");
            //使用messageListenerOrderly保证当前queue使用单线程完成消息消费
            defaultMQPushConsumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                    for (MessageExt messageExt :msgs){
                        System.out.println(Thread.currentThread().getName()+"......"+new String(messageExt.getBody()));
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }


    }
}
