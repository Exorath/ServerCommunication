package com.exorath.servercommunication.communication;

import com.exorath.servercommunication.api.communication.PubSub;
import com.exorath.servercommunication.api.communication.SubscriptionConsumer;

import java.util.*;

/**
 * Created by Toon Sevrin on 6/18/2016.
 */
public class MockPubSub implements PubSub {
    private Set<SubscriptionConsumer> consumerSet = new HashSet();
    private List<String> channels = new ArrayList<>();

    @Override
    public void publish(String channel, String message) {
        if(channels.contains(channel))
            consumerSet.forEach(c -> c.onPublish(channel, message));
    }

    @Override
    public void subscribe(String... channels) {
        this.channels.addAll(Arrays.asList(channels));
    }

    @Override
    public void addSubscriptionConsumer(SubscriptionConsumer consumer) {
        consumerSet.add(consumer);
    }

    @Override
    public void close() {

    }
}
