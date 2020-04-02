package com.beatshadow.rocketmqdemo.base.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

/**
 * 发送异步消息
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/3/30 21:13
 */
public class AsyncProducer {
    public static void main(String[] args) {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("group2");
        defaultMQProducer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            defaultMQProducer.start();
            defaultMQProducer.setRetryTimesWhenSendAsyncFailed(0);
            for (int i = 0; i < 10 ; i++) {
                Message message = new Message("base","tag2",("Hello RocketMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                defaultMQProducer.send(message, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println(sendResult);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
                //线程睡一下
                //Thread.sleep(100);
                //
                TimeUnit.SECONDS.sleep(1);
            }

        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            defaultMQProducer.shutdown();
        }

    }
}
