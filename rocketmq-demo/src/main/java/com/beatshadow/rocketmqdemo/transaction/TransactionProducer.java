package com.beatshadow.rocketmqdemo.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * 事务消息
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/4/1 23:02
 */
public class TransactionProducer{
    public static void main(String[] args) {
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("transaction-messages-group");
        TransactionListener transactionListener = new TransactionListenerImpl();
        transactionMQProducer.setNamesrvAddr("10.211.55.20:9876;10.211.55.21:9876");
        try {
            transactionMQProducer.start();
            ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("client-transaction-msg-check-thread");
                    return thread;
                }
            });
            transactionMQProducer.setExecutorService(executorService);
            transactionMQProducer.setTransactionListener(transactionListener);
            String[] tags = new String[] {"TagA", "TagB", "TagC"};
            for (int i = 0; i < 3 ; i++) {
                try {
                    Message msg = new Message("TransactionTopic", tags[i % tags.length], "KEY" + i,
                            ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                    SendResult sendResult = transactionMQProducer.sendMessageInTransaction(msg, null);
                    System.out.printf("%s%n", sendResult);
                    TimeUnit.SECONDS.sleep(1);
                } catch (MQClientException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            transactionMQProducer.shutdown();
        }
    }
}
