package com.exorath.servercommunication.api.communication;

/**
 * A PubSub manages the publishing and receiving of messages through certain messaging channels.
 * You can see an example implementation at {@link com.exorath.servercommunication.impl.communication.RedisPubSub}
 * Created by Toon Sevrin on 6/14/2016.
 */
public interface PubSub {

    /**
     * Publishes a message to the provided message with this PubSub
     * @param channel Channel to publish message to
     * @param message message to publish
     */
    void publish(String channel, String message);

    /**
     * Makes this PubSub subscribe to said channels.
     * @param channels channels that this pubSub should subscribe to
     */
    void subscribe(String... channels);

    /**
     * This method adds the {@link SubscriptionConsumer} to this PubSub.
     * Whenever a message is received by this PubSub on subcribed channels, it shall notify this {@Link SubscriptionConsumer}.
     * This notify must be send with {@link SubscriptionConsumer#onPublish(String, String)}.
     * @param consumer the consumer that will handle the message
     */
    void addSubscriptionConsumer(SubscriptionConsumer consumer);

    /**
     * Called when this publisher and subscriber should stop functioning.
     * All resources associated with this PubSub should be returned and it should unsubscribe from subscribed channels.
     */
    void close();

}
