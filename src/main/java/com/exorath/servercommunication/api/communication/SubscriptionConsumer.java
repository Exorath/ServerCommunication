package com.exorath.servercommunication.api.communication;

/**
 * Created by Toon Sevrin on 6/14/2016.
 */
public interface SubscriptionConsumer {
    void onPublish(String channel, String message);
}
