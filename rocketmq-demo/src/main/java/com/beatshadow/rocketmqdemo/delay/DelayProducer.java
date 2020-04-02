package com.beatshadow.rocketmqdemo.delay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 21:03
 */
public class DelayProducer {
    public static void main(String[] args) {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("delay_producer_groups");
        defaultMQProducer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQProducer.start();
            for (int i = 0; i < 100 ; i++) {
                Message message = new Message("DelayTopic", ("delay-message-" + i).getBytes());
                // org/apache/rocketmq/store/config/MessageStoreConfig.java
                // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
                message.setDelayTimeLevel(3);
                SendResult sendResult = defaultMQProducer.send(message);
                System.out.println(sendResult);
            }

        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } finally {
            defaultMQProducer.shutdown();
        }

    }
}
