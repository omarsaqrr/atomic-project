package com.example.infos.service;

import com.example.infos.DTO.PublishMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic}")
    private String topic;

    public PublishMessageResponse publish(JsonNode body) {
        String id = UUID.randomUUID().toString();

        String message = serialize(id, body);

        try {
            SendResult<String, String> result = kafkaTemplate
                    .send(topic, id, message)
                    .get(10, TimeUnit.SECONDS);

            RecordMetadata metadata = result.getRecordMetadata();

            log.info("Published message id={} to topic={} partition={} offset={}",
                    id, metadata.topic(), metadata.partition(), metadata.offset());

            return PublishMessageResponse.builder()
                    .id(id)
                    .topic(metadata.topic())
                    .partition(metadata.partition())
                    .offset(metadata.offset())
                    .message(message)
                    .build();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Kafka publish interrupted", e);
        } catch (ExecutionException | TimeoutException e) {
            log.error("Failed to publish message to topic {}", topic, e);
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Failed to publish message to Kafka", e);
        }
    }

    private String serialize(String id, JsonNode body) {
        try {
            ObjectNode messageBody = objectMapper.createObjectNode();

            messageBody.put("id", id);
            messageBody.set("data", body);

            return objectMapper.writeValueAsString(messageBody);

        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Invalid request body", e);
        }
    }
}