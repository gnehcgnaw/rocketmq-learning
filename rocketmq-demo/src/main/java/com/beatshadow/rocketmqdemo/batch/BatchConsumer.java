package com.beatshadow.rocketmqdemo.batch;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 21:54
 */
public class BatchConsumer {
    public static void main(String[] args) {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("batch-consumer-group");
        defaultMQPushConsumer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQPushConsumer.subscribe("batchTopic","*");
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
