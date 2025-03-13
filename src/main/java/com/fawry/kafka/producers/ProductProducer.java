package com.fawry.kafka.producers;

import com.fawry.kafka.events.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class ProductProducer {

    private static final Logger log = LoggerFactory.getLogger(ProductProducer.class);
    private static final String TOPIC_NAME = "product-event";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProductProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceProductEvent(ProductEvent event) {
        Message<ProductEvent> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();

        kafkaTemplate.send(message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent message=[{}] with offset=[{}]", event, result.getRecordMetadata().offset());
                    } else {
                        log.error("Unable to send message=[{}] due to: {}", event, ex.getMessage(), ex);
                    }
                });
    }
}
