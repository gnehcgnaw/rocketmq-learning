package com.beatshadow.rocketmqdemo.base.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 发送同步消息
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/3/30 20:53
 */
public class SyncProducer {
    public static void main(String[] args) {
        //1. 创建消息生产者producer，并指定生产者组名
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("group1");
        //2. 指定NameServer地址
        defaultMQProducer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQProducer.start();
            for (int i = 0; i < 10 ; i++) {
                Message message  = new Message("base","tag1",("Hello RocketMQ"+i).getBytes());
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
            if (defaultMQProducer!=null){
                defaultMQProducer.shutdown();
            }
        }

    }
}
