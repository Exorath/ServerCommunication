package com.exorath.servercommunication.communication;

import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;

/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class MockPubSub implements PubSub {
    @Override
    public void publish(String channel, String message) {

    }

    @Override
    public void subscribe(String... channels) {

    }

    @Override
    public void addSubscriptionConsumer(SubscriptionConsumer consumer) {

    }

    @Override
    public void close() {

    }
}
