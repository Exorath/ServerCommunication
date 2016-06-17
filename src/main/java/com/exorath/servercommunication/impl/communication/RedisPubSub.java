package com.exorath.servercommunication.impl.communication;

import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toon Sevrin on 6/14/2016.
 */
public class RedisPubSub implements PubSub {
    private List<SubscriptionConsumer> subscriptionConsumers = new ArrayList<>();
    private JedisPubSub jedisPubSub;
    private JedisPool jedisPool;

    public RedisPubSub(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        setupJedisPubSub();
    }

    private void setupJedisPubSub(){
        this.jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                RedisPubSub.this.onMessage(channel, message);
            }
        };
    }
    private void onMessage(String channel, String message) {
        for(SubscriptionConsumer consumer : subscriptionConsumers){
            try{
                consumer.onPublish(channel, message);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void subscribe(String... channels) {
        new Thread(() -> {
            try (Jedis resource = jedisPool.getResource()) {
                resource.subscribe(jedisPubSub, channels);
            }
        }, "subscriberThread").start();
    }

    @Override
    public void addSubscriptionConsumer(SubscriptionConsumer consumer) {
        subscriptionConsumers.add(consumer);
    }

    @Override
    public void publish(String channel, String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            new Thread(() -> {
                jedis.publish(channel, message);
            }, "publisherThread").start();
        }
    }

    @Override
    public void close() {
        if (jedisPubSub.isSubscribed())
            jedisPubSub.unsubscribe();
    }
}
