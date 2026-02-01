package com.anshu.device_registration.publisher;

import com.anshu.device_registration.event.DeviceRegisteredEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceEventPublisher {

    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper;

    @Value("${pubsub.topic.device-registered}")
    private String deviceRegisteredTopic;

    public void publishEvent(DeviceRegisteredEvent event) {
        try {
            // Convert event → JSON
            String message = objectMapper.writeValueAsString(event);

            // 🔥 FULL JSON LOG
            log.info(
                    "📤 Publishing DeviceRegisteredEvent | topic={} | payload={}",
                    deviceRegisteredTopic,
                    message);

            // Publish to Pub/Sub
            pubSubTemplate.publish(deviceRegisteredTopic, message);

            log.info("✅ DeviceRegisteredEvent successfully published");

        } catch (Exception e) {
            log.error("❌ Failed to publish DeviceRegisteredEvent", e);
        }
    }
}
