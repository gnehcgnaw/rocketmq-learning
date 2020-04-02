package com.beatshadow.rocketmqdemo.filter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 22:28
 */
public class FilterProducer {
    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("filter-message-group");
        producer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            producer.start();
            for (int i = 0; i < 10 ; i++) {
                Message msg = new Message("filterTopic",
                        "tagA",
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );
                // 设置一些属性
                msg.putUserProperty("a", String.valueOf(i));
                SendResult sendResult = producer.send(msg);
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            producer.shutdown();
        }

    }
}
