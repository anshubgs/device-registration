package com.anshu.device_registration.publisher;

import com.anshu.device_registration.event.DeviceRegisteredEvent;
import com.anshu.device_registration.event.DeviceStatusUpdatedEvent;
import com.anshu.device_registration.publisher.DeviceEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PubSubDeviceEventPublisher implements DeviceEventPublisher {

    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;

    @Value("${pubsub.topic.device-registered}")
    private String deviceRegisteredTopic;

    @Value("${pubsub.topic.device-status-updated}")
    private String deviceStatusUpdatedTopic;

    @Override
    public void publishDeviceRegistered(DeviceRegisteredEvent event) {
        publish(deviceRegisteredTopic, event, "DeviceRegisteredEvent");
    }

    @Override
    public void publishDeviceStatusUpdated(DeviceStatusUpdatedEvent event) {
        publish(deviceStatusUpdatedTopic, event, "DeviceStatusUpdatedEvent");
    }

    // 🔒 Single responsibility: publishing
    private void publish(String topic, Object event, String eventName) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            log.info(
                    "📤 Publishing {} | topic={} | payload={}",
                    eventName, topic, payload
            );

            pubSubTemplate.publish(topic, payload);

            log.info("✅ {} published successfully", eventName);

        } catch (Exception e) {
            log.error("❌ Failed to publish {}", eventName, e);
        }
    }
}

