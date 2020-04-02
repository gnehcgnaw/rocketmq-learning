package com.beatshadow.rocketmqdemo.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 21:51
 */
public class BatchProduce {
    public static void main(String[] args) {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("batch-messages-group");
        defaultMQProducer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQProducer.start();
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("batchTopic", "TagA", "OrderID001", "Hello world 0".getBytes()));
            messages.add(new Message("batchTopic", "TagA", "OrderID002", "Hello world 1".getBytes()));
            messages.add(new Message("batchTopic", "TagA", "OrderID003", "Hello world 2".getBytes()));
            SendResult sendResult = defaultMQProducer.send(messages);
            System.out.println(sendResult);
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
