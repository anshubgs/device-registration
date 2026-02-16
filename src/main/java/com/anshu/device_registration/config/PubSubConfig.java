package com.anshu.device_registration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;

@Configuration
public class PubSubConfig {

    @Value("${house.pubsub.subscription}")
    private String houseSubscription;

    @Value("${user.pubsub.subscription}")
    private String userSubscription;

    // -------------------- Channels --------------------
    @Bean
    public MessageChannel houseInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel userInputChannel() {
        return new DirectChannel();
    }

    // -------------------- Adapters --------------------
    @Bean
    public PubSubInboundChannelAdapter houseInboundAdapter(
            PubSubTemplate pubSubTemplate,
            MessageChannel houseInputChannel
    ) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, houseSubscription);

        adapter.setOutputChannel(houseInputChannel);
        adapter.setAckMode(AckMode.AUTO);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter userInboundAdapter(
            PubSubTemplate pubSubTemplate,
            MessageChannel userInputChannel
    ) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, userSubscription);

        adapter.setOutputChannel(userInputChannel);
        adapter.setAckMode(AckMode.AUTO);
        return adapter;
    }
}
